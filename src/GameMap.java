package src;

import src.sprites.Entities.*;
import src.tools.ImageLoader;
import src.tools.MapFocus;
import src.tools.Vector2D;
import src.tools.aStar.AStarPathFinder;
import src.tools.aStar.Path;
import src.tools.aStar.PathFinder;
import src.tools.aStar.PathMap;
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

enum MapTileType { WATER, GRASS, ROAD, PAVEMENT, ERROR}

public class GameMap implements GameKeyListener {
    public static final int TILE_SIZE = 20;
    private final List<List<MapTileType>> mapTiles;
    private final SpriteHandler mapSpriteHandler;
    private final EntityHandler mapEntityHandler;
    private final BufferedImage background;

    private final Dimension mapSize = new Dimension(100,100);
    private final Dimension screenSize;

    private final PathFinder finder;

    private final MapFocus mapFocus;
    private Entity entityFocus;

    /**
     * Object that contains and controls the map
     * @param screenSize Size of the screen allocated for GameMap
     */
    public GameMap(Dimension screenSize)
    {
        this.screenSize = screenSize;
        background = new BufferedImage(mapSize.width * TILE_SIZE, mapSize.height * TILE_SIZE, TYPE_INT_ARGB);
        finder = new AStarPathFinder(new PathMap(mapSize, null), 500, true);
        mapSpriteHandler = new SpriteHandler();
        mapEntityHandler = new EntityHandler();
        mapFocus = new MapFocus(new Vector2D(), screenSize, mapSize);
        mapTiles = new ArrayList<>();
        initBackground();

        mapEntityHandler.add(new MapEntity(new Vector2D(10,10), Game.imageLoader.getImage(ImageLoader.ImageName.ROCK)));
        mapEntityHandler.add(new MapEntity(new Vector2D(11,11), Game.imageLoader.getImage(ImageLoader.ImageName.ROCK)));
        mapEntityHandler.add(new MapEntity(new Vector2D(10,12), Game.imageLoader.getImage(ImageLoader.ImageName.ROCK)));
        //mapEntityHandler.add(new MapEntity(new Vector2D(21,18), Game.imageLoader.getImage(ImageLoader.ImageName.ROCK)));
        mapEntityHandler.add(new MapLivingEntity(new Vector2D(30,18), Game.imageLoader.getImage(ImageLoader.ImageName.ROCK), TeamType.BLUE));
    }

    /**
     * Initialize the background image
     */
    private void initBackground(){
        Graphics g = background.getGraphics();
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
    }

    /**
     * Update GameMap
     * @param deltaTime how long since last update
     */
    public void update(DeltaTime deltaTime){
        mapSpriteHandler.update(deltaTime);
        mapEntityHandler.update(deltaTime, mapFocus);
    }

    /**
     *
     * @return Iterable of all of the Entity objects located in the GameMap
     */
    public ArrayList<Sprite> getIterator(){
        mapSpriteHandler.setBackground(background.getSubimage(
                (int) (mapFocus.getX() * TILE_SIZE), (int) (mapFocus.getY() * TILE_SIZE),
                (int) (screenSize.getWidth()), ((int) screenSize.getHeight())));

        ArrayList<Sprite> iterList = new ArrayList<>(mapSpriteHandler.getLayerIterator(SpriteLayer.FIRST));
        iterList.addAll(mapEntityHandler.getIterator());

        return iterList;
    }

    /**
     * When GameMap is clicked on by mouse
     * @param mousePos position of mouse on JFrame
     * @param mouseButton button that was pressed
     */
    public void onMouseClick(Vector2D mousePos, int mouseButton){
        Vector2D mouseMapPos = new Vector2D(mousePos.getX() / TILE_SIZE, mousePos.getY() / TILE_SIZE);
        Vector2D mouseMapFocus = Vector2D.getSum(mouseMapPos, mapFocus.getPosition());
        if(mouseButton == 1){
            for (Entity mapEntity : mapEntityHandler.getIterator()) {
                if (mapEntity.isOverlap(mouseMapFocus)){
                    entityFocus = mapEntity;
                    return;
                }
            }
        }
        else if (mouseButton == 3 && entityFocus != null){
            entityFocus.onMouseClick3(this, mouseMapPos);
        }
    }

    /**
     * Chart a path to a mapPos for an entity
     * @param entity entity from which path is started
     * @param mapPos position to which path goes
     * @return Array of Steps to reach mapPos
     */
    public Path getPath(Entity entity, Vector2D mapPos){
        Vector2D entityPos = entity.getPosition();
        finder.setMap(mapSize, getBlocked());
        return finder.findPath(entity, (int)entityPos.getX(), (int)entityPos.getY(), (int)mapPos.getX(), (int)mapPos.getY());
    }

    /**
     * Calculates and returns all blocked positions of the map, 1 = blocked, 0 = free
     */
    private int[][] getBlocked(){
        int[][] blocked = new int[mapSize.width][mapSize.height];
        for (int x = 0; x < mapTiles.size(); x++){
            for (int y = 0; y < mapTiles.get(x).size(); y++){
                switch (mapTiles.get(x).get(y)){
                    case WATER -> blocked[x][y] = 1;
                }
            }
        }

        for (Entity mapEntity : mapEntityHandler.getIterator()) {
            Vector2D entityPos = mapEntity.getPosition();
            for (int x = 0; x < mapEntity.getSize().getX(); x++) {
                for (int y = 0; y < mapEntity.getSize().getY(); y++) {
                    int iterX = (int)(entityPos.getX() + x);
                    int iterY = (int)(entityPos.getY() + y);
                    blocked[iterX][iterY] = 1;
                }
            }
        }

        return blocked;
    }

    public Vector2D relativeToAbsolutePos(Vector2D relativePos){
        return Vector2D.getSum(relativePos, mapFocus.getPosition());
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
