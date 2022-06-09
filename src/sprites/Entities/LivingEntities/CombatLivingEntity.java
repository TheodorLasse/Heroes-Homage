package src.sprites.Entities.LivingEntities;

import src.player.PlayerTeam;
import src.sprites.Entities.EntityHandler;
import src.tools.Vector2D;
import src.tools.image.ImageLoader;

public class CombatLivingEntity extends LivingEntity {
    public CombatLivingEntity(Vector2D position, ImageLoader.Character character, PlayerTeam team) {
        super(position, character, team, null);
        timeBetweenMoves = 0.5;
    }
    public CombatLivingEntity(Vector2D position, Vector2D size, ImageLoader.Character character, PlayerTeam team) {
        super(position, character, team, null);
        setSize(size);
        timeBetweenMoves = 0.5;
    }

    @Override
    protected Vector2D getRelativeMapPosition(Vector2D pos, Vector2D focusPos){
        return Vector2D.getSum(focusPos, position);
    }

    public void setCombatEntityHandler(EntityHandler combatEntityHandler){
        this.entityHandler = combatEntityHandler;
    }

    public void setSize(Vector2D size){
        this.size = size;
    }
}
