import java.util.ArrayList;

/**
 * Keeps track of insertion points
 */
public class Stitch {
	int row;
	int index;
	int attachPoint;
	ArrayList<twoItems> connectedTo;
	String type;
	int postHeight;

	// public Stitch(int row, int index, int attached, Stitch[] insert, String type) {
	// 	this.row = row;
	// 	this.index = index;
	// 	this.attachPoint = attached;
	// 	this.connectedTo = new ArrayList<twoItems>(Arrays.asList(insert));
	// 	this.type = type;
	// }

	public Stitch(int row, int index, String type){
		this.row = row;
		this.index = index;
		this.type = type;
		this.attachPoint = -1;
		this.connectedTo = new ArrayList<twoItems>();
		postHeight = 0;
	}

	public void setAttachPoint(int attachPoint) {
		this.attachPoint = attachPoint;
	}

	public void addConnection(twoItems connection) {
		connectedTo.add(connection);
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


	public String toString(){
		return "type: " + type + ", row: " + row + ", index: " + index + ", attachPoint: " + attachPoint + ", connectedTo: " + connectedTo + "postHeight: " + postHeight;
	}
}
