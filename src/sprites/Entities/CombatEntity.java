package src.sprites.Entities;

import src.map.GameMap;
import src.tools.Vector2D;
import src.tools.WindowFocus;
import src.tools.time.DeltaTime;

import java.awt.image.BufferedImage;

public class CombatEntity extends Entity {
    public CombatEntity(Vector2D position, Vector2D size, BufferedImage texture) {
        super(position, size, 0, texture);
    }

    /**
     * Updates the entity. If there is a WindowFocus object, use it to get relative position.
     */
    public void update(DeltaTime deltaTime, WindowFocus focus) {
        Vector2D relativeMapPosition = Vector2D.getSum(focus.getPosition(), position);
        int tileSize = focus.getTileSize();
        relativePosition = new Vector2D(
                relativeMapPosition.getX() * tileSize, relativeMapPosition.getY() * tileSize);
    }
}
