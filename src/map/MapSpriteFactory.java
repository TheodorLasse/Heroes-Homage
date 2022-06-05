package src.map;

import src.Game;
import src.sprites.SpriteTexture;
import src.tools.image.ImageLoader;
import src.tools.MapTileType;
import src.tools.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static src.map.GameMap.TILE_SIZE;

public class MapSpriteFactory {
    Dimension screenSize;
    public MapSpriteFactory(Dimension screenSize){
        this.screenSize = screenSize;
    }

    /**
     * Creates and properly rotates the borders of the map
     */
    public List<SpriteTexture> createBorders(){
        List<BufferedImage> borders = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int size;
            if (i%2 == 1) size = screenSize.height;
            else size = screenSize.width;

            borders.add(createBorder(size));
        }

        BufferedImage corner = Game.imageLoader.getImage(ImageLoader.ImageName.GAME_BORDER_CORNER);

        List<Vector2D> borderPositions = Arrays.asList(
                new Vector2D(0, 0),
                new Vector2D(screenSize.getWidth() - borders.get(1).getHeight(), 0),
                new Vector2D(screenSize.getWidth() - borders.get(0).getWidth(), screenSize.getHeight() - borders.get(0).getHeight()),
                new Vector2D(0, screenSize.getHeight() - borders.get(1).getWidth()));

        List<Vector2D> cornerDecorationPositions = Arrays.asList(
                new Vector2D(0, 0),
                new Vector2D(screenSize.getWidth() - corner.getHeight(), 0),
                new Vector2D(screenSize.getWidth() - corner.getWidth(), screenSize.getHeight() - corner.getHeight()),
                new Vector2D(0, screenSize.getHeight() - corner.getWidth()));

        List<SpriteTexture> borderTextures = new ArrayList<>();
        for (int i = 0; i < borderPositions.size(); i++) {
            double rotation = i * Math.PI * 0.5;
            borderTextures.add(0, new SpriteTexture(borderPositions.get(i), rotation, borders.get(i)));
            borderTextures.add(new SpriteTexture(cornerDecorationPositions.get(i), rotation, corner));
        }

        return borderTextures;
    }


    /**
     * Creates one of the map's borders
     * @param length Length of the border
     * @return Border as an image
     */
    private BufferedImage createBorder(int length){
        BufferedImage borderComponent = Game.imageLoader.getImage(ImageLoader.ImageName.GAME_BORDER);
        BufferedImage borderEdge = new BufferedImage(length, borderComponent.getHeight(), TYPE_INT_ARGB);

        int componentsLong = (int) Math.ceil((double) length / borderComponent.getWidth());

        Graphics g = borderEdge.getGraphics();
        for (int i = 0; i < componentsLong; i++) {
            g.drawImage(borderComponent, i * borderComponent.getWidth(), 0, null);
        }
        return borderEdge;
    }

    /**
     * Create background image and initialize mapTiles
     * @param mapSize Size of the map the background is made from
     * @param mapTiles List of list of the tile types the map consists of
     * @return Fully developed background image
     */
    public BufferedImage createBackground(Dimension mapSize, List<List<MapTileType>> mapTiles){
        BufferedImage bg = new BufferedImage(mapSize.width * TILE_SIZE, mapSize.height * TILE_SIZE, TYPE_INT_ARGB);
        Graphics g = bg.getGraphics();
        for (int x = 0; x < mapSize.width; x++){
            List<MapTileType> current;
            current = new ArrayList<>(mapSize.height);

            for (int y = 0; y < mapSize.height; y++){
                MapTileType type = MapTileType.GRASS;
                if (y <= 5 || x <= 5 || y + 5 >= mapSize.height || x + 5 >= mapSize.width){
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
        return bg;
    }
}
