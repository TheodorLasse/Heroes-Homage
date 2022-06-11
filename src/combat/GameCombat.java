package src.combat;

import src.Army;
import src.Game;
import src.sprites.entities.*;
import src.sprites.entities.livingEntities.CombatLivingEntity;
import src.sprites.entities.livingEntities.LivingEntity;
import src.sprites.Sprite;
import src.sprites.SpriteHandler;
import src.sprites.SpriteLayer;
import src.tools.Vector2D;
import src.tools.WindowFocus;
import src.tools.aStar.AStarPathFinder;
import src.tools.aStar.PathFinder;
import src.tools.aStar.PathMap;
import src.tools.time.DeltaTime;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameCombat {
    public static final Dimension ARENA_SIZE = new Dimension(18, 10);
    private final Game game;
    private Army attacker, defender;
    private final SpriteHandler combatSpriteHandler;
    private final EntityHandler combatEntityHandler;
    private final WindowFocus focus;
    private final PathFinder finder;
    private final List<Vector2D> startingPositions;
    private CombatTurn combatTurn;

    public GameCombat(Game game){
        this.game = game;
        this.combatSpriteHandler = new SpriteHandler();
        this.combatEntityHandler = new EntityHandler();
        finder = new AStarPathFinder(new PathMap(ARENA_SIZE, null), 50, true);
        CombatSpriteFactory factory = new CombatSpriteFactory(game.getCombatScreenDimension());

        int gridTile = factory.getGridSquareLength();
        Vector2D gridPos = factory.getGridOffset(); // measured in pixels
        Vector2D focusPos = new Vector2D(gridPos.getX() / gridTile, gridPos.getY() / gridTile); // measured in tiles
        Dimension screenSize = game.getCombatScreenDimension();
        this.focus = new WindowFocus(focusPos, screenSize, ARENA_SIZE, gridTile);

        combatSpriteHandler.setBackground(factory.getCombatBackground());


        startingPositions = Arrays.asList(new Vector2D(),
                new Vector2D(0, 1), new Vector2D(0, 2), new Vector2D(0, 3),
                new Vector2D(0, 4), new Vector2D(0, 5), new Vector2D(0, 6));
    }

    public Iterable<Sprite> getIterator(){
        ArrayList<Sprite> iterable = combatSpriteHandler.getLayerIterator(SpriteLayer.FIRST);
        iterable.addAll(combatEntityHandler.getIterator());
        return iterable;
    }

    public void update(DeltaTime deltaTime) {
        for (Entity entity : combatEntityHandler.getIterator()) {
            entity.update(deltaTime, focus);
        }
        combatEntityHandler.update(deltaTime, focus);
    }

    public void setUpBattlefield(Army attacker, Army defender){
        this.attacker = attacker;
        this.defender = defender;
        ArrayList<CombatLivingEntity> entityList = new ArrayList<>();

        int i = 0;
        for (CombatLivingEntity entity : attacker.getCombatEntities()){
            entity.setCombatEntityHandler(combatEntityHandler);
            combatEntityHandler.add(entity);
            entityList.add(entity);
            entity.setPosition(startingPositions.get(i).copy());
            i++;
        }

        i=0;
        for (CombatLivingEntity entity : defender.getCombatEntities()){
            entity.setCombatEntityHandler(combatEntityHandler);
            combatEntityHandler.add(entity);
            entityList.add(entity);
            entity.setFacingRight(false);

            Vector2D defenderStart = startingPositions.get(i).copy();
            defenderStart.addX(ARENA_SIZE.getWidth() - 1);
            entity.setPosition(defenderStart);
            i++;
        }
        combatTurn = new CombatTurn(entityList);
    }

    public void cleanUpBattlefield(){
        attacker = null;
        defender = null;
        game.finishCombat();
    }

    public boolean isBattle(){
        return attacker != null;
    }

    public void onMouseClick(Vector2D mousePos, int mouseButton) {
        Vector2D mouseMapFocus = new Vector2D(mousePos.getX() / focus.getTileSize(), mousePos.getY() / focus.getTileSize());
        Vector2D mouseAbsolutePos = relativeToAbsolutePos(mouseMapFocus);
        if (mouseButton == 3){
            PathMap map = new PathMap(ARENA_SIZE, getBlocked());
            combatTurn.getCurrentEntityTurn().onMouseClick3(map, finder, mouseAbsolutePos);
            combatTurn.endEntityTurn();
        }
    }

    private int[][] getBlocked() {
        int[][] blocked = new int[ARENA_SIZE.width][ARENA_SIZE.height];

        for (Entity mapEntity : combatEntityHandler.getIterator()) {
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
        return Vector2D.getDifference(relativePos, focus.getPosition());
    }
}
