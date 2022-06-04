package src.tools.aStar;


import src.tools.aStar.heuristics.ClosestHeuristic;

import java.util.ArrayList;
import java.util.Collections;

/**
 * CREDIT TO KEVIN GLASS FOR A* ALGORITHM
 */

public class AStarPathFinder implements PathFinder {
    private ArrayList closed;
    private AStarPathFinder.SortedList open;
    private TileBasedMap map;
    private int maxSearchDistance;
    private AStarPathFinder.Node[][] nodes;
    private boolean allowDiagMovement;
    private AStarHeuristic heuristic;

    public AStarPathFinder(TileBasedMap map, int maxSearchDistance, boolean allowDiagMovement) {
        this(map, maxSearchDistance, allowDiagMovement, new ClosestHeuristic());
    }

    public AStarPathFinder(TileBasedMap map, int maxSearchDistance, boolean allowDiagMovement, AStarHeuristic heuristic) {
        this.closed = new ArrayList();
        this.open = new AStarPathFinder.SortedList();
        this.heuristic = heuristic;
        this.map = map;
        this.maxSearchDistance = maxSearchDistance;
        this.allowDiagMovement = allowDiagMovement;
        this.nodes = new AStarPathFinder.Node[map.getWidthInTiles()][map.getHeightInTiles()];

        for(int x = 0; x < map.getWidthInTiles(); ++x) {
            for(int y = 0; y < map.getHeightInTiles(); ++y) {
                this.nodes[x][y] = new AStarPathFinder.Node(x, y);
            }
        }

    }

    public Path findPath(Mover mover, int sx, int sy, int tx, int ty) {
        if (this.map.blocked(mover, tx, ty)) {
            return null;
        } else {
            this.nodes[sx][sy].cost = 0.0F;
            this.nodes[sx][sy].depth = 0;
            this.closed.clear();
            this.open.clear();
            this.open.add(this.nodes[sx][sy]);
            this.nodes[tx][ty].parent = null;
            int maxDepth = 0;

            while(maxDepth < this.maxSearchDistance && this.open.size() != 0) {
                AStarPathFinder.Node current = this.getFirstInOpen();
                if (current == this.nodes[tx][ty]) {
                    break;
                }

                this.removeFromOpen(current);
                this.addToClosed(current);

                for(int x = -1; x < 2; ++x) {
                    for(int y = -1; y < 2; ++y) {
                        if ((x != 0 || y != 0) && (this.allowDiagMovement || x == 0 || y == 0)) {
                            int xp = x + current.x;
                            int yp = y + current.y;
                            if (this.isValidLocation(mover, sx, sy, xp, yp)) {
                                float nextStepCost = current.cost + this.getMovementCost(mover, current.x, current.y, xp, yp);
                                AStarPathFinder.Node neighbour = this.nodes[xp][yp];
                                this.map.pathFinderVisited(xp, yp);
                                if (nextStepCost < neighbour.cost) {
                                    if (this.inOpenList(neighbour)) {
                                        this.removeFromOpen(neighbour);
                                    }

                                    if (this.inClosedList(neighbour)) {
                                        this.removeFromClosed(neighbour);
                                    }
                                }

                                if (!this.inOpenList(neighbour) && !this.inClosedList(neighbour)) {
                                    neighbour.cost = nextStepCost;
                                    neighbour.heuristic = this.getHeuristicCost(mover, xp, yp, tx, ty);
                                    maxDepth = Math.max(maxDepth, neighbour.setParent(current));
                                    this.addToOpen(neighbour);
                                }
                            }
                        }
                    }
                }
            }

            if (this.nodes[tx][ty].parent == null) {
                return null;
            } else {
                Path path = new Path();

                for(AStarPathFinder.Node target = this.nodes[tx][ty]; target != this.nodes[sx][sy]; target = target.parent) {
                    path.prependStep(target.x, target.y);
                }

                path.prependStep(sx, sy);
                return path;
            }
        }
    }

    protected AStarPathFinder.Node getFirstInOpen() {
        return (AStarPathFinder.Node)this.open.first();
    }

    protected void addToOpen(AStarPathFinder.Node node) {
        this.open.add(node);
    }

    protected boolean inOpenList(AStarPathFinder.Node node) {
        return this.open.contains(node);
    }

    protected void removeFromOpen(AStarPathFinder.Node node) {
        this.open.remove(node);
    }

    protected void addToClosed(AStarPathFinder.Node node) {
        this.closed.add(node);
    }

    protected boolean inClosedList(AStarPathFinder.Node node) {
        return this.closed.contains(node);
    }

    protected void removeFromClosed(AStarPathFinder.Node node) {
        this.closed.remove(node);
    }

    protected boolean isValidLocation(Mover mover, int sx, int sy, int x, int y) {
        boolean invalid = x < 0 || y < 0 || x >= this.map.getWidthInTiles() || y >= this.map.getHeightInTiles();
        if (!invalid && (sx != x || sy != y)) {
            invalid = this.map.blocked(mover, x, y);
        }

        return !invalid;
    }

    public float getMovementCost(Mover mover, int sx, int sy, int tx, int ty) {
        return this.map.getCost(mover, sx, sy, tx, ty);
    }

    public float getHeuristicCost(Mover mover, int x, int y, int tx, int ty) {
        return this.heuristic.getCost(this.map, mover, x, y, tx, ty);
    }

    private class Node implements Comparable {
        private int x;
        private int y;
        private float cost;
        private AStarPathFinder.Node parent;
        private float heuristic;
        private int depth;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int setParent(AStarPathFinder.Node parent) {
            this.depth = parent.depth + 1;
            this.parent = parent;
            return this.depth;
        }

        public int compareTo(Object other) {
            AStarPathFinder.Node o = (AStarPathFinder.Node)other;
            float f = this.heuristic + this.cost;
            float of = o.heuristic + o.cost;
            if (f < of) {
                return -1;
            } else {
                return f > of ? 1 : 0;
            }
        }
    }

    private class SortedList {
        private ArrayList list;

        private SortedList() {
            this.list = new ArrayList();
        }

        public Object first() {
            return this.list.get(0);
        }

        public void clear() {
            this.list.clear();
        }

        public void add(Object o) {
            this.list.add(o);
            Collections.sort(this.list);
        }

        public void remove(Object o) {
            this.list.remove(o);
        }

        public int size() {
            return this.list.size();
        }

        public boolean contains(Object o) {
            return this.list.contains(o);
        }
    }
}