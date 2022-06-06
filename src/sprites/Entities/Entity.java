package src.sprites.Entities;

import src.map.GameMap;
import src.map.MapFocus;
import src.tools.Rotation;
import src.tools.Vector2D;
import src.tools.aStar.Mover;
import src.tools.time.DeltaTime;
import src.sprites.Sprite;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Entity implements Sprite, Mover {
    protected Vector2D position;
    protected Vector2D relativePosition;
    protected Vector2D size;
    protected Rotation rotation;
    protected BufferedImage texture;
    protected EntityType entityType;

    protected Entity(final Vector2D position, final Vector2D size, final double rotation, final BufferedImage texture){
        this.position = position;
        this.relativePosition = position;
        this.size = size;
        this.rotation = new Rotation(rotation);
        this.texture = texture;
        setEntityType(EntityType.NONE);
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

    protected EntityType getEntityType(){
        return entityType;
    }

    public boolean isOverlap(Vector2D mapPos){
        double x = mapPos.getX();
        double y = mapPos.getY();
        boolean isWithinX = position.getX() <= x && x <= position.getX() + size.getX();
        boolean isWithinY = position.getY() <= y && y <= position.getY() + size.getY();
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

    protected void setEntityType(EntityType entityType){
        this.entityType = entityType;
    }

    @Override public void draw(final Graphics g, final JComponent gc) {
        g.drawImage(getTexture(), (int) relativePosition.getX() * GameMap.TILE_SIZE, (int) relativePosition.getY() * GameMap.TILE_SIZE, gc);
    }

    public void onMouseClick3(GameMap map, Vector2D mouseMapPos){

    }
}
