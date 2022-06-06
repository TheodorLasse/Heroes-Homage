package src.combat;

import src.Army;
import src.Game;
import src.tools.time.DeltaTime;

public class GameCombat {
    private final Game game;
    private Army attacker;
    private Army defender;

    public GameCombat(Game game){
        this.game = game;
    }

    public void update(DeltaTime deltaTime) {
    }

    public void setUpBattlefield(Army attacker,Army defender){

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
