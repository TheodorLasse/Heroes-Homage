package src.map;

import src.Game;
import src.player.PlayerTeam;
import src.player.PlayerTeamColor;
import src.sprites.Entities.*;
import src.sprites.Entities.LivingEntities.LivingEntity;
import src.sprites.Entities.LivingEntities.MapLivingEntity;
import src.sprites.Entities.MapEntity;
import src.sprites.SpriteTexture;
import src.tools.*;
import src.tools.aStar.AStarPathFinder;
import src.tools.aStar.PathFinder;
import src.tools.aStar.PathMap;
import src.tools.image.ImageLoader;
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



public class GameMap implements GameKeyListener {
    public static final int TILE_SIZE = 20;
    private final Game game;
    private final List<List<MapTileType>> mapTiles;
    private final SpriteHandler mapSpriteHandler;
    private final EntityHandler mapEntityHandler;
    private final BufferedImage background;
    private final PathFinder finder;

    private final Dimension mapSize = new Dimension(100,100);
    private final Dimension screenSize;

    private final WindowFocus windowFocus;
    private LivingEntity entityFocus;

    private final int teamCount = 2;

    /**
     * Object that contains and controls the map
     * @param screenSize Size of the screen allocated for GameMap
     */
    public GameMap(Game game, Dimension screenSize, ArrayList<PlayerTeam> playerTeamList)
    {
        this.game = game;
        this.screenSize = screenSize;
        finder = new AStarPathFinder(new PathMap(mapSize, null), 500, true);
        mapSpriteHandler = new SpriteHandler();
        mapEntityHandler = new EntityHandler();
        windowFocus = new WindowFocus(new Vector2D(), screenSize, mapSize, TILE_SIZE);
        mapTiles = new ArrayList<>();
        initPlayerTeams(playerTeamList);

        MapSpriteFactory factory = new MapSpriteFactory(screenSize);
        background = factory.createBackground(mapSize, mapTiles);
        for (SpriteTexture borderTexture:factory.createBorders()) {
            mapSpriteHandler.add(borderTexture, SpriteLayer.LAST);
        }

        mapEntityHandler.add(new MapEntity(new Vector2D(10,12), Game.imageLoader.getImage(ImageLoader.ImageName.ROCK), playerTeamList.get(0)));
        mapEntityHandler.add(new MapLivingEntity(new Vector2D(10,18), ImageLoader.Character.NINJA_1, game, playerTeamList.get(0), mapEntityHandler));
        mapEntityHandler.add(new MapLivingEntity(new Vector2D(14,18), ImageLoader.Character.NINJA_1, game, playerTeamList.get(1), mapEntityHandler));
    }

    /**
     * Sets the centre of the MapFocus object to position, i.e "the middle" of the screen is set to position
     * @param position position which MapFocus centres on
     */
    public void setMapFocusCentre(Vector2D position) {
        windowFocus.setCentre(position);
    }

    /**
     * Initializes the game object's list of players since the number would depend on the map
     * @param playerTeamList list to be updated with players
     */
    private void initPlayerTeams(ArrayList<PlayerTeam> playerTeamList){
        for (PlayerTeamColor color: PlayerTeamColor.values()){
            playerTeamList.add(new PlayerTeam(color));
            if (playerTeamList.size() == teamCount) break;
        }
    }

    public Dimension getMapSize() {
        return mapSize;
    }

    public BufferedImage getBackground(){
        return background;
    }

    /**
     *
     * @return Iterable of all of the Entity objects located in the GameMap
     */
    public ArrayList<Sprite> getIterator(){
        mapSpriteHandler.setBackground(background.getSubimage(
                (int) (windowFocus.getX() * TILE_SIZE), (int) (windowFocus.getY() * TILE_SIZE),
                (int) (screenSize.getWidth()), ((int) screenSize.getHeight())));

        ArrayList<Sprite> tempList = new ArrayList<>(mapSpriteHandler.getLayerIterator(SpriteLayer.FIRST));
        tempList.addAll(mapEntityHandler.getIterator());
        tempList.addAll(mapSpriteHandler.getLayerIterator(SpriteLayer.LAST));

        return tempList;
    }

    /**
     * Chart a path to a mapPos for an entity
     * @param entity entity from which path is started
     * @param mapPos position to which path goes
     * @return Array of Steps to reach mapPos
     */
    /*public Path getPath(Entity entity, Vector2D mapPos){
        Vector2D entityPos = entity.getPosition();
        finder.setMap(mapSize, getBlocked());
        return finder.findPath(entity, (int)entityPos.getX(), (int)entityPos.getY(), (int)mapPos.getX(), (int)mapPos.getY());
    }*/

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

    /**
     * Converts the position relative to mapFocus to the absolute position of the map
     * @param relativePos Position on the mapFocus
     * @return relativePos's position on the map in absolute terms
     */
    public Vector2D relativeToAbsolutePos(Vector2D relativePos){
        return Vector2D.getSum(relativePos, windowFocus.getPosition());
    }

    /**
     * Update GameMap
     * @param deltaTime how long since last update
     */
    public void update(DeltaTime deltaTime){
        mapSpriteHandler.update(deltaTime);
        mapEntityHandler.update(deltaTime, windowFocus);
    }

    /**
     * When GameMap is clicked on by mouse
     * @param mousePos position of mouse on JFrame
     * @param mouseButton button that was pressed
     */
    public void onMouseClick(Vector2D mousePos, int mouseButton){
        Vector2D mouseMapFocus = new Vector2D(mousePos.getX() / TILE_SIZE, mousePos.getY() / TILE_SIZE);
        Vector2D mouseAbsolutePos = relativeToAbsolutePos(mouseMapFocus);
        if(mouseButton == 1){
            for (Entity mapEntity : mapEntityHandler.getIterator()) {
                if (mapEntity.isOverlap(mouseAbsolutePos) && mapEntity.getEntityType() == EntityType.LIVING){
                    entityFocus = (LivingEntity)mapEntity;
                    return;
                }
            }
        }
        else if (mouseButton == 3 && entityFocus != null){
            PathMap map = new PathMap(mapSize, getBlocked());
            entityFocus.onMouseClick3(map, finder, mouseAbsolutePos);
        }
    }

    @Override
    public void onKeyEvent(KeyEvent e, AbstractMap<Key, KeyState> keyStates) {
        double mapShiftStep = 1;
        switch (e.getKey()) {
            case LEFT -> windowFocus.addX(-mapShiftStep);
            case RIGHT -> windowFocus.addX(mapShiftStep);
            case UP -> windowFocus.addY(-mapShiftStep);
            case DOWN -> windowFocus.addY(mapShiftStep);
            case ESC -> {
                if (entityFocus != null) entityFocus = null;
                System.exit(-1);
                //TODO keys presses register twice
            }
        }
    }
}
