package src.data;

import src.Game;
import src.sprites.Entities.MapEntity;
import src.sprites.Sprite;
import src.sprites.SpriteHandler;
import src.sprites.SpriteLayer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static java.lang.Double.max;
import static java.lang.Math.min;

enum MapTileType { WATER, GRASS, ROAD, PAVEMENT, ERROR}

public class GameMap {
    public static final int TILE_SIZE = 50;
    List<MapEntity> mapEntities = null;
    List<List<MapTileType>> mapTiles = null;
    SpriteHandler mapSpriteHandler = null;
    BufferedImage background;
    Dimension mapSize = new Dimension(500,500);

    public GameMap()
    {
        background = new BufferedImage(mapSize.width * TILE_SIZE, mapSize.height * TILE_SIZE, TYPE_INT_ARGB);
        mapSpriteHandler = new SpriteHandler();
        mapTiles = new ArrayList<>();
        mapEntities = new ArrayList<>();
        mapEntities.add(new MapEntity(new Vector2D(3,3), Game.imageLoader.getImage(ImageLoader.ImageName.ROCK)));
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

    public ArrayList<Sprite> getIterator(Vector2D position, Vector2D size, Vector2D scope){
        Vector2D centre = Vector2D.getSum(position, new Vector2D(size.getX()/2, size.getY()/2));
        double posX = centre.getX() - scope.getX()/2;
        double posY = centre.getY() - scope.getY()/2;
        double width = scope.getX();
        double height = scope.getY();
        posX = max(posX, 0);
        posY = max(posY, 0);
        width = min(width, mapSize.getWidth());
        height = min(height, mapSize.getHeight());

        mapSpriteHandler.setBackground(background.getSubimage(
                (int) posX * TILE_SIZE, (int) posY * TILE_SIZE,
                (int) width * TILE_SIZE, (int) height * TILE_SIZE));

        return mapSpriteHandler.getLayerIterator(SpriteLayer.FIRST);
    }
}
