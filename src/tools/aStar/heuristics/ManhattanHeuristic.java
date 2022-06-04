package src.tools.aStar.heuristics;

import src.tools.aStar.AStarHeuristic;
import src.tools.aStar.Mover;
import src.tools.aStar.TileBasedMap;

public class ManhattanHeuristic implements AStarHeuristic {
    private int minimumCost;

    public ManhattanHeuristic(int minimumCost) {
        this.minimumCost = minimumCost;
    }

    public float getCost(TileBasedMap map, Mover mover, int x, int y, int tx, int ty) {
        return (float)(this.minimumCost * (Math.abs(x - tx) + Math.abs(y - ty)));
    }
}