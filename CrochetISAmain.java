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
	Stitch createStitch(int insertionPoint, Stitch prevStitch) {
		int width = 0;
		//find Stitch width by tracing prevRow
		Stitch[] prevRow = prevStitch.getPrevRow();

		if(prevRow.length > 1) {
			//previous stitch was a decrease/spanned multiple stitches, so there isn't room to skip stitches
			width = 1;
		}
		else {
			Stitch current = prevStitch;
			while(current.getPrevRow() != prevRow) {	//may need to add explicit comparator function
				current = current.getBefore();
				width++;
			}
		}
		Stitch insertionStitch = prevStitch;
		//assumes availability of insertion point
		for(int i = 0; i < width; i++) {
			insertionStitch = insertionStitch.getPrevRow()[0].getBefore();
		}
		Stitch row[] = {insertionStitch};
		return new Stitch(prevStitch, row, insertionPoint, true);
	}

	Stitch chain(Stitch prevStitch){
		//only create connection to previous stitch
		Stitch chain = new Stitch(prevStitch, null, 0, false);
		return chain;
	}

	Stitch newRow(Stitch prevStitch, int turningChains){
		Stitch current = prevStitch;
		for(int i = 0; i < turningChains; i++){
			current = chain(current);
		}
		Stitch row[] = {prevStitch};
		return new Stitch(current, row, 0, true);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
