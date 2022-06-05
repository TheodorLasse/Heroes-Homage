package src.tools;

import java.awt.*;

import static src.GameMap.TILE_SIZE;

public class MapFocus {
    Vector2D position;
    Dimension screenSize;
    Dimension mapSize;

    /**
     * Controls which part of the map is visible on screen
     * @param position top left corner of the mapFocus, measured in tile coords
     * @param screenSize size of the screen which mapFocus is placed in, measured in pixels
     * @param mapSize size of the map which mapFocus is placed in, measured in tile coords
     */
    public MapFocus(Vector2D position, Dimension screenSize, Dimension mapSize){
        this.position = position;
        this.screenSize = screenSize;
        this.mapSize = mapSize;
    }

    public void addX(double x) {
        position.addX(x);
        if (position.getX() > mapSize.getWidth() - screenSize.getWidth() / TILE_SIZE)
            position.setX((int)(mapSize.getWidth() - screenSize.getWidth() / TILE_SIZE));
        else if (position.getX() < 0)
            position.setX(0);
    }

    public void addY(double y){
        position.addY(y);
        if (position.getY() > mapSize.getHeight() - screenSize.getHeight() / TILE_SIZE)
            position.setY((int)(mapSize.getHeight() - screenSize.getHeight() / TILE_SIZE));
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
