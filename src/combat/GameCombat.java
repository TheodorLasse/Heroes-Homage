package src.combat;

import src.Army;
import src.Game;
import src.sprites.Entities.CombatEntity;
import src.sprites.Entities.Entity;
import src.sprites.Entities.EntityHandler;
import src.sprites.Sprite;
import src.sprites.SpriteHandler;
import src.sprites.SpriteLayer;
import src.tools.Vector2D;
import src.tools.time.DeltaTime;

import java.awt.*;
import java.util.ArrayList;

public class GameCombat {
    public static final Dimension ARENA_SIZE = new Dimension(15, 10);
    private final Game game;
    private Army attacker, defender;
    private final SpriteHandler combatSpriteHandler;
    private final EntityHandler combatEntityHandler;

    public GameCombat(Game game){
        this.game = game;
        this.combatSpriteHandler = new SpriteHandler();
        this.combatEntityHandler = new EntityHandler();
        CombatSpriteFactory factory = new CombatSpriteFactory(game.getCombatScreenDimension());
        combatSpriteHandler.setBackground(factory.getCombatBackground());
    }

    public Iterable<Sprite> getIterator(){
        ArrayList<Sprite> iterable = combatSpriteHandler.getLayerIterator(SpriteLayer.FIRST);
        iterable.addAll(combatEntityHandler.getIterator());
        return iterable;
    }

    public void update(DeltaTime deltaTime) {
        for (Entity entity : combatEntityHandler.getIterator()) {
            entity.update(deltaTime);
        }
        combatEntityHandler.update(deltaTime);
    }

    public void setUpBattlefield(Army attacker, Army defender){
        this.attacker = attacker;
        this.defender = defender;

        for (CombatEntity entity : attacker.getCombatEntities()){
            combatEntityHandler.add(entity);
        }
        for (CombatEntity entity : defender.getCombatEntities()){
            combatEntityHandler.add(entity);
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
}
