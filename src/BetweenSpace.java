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

    public int size(){
        return loops.size();
    }

    public ArrayList<Loop> getLoops(){
        return loops;
    }
}
