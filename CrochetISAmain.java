/**
 * 
 */
public class CrochetISAmain {
	/**
	 * Creates a new stitch at the first insertion point that will create a flat fabric
	 * @param insertionPoint: where the stitch will be inserted (top, between)
	 * @param between: does this stitch create a new between
	 * @param prevStitch: stitch currently on hook (previous to this new stitch)
	 * @return: the newly created stitch
	 */
	Stitch createStitch(int insertionPoint, boolean between, Stitch prevStitch) {
		int width = 0;
		//find Stitch width by tracing prevRow
		Stitch[] prevRow = prevStitch.prevRow;
		if(prevRow.length > 1) {
			//previous stitch was a decrease/spanned multiple stitches, so there isn't room to skip stitches
			width = 1;
		}
		else {
			Stitch current = prevStitch;
			while(current.prevRow != prevRow) {	//may need to add explicit comparator function
				current = current.before;
				width++;
			}
		}
		Stitch insertionStitch = prevRow[0].before;
		//assumes availability of insertion point
		for(int i = 0; i < width; i++) {
			insertionStitch = insertionStitch.prevRow[0].before;
		}
		Stitch row[] = {insertionStitch};
		return new Stitch(prevStitch, row, insertionPoint, between);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
