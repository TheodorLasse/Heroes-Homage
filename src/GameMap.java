package src;

import src.sprites.Entities.*;
import src.tools.ImageLoader;
import src.tools.MapFocus;
import src.tools.ShortestPath;
import src.tools.Vector2D;
import src.tools.input.GameKeyListener;
import src.tools.input.Key;
import src.tools.input.KeyEvent;
import src.tools.input.KeyState;
import src.tools.time.DeltaTime;
import src.sprites.Sprite;
import src.sprites.SpriteHandler;
import src.sprites.SpriteLayer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static java.lang.Math.min;

enum MapTileType { WATER, GRASS, ROAD, PAVEMENT, ERROR}

public class GameMap implements GameKeyListener {
    public static final int TILE_SIZE = 20;
    List<List<MapTileType>> mapTiles;
    SpriteHandler mapSpriteHandler;
    EntityHandler mapEntityHandler;
    BufferedImage background;
    Dimension mapSize = new Dimension(500,500);
    Dimension screenSize;

    MapFocus mapFocus;
    Entity entityFocus;

    public GameMap(Dimension screenSize)
    {
        this.screenSize = screenSize;
        background = new BufferedImage(mapSize.width * TILE_SIZE, mapSize.height * TILE_SIZE, TYPE_INT_ARGB);
        mapSpriteHandler = new SpriteHandler();
        mapEntityHandler = new EntityHandler();
        mapFocus = new MapFocus(new Vector2D(), screenSize, mapSize);
        mapTiles = new ArrayList<>();
        initBackground();

        mapEntityHandler.add(new MapEntity(new Vector2D(0,0), Game.imageLoader.getImage(ImageLoader.ImageName.ROCK)));
        mapEntityHandler.add(new MapEntity(new Vector2D(20,19), Game.imageLoader.getImage(ImageLoader.ImageName.ROCK)));
        mapEntityHandler.add(new MapEntity(new Vector2D(21,18), Game.imageLoader.getImage(ImageLoader.ImageName.ROCK)));
        mapEntityHandler.add(new MapLivingEntity(new Vector2D(30,18), Game.imageLoader.getImage(ImageLoader.ImageName.ROCK), TeamType.BLUE));
    }

    private void initBackground(){
        Graphics g = background.getGraphics();
        for (int y = 0; y < mapSize.height; y++){
            List<MapTileType> current;
            current = new ArrayList<>(mapSize.width);

            for (int x = 0; x < mapSize.width; x++){
                MapTileType type = MapTileType.GRASS;
                if (y <= 5){
                    type = MapTileType.WATER;
                }

                BufferedImage toDraw;

                switch (type){
                    case GRASS -> toDraw = Game.imageLoader.getImage(ImageLoader.ImageName.GRASS);
                    case WATER -> toDraw = Game.imageLoader.getImage(ImageLoader.ImageName.WATER);
                    default -> toDraw = Game.imageLoader.getImage(ImageLoader.ImageName.ERROR);
                }

                g.drawImage(toDraw, x * TILE_SIZE, y * TILE_SIZE, null);
                current.add(type);
            }

            mapTiles.add(current);
        }
    }

    public void update(DeltaTime deltaTime){
        mapSpriteHandler.update(deltaTime);
        mapEntityHandler.update(deltaTime, mapFocus);
    }

    public ArrayList<Sprite> getIterator(){
        int width = (int) min(screenSize.getWidth() / TILE_SIZE, mapSize.getWidth());
        int height = (int) min(screenSize.getHeight() / TILE_SIZE, mapSize.getHeight());

        mapSpriteHandler.setBackground(background.getSubimage(
                (int) (mapFocus.getX() * TILE_SIZE), (int) (mapFocus.getY() * TILE_SIZE),
                (width * TILE_SIZE), (height * TILE_SIZE)));

        ArrayList<Sprite> iterList = new ArrayList<>(mapSpriteHandler.getLayerIterator(SpriteLayer.FIRST));
        iterList.addAll(mapEntityHandler.getIterator());

        return iterList;
    }

    public void onMouseClick(Vector2D mousePos, int mouseButton){
        Vector2D mouseMapPos = new Vector2D(mousePos.getX() / TILE_SIZE, mousePos.getY() / TILE_SIZE);
        Vector2D mouseMapFocus = Vector2D.getSum(mouseMapPos, mapFocus.getPosition());
        calculateBlocked();
        if(mouseButton == 1){
            for (Entity mapEntity : mapEntityHandler.getIterator()) {
                if (mapEntity.isOverlap(mouseMapFocus)){
                    entityFocus = mapEntity;
                    return;
                }
            }
        }
        else if (mouseButton == 3){
            setPath(entityFocus, mouseMapPos);
        }
    }

    private void setPath(Entity entity, Vector2D mapPos){
        int[][] map = calculateBlocked();
        ShortestPath.Point entityPoint = new ShortestPath.Point((int) entity.getPosition().getX(), (int) entity.getPosition().getY(), null);
        ShortestPath.Point destinationPoint = new ShortestPath.Point((int) mapPos.getX(), (int) mapPos.getY(), null);
        List<ShortestPath.Point> path = ShortestPath.FindPath(map, entityPoint, destinationPoint);
        entity.setPath(path);
    }

    private int[][] calculateBlocked(){
        int[][] blocked = new int[mapSize.height][mapSize.width];
        for (int y = 0; y < mapTiles.size(); y++){
            for (int x = 0; x < mapTiles.get(y).size(); x++){
                switch (mapTiles.get(y).get(x)){
                    case WATER -> blocked[y][x] = 1;
                }
            }
        }

        for (Entity mapEntity : mapEntityHandler.getIterator()) {
            Vector2D entityPos = mapEntity.getPosition();
            for (int y = 0; y < mapEntity.getSize().getY(); y++) {
                for (int x = 0; x < mapEntity.getSize().getX(); x++) {
                    int iterX = (int)(entityPos.getX() + x);
                    int iterY = (int)(entityPos.getY() + y);
                    blocked[iterY][iterX] = 1;
                }
            }
        }

        return blocked;
    }

    @Override
    public void onKeyEvent(KeyEvent e, AbstractMap<Key, KeyState> keyStates) {
        double mapShiftStep = 1;
        switch (e.getKey()) {
            case LEFT -> mapFocus.addX(-mapShiftStep);
            case RIGHT -> mapFocus.addX(mapShiftStep);
            case UP -> mapFocus.addY(-mapShiftStep);
            case DOWN -> mapFocus.addY(mapShiftStep);
        }
    }
}
