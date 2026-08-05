import java.util.ArrayList;

/**
 * Keeps track of insertion points
 */
public class Stitch {
	int row;
	int index;
	int attachPoint;
	ArrayList<twoItems> connectedTo;
	ArrayList<String> actions;
	String type;
	int postHeight;
	boolean between;

	public Stitch(int row, int index, String type){
		this.row = row;
		this.index = index;
		this.type = type;
		this.attachPoint = -1;
		this.connectedTo = new ArrayList<twoItems>();
		postHeight = 0;
		between = false;
		actions = new ArrayList<String>();
	}

	public void setAttachPoint(int attachPoint) {
		this.attachPoint = attachPoint;
	}

	public void addConnection(twoItems connection) {
		connectedTo.add(connection);
	}

	public ArrayList<twoItems> getConnections(){
		return connectedTo;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getType(){
		return type;
	}

	public int getIndex() {
		return index;
	}

	public void setHeight(int height){
		postHeight = height;
	}

	public int getHeight(){
		return postHeight;
	}

	public void setBetween(boolean b){
		between = b;
	}

	public void setActions(ArrayList<String> newActions){
		actions = newActions;
	}

	public ArrayList<String> getActions(){
		return actions;
	}

	public String toString(){
		return "type: " + type + ", row: " + row + ", index: " + index + ", attachPoint: " + attachPoint + ", connectedTo: " + connectedTo + ", postHeight: " + postHeight+ ", Between Space Available? "+ between;
	}
}
