package src.tools.aStar;

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

    public void setTerrain(int[][] newTerrain){
        terrain = newTerrain;
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
        return this.terrain[x][y] == 1;
    }

    @Override
    public float getCost(Mover var1, int var2, int var3, int var4, int var5) {
        return 1.0F;
    }
}
