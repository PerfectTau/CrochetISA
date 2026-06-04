/**
 * Keeps track of insertion points
 */
public class Stitch {
	int id;
	int attached;
	int insert;;
	int type;

	public Stitch(int id, int attached, int insert, int type) {
		this.id = id;
		this.attached = attached;
		this.insert = insert;
		this.type = type;
	}
}
