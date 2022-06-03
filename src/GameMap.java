package src;

import src.sprites.Entities.*;
import src.tools.ImageLoader;
import src.tools.MapFocus;
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
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static java.lang.Math.min;

enum MapTileType { WATER, GRASS, ROAD, PAVEMENT, ERROR}

public class GameMap implements GameKeyListener {
    public static final int TILE_SIZE = 20;
    List<List<MapTileType>> mapTiles = null;
    SpriteHandler mapSpriteHandler = null;
    EntityHandler mapEntityHandler = null;
    BufferedImage background;
    Dimension mapSize = new Dimension(500,500);
    Dimension screenSize;

    MapFocus mapFocus;

    public GameMap(Dimension screenSize)
    {
        this.screenSize = screenSize;
        background = new BufferedImage(mapSize.width * TILE_SIZE, mapSize.height * TILE_SIZE, TYPE_INT_ARGB);
        mapSpriteHandler = new SpriteHandler();
        mapEntityHandler = new EntityHandler();
        mapFocus = new MapFocus(new Vector2D(), screenSize, mapSize);
        mapTiles = new ArrayList<>();
        mapEntityHandler.add(new MapEntity(new Vector2D(0,0), Game.imageLoader.getImage(ImageLoader.ImageName.ROCK)));
        mapEntityHandler.add(new MapEntity(new Vector2D(20,19), Game.imageLoader.getImage(ImageLoader.ImageName.ROCK)));
        mapEntityHandler.add(new MapEntity(new Vector2D(21,18), Game.imageLoader.getImage(ImageLoader.ImageName.ROCK)));
        mapEntityHandler.add(new MapLivingEntity(new Vector2D(21,18), Game.imageLoader.getImage(ImageLoader.ImageName.ROCK), TeamType.BLUE));
        initBackground();

    }

    private void initBackground(){
        Graphics g = background.getGraphics();
        for (int y = 0; y < mapSize.height; y++){
            List<MapTileType> current;
            current = new ArrayList<>(mapSize.width);

            for (int x = 0; x < mapSize.width; x++){
                MapTileType type = MapTileType.GRASS;

                BufferedImage toDraw;

                switch (type){
                    case GRASS -> toDraw = Game.imageLoader.getImage(ImageLoader.ImageName.GRASS);
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

    public void onMouseClick(int x, int y){
        Vector2D mousePos = new Vector2D(x, y);
        Vector2D mouseMapFocus = Vector2D.getSum(mousePos, mapFocus.getPosition());
        for (Entity mapEntity : mapEntityHandler.getIterator()) {
            if (mapEntity.isOverlap(mouseMapFocus)){
                mapEntity.onClick();
            }
        }
    }

    @Override
    public void onKeyEvent(KeyEvent e, AbstractMap<Key, KeyState> keyStates) {
        double mapShiftStep = 1;
        switch (e.getKey()) {
            case LEFT -> {
                mapFocus.addX(-mapShiftStep);
            }
            case RIGHT -> {
                mapFocus.addX(mapShiftStep);
            }
            case UP -> {
                mapFocus.addY(-mapShiftStep);
            }
            case DOWN -> {
                mapFocus.addY(mapShiftStep);
            }
        }
    }
}
