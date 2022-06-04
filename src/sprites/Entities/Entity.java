package src.sprites.Entities;

import src.Game;
import src.GameMap;
import src.tools.MapFocus;
import src.tools.Rotation;
import src.tools.Vector2D;
import src.tools.aStar.Mover;
import src.tools.aStar.Path;
import src.tools.time.DeltaTime;
import src.sprites.Sprite;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public abstract class Entity implements Sprite, Mover {
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
    public void update(DeltaTime deltaTime, MapFocus mapFocus) {
        relativePosition = Vector2D.getDifference(position, mapFocus.getPosition());
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

    public boolean isOverlap(Vector2D mapPos){
        double x = mapPos.getX();
        double y = mapPos.getY();
        boolean isWithinX = relativePosition.getX() <= x && x <= relativePosition.getX() + size.getX();
        boolean isWithinY = relativePosition.getY() <= y && y <= relativePosition.getY() + size.getY();
        return isWithinX && isWithinY;
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

    @Override public void draw(final Graphics g, final JComponent gc) {
        g.drawImage(getTexture(), (int) relativePosition.getX() * GameMap.TILE_SIZE, (int) relativePosition.getY() * GameMap.TILE_SIZE, gc);
    }

    public void onClick(){
    }

    public void onMouseClick3(GameMap map, Vector2D mouseMapPos){

    }
}
