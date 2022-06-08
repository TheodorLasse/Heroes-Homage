package src.sprites.Entities;

import src.player.PlayerTeam;
import src.tools.Vector2D;
import src.tools.WindowFocus;
import src.tools.image.ImageLoader;
import src.tools.time.DeltaTime;

import java.awt.image.BufferedImage;

public class CombatLivingEntity extends LivingEntity {
    public CombatLivingEntity(Vector2D position, ImageLoader.Character character, PlayerTeam team) {
        super(position, character, team, null);
    }
    public CombatLivingEntity(Vector2D position, Vector2D size, ImageLoader.Character character, PlayerTeam team) {
        super(position, character, team, null);
        setSize(size);
    }

    /**
     * Updates the entity. If there is a WindowFocus object, use it to get relative position.
     */
    public void update(DeltaTime deltaTime, WindowFocus focus) {
        Vector2D relativeMapPosition = Vector2D.getSum(focus.getPosition(), position);
        int tileSize = focus.getTileSize();
        relativePosition = new Vector2D(
                relativeMapPosition.getX() * tileSize, relativeMapPosition.getY() * tileSize);
        move(deltaTime);
    }

    public void setCombatEntityHandler(EntityHandler combatEntityHandler){
        this.entityHandler = combatEntityHandler;
    }

    public void setSize(Vector2D size){
        this.size = size;
    }
}
