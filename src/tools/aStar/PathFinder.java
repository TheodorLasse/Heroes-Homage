package src.tools.aStar;

import java.awt.*;

public interface PathFinder {
    Path findPath(Mover var1, int var2, int var3, int var4, int var5);

    void setMap(PathMap newMap);
}