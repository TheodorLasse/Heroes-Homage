package src.sprites.entities;

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
    protected Vector2D drawPosition;

    protected Vector2D relativePosition;
    protected Vector2D size;
    protected Rotation rotation;
    protected BufferedImage texture;
    protected EntityType entityType;

    protected Entity(final Vector2D position, final Vector2D size, final double rotation, final BufferedImage texture){
        this.position = position;
        this.drawPosition = position;
        this.size = size;
        this.rotation = new Rotation(rotation);
        this.texture = texture;
        setEntityType(EntityType.NONE);
    }

    /**
     * Updates the entity
     */
    public void update(DeltaTime deltaTime, WindowFocus focus) {
        updateRelativePos(focus);
    }

    @Override public void draw(final Graphics g, final JComponent gc) {
        g.drawImage(getTexture(), (int) drawPosition.getX(),
                (int) drawPosition.getY(), gc);
    }

    protected Vector2D getRelativeMapPosition(Vector2D pos, Vector2D focusPos){
        return Vector2D.getDifference(position, focusPos);
    }

    protected void updateRelativePos(WindowFocus focus){
        Vector2D relativeMapPosition = getRelativeMapPosition(position, focus.getPosition());
        int tileSize = focus.getTileSize();
        drawPosition = new Vector2D(
                relativeMapPosition.getX() * tileSize, relativeMapPosition.getY() * tileSize);
        relativePosition = drawPosition.copy();
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
