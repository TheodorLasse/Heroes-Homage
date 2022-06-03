package src.data;

import src.Game;
import src.data.input.GameKeyListener;
import src.data.input.Key;
import src.data.input.KeyEvent;
import src.data.input.KeyState;
import src.data.time.DeltaTime;
import src.sprites.Entities.EntityHandler;
import src.sprites.Entities.MapEntity;
import src.sprites.Entities.MapLivingEntity;
import src.sprites.Entities.TeamType;
import src.sprites.Sprite;
import src.sprites.SpriteHandler;
import src.sprites.SpriteLayer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static java.lang.Double.max;
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

    Vector2D mapFocus = new Vector2D(0,0);

    public GameMap(Dimension screenSize)
    {
        this.screenSize = screenSize;
        background = new BufferedImage(mapSize.width * TILE_SIZE, mapSize.height * TILE_SIZE, TYPE_INT_ARGB);
        mapSpriteHandler = new SpriteHandler();
        mapEntityHandler = new EntityHandler();
        mapTiles = new ArrayList<>();
        mapEntityHandler.add(new MapEntity(new Vector2D(19,20), Game.imageLoader.getImage(ImageLoader.ImageName.ROCK)));
        mapEntityHandler.add(new MapEntity(new Vector2D(20,19), Game.imageLoader.getImage(ImageLoader.ImageName.ROCK)));
        mapEntityHandler.add(new MapEntity(new Vector2D(21,18), Game.imageLoader.getImage(ImageLoader.ImageName.ROCK)));
        mapEntityHandler.add(new MapLivingEntity(new Vector2D(21,18), Game.imageLoader.getImage(ImageLoader.ImageName.ROCK), TeamType.BLUE));
        init();

    }

    private void init(){
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

    public void update(DeltaTime deltaTime, Dimension screenSize){
        mapSpriteHandler.update(deltaTime);
        mapEntityHandler.update(deltaTime, mapFocus);
    }

    public ArrayList<Sprite> getIterator(Vector2D scope){
        ArrayList<Sprite> iterList = new ArrayList<>();

        //TODO have this refer to size of object in focus, when focus function is implemented
        Vector2D size = new Vector2D();

        Vector2D centre = Vector2D.getSum(mapFocus, new Vector2D(size.getX()/2, size.getY()/2));
        double posX = centre.getX() - scope.getX()/2;
        double posY = centre.getY() - scope.getY()/2;
        double width = scope.getX();
        double height = scope.getY();
        posX = max(posX, 0);
        posY = max(posY, 0);
        width = min(width, mapSize.getWidth());
        height = min(height, mapSize.getHeight());

        mapSpriteHandler.setBackground(background.getSubimage(
                (int) (posX * TILE_SIZE), (int) (posY * TILE_SIZE),
                (int) (width * TILE_SIZE), (int) (height * TILE_SIZE)));

        iterList.addAll(mapSpriteHandler.getLayerIterator(SpriteLayer.FIRST));

        for (Sprite entity:mapEntityHandler.getIterator()) {
            Vector2D entityPos = entity.getPosition();
            boolean inScope =
                    entityPos.getX() >= posX + entity.getSize().getX() &&
                    entityPos.getY() >= posY + entity.getSize().getY() &&
                    entityPos.getX() <= width &&
                    entityPos.getY() <= height;

            if (inScope){
                iterList.add(entity);
            }
        }

        return iterList;
    }

    @Override
    public void onKeyEvent(KeyEvent e, AbstractMap<Key, KeyState> keyStates) {
        double mapShiftStep = 1;
        switch (e.getKey()) {
            case LEFT -> {
                mapFocus.addX(-mapShiftStep);
                if (mapFocus.getX() < 0) mapFocus.setX(0);
            }
            case RIGHT -> {
                mapFocus.addX(mapShiftStep);
                if (mapFocus.getX() > mapSize.getWidth() * TILE_SIZE - screenSize.getWidth()) mapFocus.setX(0);
            }
            case UP -> {
                mapFocus.addY(-mapShiftStep);
                if (mapFocus.getY() < 0) mapFocus.setY(0);
            }
            case DOWN -> {
                mapFocus.addY(mapShiftStep);
                if (mapFocus.getY() > mapSize.getHeight() * TILE_SIZE - screenSize.getHeight()) mapFocus.setY(0);
            }
        }
    }
}
