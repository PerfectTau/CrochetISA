public class twoItems {
    private int row;
    private int index;

    public twoItems(int row, int index) {
        this.row = row;
        this.index = index;
    }

    public int getRow() {
        return row;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index){
        this.index = index;
    }

    public String toString() {
        return "Row: " + row + ", Index: " + index;
    }
}
