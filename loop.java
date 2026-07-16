import java.util.ArrayList;

public class loop {
    private int id;
    private int stitchId;
    private ArrayList<loop> attachedTo;
    private boolean top;

    public loop(int id, int stitchId){
        this.id = id;
        this.stitchId = stitchId;
        attachedTo = new ArrayList<loop>();
        top = false;
    }

    public void addConnection(loop l){
        attachedTo.add(l);
    }

    public ArrayList<loop> getConnections(){
        return attachedTo;
    }

    public void top(){
        top = true;
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
        return "[Loop: " + id + ", Stitch: " + stitchId + ", Top: " + top + "]";
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
