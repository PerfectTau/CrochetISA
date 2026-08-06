import java.util.ArrayList;

public class Post {
    ArrayList<loop> loops;
    int stitchID;

    public Post(int stitchID){
        this.stitchID = stitchID;
        loops = new ArrayList<loop>();
    }

    public Post(int stitchID, ArrayList<loop> loops){
        this.stitchID = stitchID;
        this.loops = loops;
    }

    public void addLoop(loop l){
        loops.add(l);
    }

    public int size(){
        return loops.size();
    }

    public ArrayList<loop> getLoops(){
        return loops;
    }
    
}
