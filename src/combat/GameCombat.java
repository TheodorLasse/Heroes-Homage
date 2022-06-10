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

public class GameCombat {
    public static final Dimension ARENA_SIZE = new Dimension(18, 10);
    private final Game game;
    private Army attacker, defender;
    private final SpriteHandler combatSpriteHandler;
    private final EntityHandler combatEntityHandler;
    private final WindowFocus focus;
    private LivingEntity entityFocus;
    private PathFinder finder;

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
        attacker.setCombatEntities(combatEntityHandler);
        defender.setCombatEntities(combatEntityHandler);

        int i = 0;
        for (CombatLivingEntity entity : attacker.getCombatEntities()){
            combatEntityHandler.add(entity);
            entity.setPosition(attacker.getStartingPositions().get(i));
            i++;
        }
        for (CombatLivingEntity entity : defender.getCombatEntities()){
            combatEntityHandler.add(entity);
            entity.setPosition(defender.getStartingPositions().get(i));
            i++;
        }
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
        if(mouseButton == 1){
            for (Entity entity : combatEntityHandler.getIterator()) {
                if (entity.isOverlap(mouseAbsolutePos) && entity.getEntityType() == EntityType.LIVING){
                    entityFocus = (LivingEntity)entity;
                    return;
                }
            }
        }
        else if (mouseButton == 3 && entityFocus != null){
            PathMap map = new PathMap(ARENA_SIZE, getBlocked());
            entityFocus.onMouseClick3(map, finder, mouseAbsolutePos);
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
