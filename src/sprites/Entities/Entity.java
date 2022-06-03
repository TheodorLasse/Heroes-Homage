package src.sprites.Entities;

import src.data.GameComponent;
import src.data.GameMap;
import src.data.Rotation;
import src.data.Vector2D;
import src.data.time.DeltaTime;
import src.sprites.Sprite;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Entity implements Sprite {
    protected Vector2D position;
    protected Vector2D relativePosition;
    protected Vector2D size;
    protected Rotation rotation;
    protected BufferedImage texture;

    protected Entity(final Vector2D position, final Vector2D size, final double rotation, final BufferedImage texture){
        this.position = position;
        this.relativePosition = position;
        this.size = size;
        this.rotation = new Rotation(rotation);
        this.texture = texture;
    }
    /**
     * Updates the entity.
     */
    public void update(DeltaTime deltaTime, Vector2D mapFocus) {
        relativePosition = Vector2D.getDifference(position, mapFocus);
    }

    @Override public Vector2D getPosition() {
        return position.copy();
    }

    @Override public Vector2D getSize() {
        return size.copy();
    }

    @Override public double getRotation() {
        return rotation.getRadians();
    }

    protected BufferedImage getTexture() {
        return texture;
    }

    protected void setSize(Vector2D size){
        this.size = size;
    }

    protected void setTexture(BufferedImage image){
        this.texture = image;
    }

    protected void setRotation(Rotation rotation){
        this.rotation = rotation;
    }

    @Override public void draw(final Graphics g, final GameComponent gc) {
        g.drawImage(getTexture(), (int) relativePosition.getX() * GameMap.TILE_SIZE, (int) relativePosition.getY() * GameMap.TILE_SIZE, gc);
    }
}
