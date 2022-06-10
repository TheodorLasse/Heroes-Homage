package src.tools.aStar;

import src.tools.Vector2D;

import java.awt.*;

/**
 * CREDIT TO KEVIN GLASS FOR A* ALGORITHM
 */
public class PathMap implements TileBasedMap{
    private Dimension mapSize;
    private int[][] terrain;
    private boolean[][] visited;

    public PathMap(Dimension mapSize, int[][] terrain){
        this.mapSize = mapSize;
        this.terrain = terrain;
        this.visited = new boolean[mapSize.width][mapSize.height];
    }

    @Override
    public int getWidthInTiles() {
        return mapSize.width;
    }

    @Override
    public int getHeightInTiles() {
        return mapSize.height;
    }

    @Override
    public void pathFinderVisited(int x, int y) {
        this.visited[x][y] = true;
    }

    @Override
    public boolean blocked(Mover var1, int x, int y) {
        Vector2D size = var1.getSize();
        for (int sizeX = 0; sizeX < size.getX(); sizeX++) {
            for (int sizeY = 0; sizeY < size.getY(); sizeY++) {
                if (this.terrain[x + sizeX][y + sizeY] == 1) return true;
            }
        }
        return false;
    }

    @Override
    public float getCost(Mover var1, int var2, int var3, int var4, int var5) {
        return 1.0F;
    }
}
