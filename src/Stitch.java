import java.util.ArrayList;
import java.util.Arrays;

/**
 * Keeps track of insertion points
 */
public class Stitch {
	int row;
	int index;
	int attachPoint;
	ArrayList<Stitch> connectedTo;
	String type;

	public Stitch(int row, int index, int attached, Stitch[] insert, String type) {
		this.row = row;
		this.index = index;
		this.attachPoint = attached;
		this.connectedTo = new ArrayList<Stitch>(Arrays.asList(insert));
		this.type = type;
	}

	public Stitch(int row, int index, String type){
		this.row = row;
		this.index = index;
		this.type = type;
		this.attachPoint = -1;
		this.connectedTo = new ArrayList<Stitch>();
	}

	public void setAttachPoint(int attachPoint) {
		this.attachPoint = attachPoint;
	}

	public void addConnection(Stitch connection) {
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

	public String toString(){
		return "type: " + type + ", row: " + row + ", index: " + index + ", attachPoint: " + attachPoint + ", connectedTo: " + connectedTo;
	}
}
