import java.util.ArrayList;
import java.util.Arrays;

/**
 * Keeps track of insertion points
 */
public class Stitch {
	int id;
	int attachPoint;
	ArrayList<Integer> connectedTo;
	String type;

	public Stitch(int id, int attached, Integer[] insert, String type) {
		this.id = id;
		this.attachPoint = attached;
		this.connectedTo = new ArrayList<Integer>(Arrays.asList(insert));
		this.type = type;
	}

	public Stitch(int id, String type){
		this.id = id;
		this.type = type;
		this.attachPoint = -1;
		this.connectedTo = new ArrayList<Integer>();
	}

	public void setAttachPoint(int attachPoint) {
		this.attachPoint = attachPoint;
	}

	public void addConnection(int connection) {
		connectedTo.add(connection);
	}

	public String toString(){
		return "type: " + type + ", id: " + id + ", attachPoint: " + attachPoint + ", connectedTo: " + connectedTo;
	}
}
