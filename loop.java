import java.util.ArrayList;

public class loop {
    private int id;
    private int stitchId;
    private ArrayList<Integer> attachedTo;
    private boolean top;

    public loop(int id, int stitchId){
        this.id = id;
        this.stitchId = stitchId;
        attachedTo = new ArrayList<Integer>();
        top = false;
    }

    public void addConnection(Integer i){
        attachedTo.add(i);
    }

    public boolean removeConnection(Integer i){
        return attachedTo.remove(i);
    }

    public ArrayList<Integer> getConnections(){
        return attachedTo;
    }

    public void toggleTop(){
        top = !top;
    }

    public boolean isTop(){
        return top;
    }

    public int getID(){
        return id;
    }

    public int getStitch(){
        return stitchId;
    }
    
	public void setStitchID(int id){
		stitchId = id;
	}

    public String toString(){
        return "[Loop: " + id + ", Stitch: " + stitchId + ", Connected To: " + attachedTo + "]";
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof loop){
            loop l = (loop) o;
            return this.id == l.id;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return id;
    }
}
