import java.util.ArrayList;

public class Post implements HookElement{
    int stitchID;
    ArrayList<Loop> loops;

    public Post(int stitchID){
        this.stitchID = stitchID;
        loops = new ArrayList<Loop>();
    }

    public Post(int stitchID, ArrayList<Loop> loops){
        this.stitchID = stitchID;
        this.loops = loops;
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

    public void setStitchID(int stitchID){
        this.stitchID = stitchID;
    }

    public int getStitchID(){
        return stitchID;
    }

    public String toString(){
        return "Post: " + stitchID;
    }

    public boolean equals(Object o){
        if(o instanceof Post){
            Post p = (Post) o;
            return this.stitchID == p.stitchID;
        }
        return false;
    }
    
}
