package src.tools.aStar;

/**
 * CREDIT TO KEVIN GLASS FOR A* ALGORITHM
 */
public interface AStarHeuristic {
    float getCost(TileBasedMap var1, Mover var2, int var3, int var4, int var5, int var6);
}