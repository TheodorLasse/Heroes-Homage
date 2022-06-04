package src.tools.aStar;

public interface TileBasedMap {
    int getWidthInTiles();

    int getHeightInTiles();

    void pathFinderVisited(int var1, int var2);

    boolean blocked(Mover var1, int var2, int var3);

    float getCost(Mover var1, int var2, int var3, int var4, int var5);
}