import java.util.ArrayList;

public class BetweenSpace implements HookElement{
    int stitchID;
    ArrayList<Loop> loops;
    
    public BetweenSpace(int stitchID){
        this.stitchID = stitchID;
        loops = new ArrayList<Loop>();
    }

    public void setStitchID(int stitchID){
        this.stitchID = stitchID;
    }

    public int getStitchID(){
        return stitchID;
    }

    public void addLoop(Loop l){
        loops.add(l);
    }

    public boolean removeLoop(Loop l){
        return loops.remove(l);
    }

    public Loop removeLoop(int index){
        return loops.remove(index);
    }

    public int size(){
        return loops.size();
    }

    public ArrayList<Loop> getLoops(){
        return loops;
    }

    public String toString(){
        return "[Between Space: " + stitchID + "; Loops: " + loops + "]";
    }
}
