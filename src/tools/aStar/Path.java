package src.tools.aStar;


import java.util.ArrayList;

/**
 * CREDIT TO KEVIN GLASS FOR A* ALGORITHM
 */
public class Path implements Comparable<Path>{
    private final ArrayList<Step> steps = new ArrayList<>();

    public Path() {
    }

    public int getLength() {
        return this.steps.size();
    }

    public Path.Step getStep(int index) {
        return (Path.Step)this.steps.get(index);
    }

    public int getX(int index) {
        return this.getStep(index).x;
    }

    public int getY(int index) {
        return this.getStep(index).y;
    }

    public void appendStep(int x, int y) {
        this.steps.add(new Step(x, y));
    }

    public void prependStep(int x, int y) {
        this.steps.add(0, new Step(x, y));
    }

    public boolean contains(int x, int y) {
        return this.steps.contains(new Step(x, y));
    }

    public Step popStep(){
        return steps.remove(0);
    }

    public boolean equals(Object other){
        if (!(other instanceof Path)) {
            return false;
        } else {
            Path o = ((Path)other);
            if (o.getLength() != this.getLength()) return false;

            boolean result = true;
            for (int i = 0; i < o.getLength(); i++) {
                result = o.getStep(i).equals(this.getStep(i));
                if (!result) break;
            }
            return result;
        }
    }

    @Override
    public int compareTo(Path o) {
        return Integer.compare(this.getLength(), o.getLength());
    }

    public static class Step {
        private final int x;
        private final int y;

        public Step(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        public int hashCode() {
            return this.x * this.y;
        }

        public boolean equals(Object other) {
            if (!(other instanceof Path.Step)) {
                return false;
            } else {
                Path.Step o = (Path.Step)other;
                return o.x == this.x && o.y == this.y;
            }
        }
    }
}