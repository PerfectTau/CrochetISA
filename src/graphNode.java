public class graphNode {
    int x, y;
    Stitch stitch;
    public graphNode(int x, int y, Stitch stitch) {
        this.x = x;
        this.y = y;
        this.stitch = stitch;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Stitch getStitch() {
        return stitch;
    }

    public String toString() {
        return "graphNode [stitch: " + stitch + "]";
    }
}
