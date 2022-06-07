package src.sprites.Entities;

import src.map.GameMap;
import src.tools.WindowFocus;
import src.tools.Rotation;
import src.tools.Vector2D;
import src.tools.aStar.Mover;
import src.tools.aStar.PathFinder;
import src.tools.aStar.PathMap;
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

    public void update(DeltaTime deltaTime) {}

    /**
     * Updates the entity. If there is a WindowFocus object, use it to get relative position.
     */
    public void update(DeltaTime deltaTime, WindowFocus focus) {
        Vector2D relativeMapPosition = Vector2D.getDifference(position, focus.getPosition());
        int tileSize = focus.getTileSize();
        relativePosition = new Vector2D(
                relativeMapPosition.getX() * tileSize, relativeMapPosition.getY() * tileSize);
    }

    @Override public void draw(final Graphics g, final JComponent gc) {
        g.drawImage(getTexture(), (int) relativePosition.getX(),
                (int) relativePosition.getY(), gc);
    }

    public boolean isOverlap(Vector2D mapPos){
        double x = mapPos.getX();
        double y = mapPos.getY();
        boolean isWithinX = position.getX() <= x && x <= position.getX() + size.getX();
        boolean isWithinY = position.getY() <= y && y <= position.getY() + size.getY();
        return isWithinX && isWithinY;
    }

    public void onMouseClick3(PathMap map, PathFinder finder, Vector2D mouseMapPos) {}

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

    public EntityType getEntityType(){
        return entityType;
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

    public void setPosition(Vector2D position) {
        this.position = position;
    }
}
