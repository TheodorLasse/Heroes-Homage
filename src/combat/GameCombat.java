package src.combat;

import src.Army;
import src.Game;
import src.sprites.Sprite;
import src.sprites.SpriteHandler;
import src.sprites.SpriteLayer;
import src.tools.time.DeltaTime;

import java.awt.*;

public class GameCombat {
    public static final Dimension ARENA_SIZE = new Dimension(15, 10);
    private final Game game;
    private Army attacker;
    private Army defender;
    private final SpriteHandler combatSpriteHandler;

    public GameCombat(Game game){
        this.game = game;
        this.combatSpriteHandler = new SpriteHandler();
        CombatSpriteFactory factory = new CombatSpriteFactory(game.getCombatScreenDimension());
        combatSpriteHandler.setBackground(factory.getCombatBackground());
    }

    public Iterable<Sprite> getIterator(){
        return combatSpriteHandler.getLayerIterator(SpriteLayer.FIRST);
    }

    public void update(DeltaTime deltaTime) {
    }

    public void setUpBattlefield(Army attacker,Army defender){
        this.attacker = attacker;
        this.defender = defender;

    }

    public void cleanUpBattlefield(){
        attacker = null;
        defender = null;
        game.finishCombat();
    }

    public boolean isBattle(){
        return attacker == null;
    }
}
