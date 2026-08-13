import java.util.ArrayList;

public class Loop implements HookElement {
    private int id;
    private int stitchId;
    private ArrayList<HookElement> attachedTo;
    private boolean top;

    public Loop(int id, int stitchId){
        this.id = id;
        this.stitchId = stitchId;
        attachedTo = new ArrayList<HookElement>();
        top = false;
    }

    public void addConnection(HookElement element){
        attachedTo.add(element);
    }

    public boolean removeConnection(HookElement element){
        return attachedTo.remove(element);
    }

    public ArrayList<HookElement> getConnections(){
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

    public int getStitchID(){
        return stitchId;
    }
    
	public void setStitchID(int id){
		stitchId = id;
	}

    public String toString(){
        return "[Loop: " + id + ", Stitch: " + stitchId +"]";
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Loop){
            Loop l = (Loop) o;
            return this.id == l.id;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return id;
    }
}
