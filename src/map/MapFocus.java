package src.map;

import src.tools.Vector2D;

import java.awt.*;

public class MapFocus {
    private Vector2D position;
    private final Dimension screenSize;
    private final Dimension mapSize;
    private final int tileSize;

    /**
     * Controls which part of the map is visible on screen
     * @param position top left corner of the mapFocus, measured in tile coords
     * @param screenSize size of the screen which mapFocus is placed in, measured in pixels
     * @param mapSize size of the map which mapFocus is placed in, measured in tile coords
     */
    public MapFocus(Vector2D position, Dimension screenSize, Dimension mapSize, int tileSize){
        this.position = position;
        this.screenSize = screenSize;
        this.mapSize = mapSize;
        this.tileSize = tileSize;
    }

    /**
     * Adds X to this object's position
     * @param x size of step
     */
    public void addX(double x) {
        position.addX(x);
        setWithinBoundsX();
    }

    /**
     * Adds Y to this object's position
     * @param y size of step
     */
    public void addY(double y){
        position.addY(y);
        setWithinBoundsY();
    }

    /**
     * Sets this objects position to given position, also sanity checks new position
     * @param position new position of this object
     */
    public void setPosition(Vector2D position){
        this.position = new Vector2D((int)position.getX(), (int)position.getY());
        setWithinBoundsX();
        setWithinBoundsY();
    }

    public void setCentre(Vector2D centrePosition){
        Vector2D centreOffset = new Vector2D((double)screenSize.width / tileSize * 0.5, (double)screenSize.height / tileSize * 0.5);
        Vector2D topLeftPosition = Vector2D.getDifference(centrePosition, centreOffset);
        setPosition(topLeftPosition);
    }

    /**
     * If this object's X-position sets it outside of bounds, set it to the bound
     */
    private void setWithinBoundsX(){
        if (position.getX() > mapSize.getWidth() - screenSize.getWidth() / tileSize)
            position.setX((int)(mapSize.getWidth() - screenSize.getWidth() / tileSize));
        else if (position.getX() < 0)
            position.setX(0);
    }

    /**
     * If this object's Y-position sets it outside of bounds, set it to the bound
     */
    private void setWithinBoundsY(){
        if (position.getY() > mapSize.getHeight() - screenSize.getHeight() / tileSize)
            position.setY((int)(mapSize.getHeight() - screenSize.getHeight() / tileSize));
        else if (position.getY() < 0)
            position.setY(0);
    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public Vector2D getPosition(){
        return position.copy();
    }
}
