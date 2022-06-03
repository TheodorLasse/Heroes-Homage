package src.sprites.Entities;

import src.data.GameComponent;
import src.data.Rotation;
import src.data.Vector2D;
import src.sprites.Sprite;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Entity implements Sprite {
    protected Vector2D position;
    protected Vector2D size;
    protected Rotation rotation;
    protected BufferedImage texture;

    protected Entity(final Vector2D position, final Vector2D size, final double rotation, final BufferedImage texture){
        this.position = position;
        this.size = size;
        this.rotation = new Rotation(rotation);
        this.texture = texture;
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
        g.drawImage(getTexture(), (int) position.getX(), (int) position.getY(), gc);
    }
}
