/**
 * Keeps track of insertion points
 */
public class Stitch {
	boolean top;
	boolean between;
	Stitch before;
	Stitch prevRow[];
	//Where was this inserted into? (front loop, back loop, top, between, fp, bp?)
	int insertionPoint;
	
	Stitch(Stitch before, Stitch prevRow[], int insertionPoint, boolean between){
		this.before = before;
		this.prevRow = prevRow;
		this.insertionPoint = insertionPoint;
		top = true;
		this.between = between;
	}
	
	Stitch getBefore() {return before;}
	
	Stitch[] getPrevRow() {return prevRow;}
	
	boolean getTop() {return top;}
	void consumeTop() {top = false;}
	boolean getBetween() {return between;}
	void consumeBetween() {between = false;}
	int getInsert() {return insertionPoint;}
}
