package src.sprites.Entities;

import src.tools.Vector2D;

import java.awt.image.BufferedImage;

public class MapEntity extends Entity{


    public MapEntity(final Vector2D position, final Vector2D size, final double rotation, final BufferedImage texture){
        super(position, size, rotation, texture);

    }
    public MapEntity(final Vector2D position, final BufferedImage texture){
        super(position, new Vector2D(1,1), 0, texture);

    }
}
