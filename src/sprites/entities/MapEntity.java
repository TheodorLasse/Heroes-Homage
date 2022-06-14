package src.sprites.entities;

import src.player.PlayerTeam;
import src.tools.Vector2D;

import java.awt.image.BufferedImage;

public class MapEntity extends Entity {
    public MapEntity(final Vector2D position, final BufferedImage texture){
        super(position, new Vector2D(1,1), 0, texture);
        setEntityType(EntityType.OBSTACLE);
    }
}