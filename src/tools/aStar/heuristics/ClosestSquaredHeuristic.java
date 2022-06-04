package src.tools.aStar.heuristics;

import src.tools.aStar.AStarHeuristic;
import src.tools.aStar.Mover;
import src.tools.aStar.TileBasedMap;

public class ClosestSquaredHeuristic implements AStarHeuristic {
    public ClosestSquaredHeuristic() {
    }

    public float getCost(TileBasedMap map, Mover mover, int x, int y, int tx, int ty) {
        float dx = (float)(tx - x);
        float dy = (float)(ty - y);
        return dx * dx + dy * dy;
    }
}