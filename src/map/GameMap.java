package src.map;

import src.Game;
import src.player.PlayerTeam;
import src.player.PlayerTeamColor;
import src.player.Resource;
import src.sprites.entities.*;
import src.sprites.entities.livingEntities.Character;
import src.sprites.entities.livingEntities.MapLivingEntity;
import src.sprites.entities.MapEntity;
import src.sprites.SpriteTexture;
import src.tools.*;
import src.tools.aStar.AStarPathFinder;
import src.tools.aStar.Path;
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
    private final List<List<MapTileType>> mapTiles;
    private final SpriteHandler mapSpriteHandler;
    private final EntityHandler mapEntityHandler;
    private final BufferedImage background;
    private List<SpriteTexture> pathSprites;
    private final PathFinder finder;
    private final Dimension mapSize = new Dimension(100,100);
    private final Dimension screenSize;
    private final WindowFocus windowFocus;
    private final MapTurn mapTurn;
    private MapLivingEntity entityFocus;
    private final int teamCount = 2;

    /**
     * Object that contains and controls the map
     * @param screenSize Size of the screen allocated for GameMap
     */
    public GameMap(Game game, Dimension screenSize, ArrayList<PlayerTeam> playerTeamList)
    {
        this.screenSize = screenSize;
        finder = new AStarPathFinder(new PathMap(mapSize, null), 500, true);
        mapSpriteHandler = new SpriteHandler();
        mapEntityHandler = new EntityHandler();
        windowFocus = new WindowFocus(new Vector2D(), screenSize, mapSize, TILE_SIZE);
        mapTiles = new ArrayList<>();
        initPlayerTeams(playerTeamList);
        mapTurn = new MapTurn(playerTeamList);

        MapSpriteFactory factory = new MapSpriteFactory(screenSize);
        background = factory.createBackground(mapSize, mapTiles);
        for (SpriteTexture borderTexture:factory.createBorders()) {
            mapSpriteHandler.add(borderTexture, SpriteLayer.LAST);
        }
        pathSprites = new ArrayList<>();

        mapEntityHandler.add(new MapEntity(new Vector2D(10,12), Game.imageLoader.getImage(ImageLoader.ImageName.ROCK)));
        mapEntityHandler.add(new CollectableMapEntity(new Vector2D(15, 12), Resource.GOLD, mapEntityHandler));
        mapEntityHandler.add(new CollectableMapEntity(new Vector2D(18, 12), Resource.WOOD, mapEntityHandler));
        mapEntityHandler.add(new CollectableMapEntity(new Vector2D(21, 12), Resource.ORE, mapEntityHandler));
        mapEntityHandler.add(new MapLivingEntity(new Vector2D(10,18), Character.CharacterEnum.NECROMANCER_LIGHT, game, playerTeamList.get(0), mapEntityHandler));
        mapEntityHandler.add(new MapLivingEntity(new Vector2D(14,18), Character.CharacterEnum.ORC, game, playerTeamList.get(1), mapEntityHandler));
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

    public PlayerTeam getCurrentPlayer(){
        return mapTurn.getCurrentPlayer();
    }

    /**
     *
     * @return Iterable of all the Entity objects located in the GameMap
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
     * Calculates and returns all blocked positions of the map, 1 = blocked, 0 = free
     */
    private int[][] getBlocked(){
        int[][] blocked = new int[mapSize.width][mapSize.height];
        for (int x = 0; x < mapTiles.size(); x++){
            for (int y = 0; y < mapTiles.get(x).size(); y++){
                if (mapTiles.get(x).get(y) == MapTileType.WATER) {
                    blocked[x][y] = 1;
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
        updatePathSprites(entityFocus);

        mapSpriteHandler.update(deltaTime);
        mapEntityHandler.update(deltaTime, windowFocus);
    }

    /**
     * updates the SpriteTextures which denote where a MapLivingEntity's path lies.
     * @param entity the Entity whose path is drawn.
     */
    private void updatePathSprites(MapLivingEntity entity) {
        ArrayList<SpriteTexture> newPathSprites = new ArrayList<>();
        if (entity != null && entity.getQueuedPath() != null) {
            Path path = entity.getQueuedPath();
            final int tileSize = windowFocus.getTileSize();
            for (int i = 0; i < path.getLength(); i++) {
                Path.Step step = path.getStep(i);

                BufferedImage stepImage = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
                Graphics g = stepImage.getGraphics();
                g.setColor(Color.ORANGE);
                g.fillRect(0, 0, tileSize, tileSize);
                SpriteTexture stepSprite = new SpriteTexture(new Vector2D(step.getX() * tileSize, step.getY() * tileSize), 0, stepImage);
                newPathSprites.add(stepSprite);
            }
        }
        for (SpriteTexture spriteTexture : pathSprites) {
            mapSpriteHandler.remove(spriteTexture);
        }
        for (SpriteTexture spriteTexture : newPathSprites) {
            mapSpriteHandler.add(spriteTexture, SpriteLayer.LAST);
        }
        pathSprites = newPathSprites;
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
                    entityFocus = (MapLivingEntity)mapEntity;
                    return;
                }
            }
        }
        else if (mouseButton == 3 && entityFocus != null && entityFocus.getPlayerTeam() == mapTurn.getCurrentPlayer()){
            PathMap map = new PathMap(mapSize, getBlocked());
            entityFocus.onMouseClick3(map, finder, mouseAbsolutePos);
        }
    }

    @Override
    public void onKeyEvent(KeyEvent e, AbstractMap<Key, KeyState> keyStates) {
        double mapShiftStep = 1;
        if (keyStates.get(e.getKey()) == KeyState.RELEASED) return; //Only register key event when pressed, not released
        switch (e.getKey()) {
            case LEFT -> windowFocus.addX(-mapShiftStep);
            case RIGHT -> windowFocus.addX(mapShiftStep);
            case UP -> windowFocus.addY(-mapShiftStep);
            case DOWN -> windowFocus.addY(mapShiftStep);
            case ESC -> {
                if (entityFocus != null) entityFocus = null;
                else System.exit(-1);
            }
            case E -> {
                mapTurn.nextPlayersTurn();
                entityFocus = null;
            }
        }
    }
}
