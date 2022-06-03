package src.tools;

import java.awt.*;

import static src.GameMap.TILE_SIZE;

public class MapFocus {
    Vector2D position;
    Dimension screenSize;
    Dimension mapSize;

    public MapFocus(Vector2D position, Dimension screenSize, Dimension mapSize){
        this.position = position;
        this.screenSize = screenSize;
        this.mapSize = mapSize;
    }

    public void addX(double x) {
        position.addX(x);
        if (position.getX() > mapSize.getWidth() * TILE_SIZE - screenSize.getWidth())
            position.setX(mapSize.getHeight() * TILE_SIZE - screenSize.getWidth());
        else if (position.getX() < 0)
            position.setX(0);
    }

    public void addY(double y){
        position.addY(y);
        if (position.getY() > mapSize.getHeight() * TILE_SIZE - screenSize.getHeight())
            position.setY(mapSize.getHeight() * TILE_SIZE - screenSize.getHeight());
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
