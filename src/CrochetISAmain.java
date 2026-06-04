import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * 
 */
public class CrochetISAmain {
	//enumerate insertion points
	final static int TOP = 0;
	final static int FRONT_LOOP = 1;
	final static int BACK_LOOP = 2;
	final static int FRONT_POST = 3;
	final static int BACK_POST = 4;
	final static int BETWEEN = 5;

	//enumerate stitch types
	final static int INSERT = 0;
	final static int YO = 1;
	final static int PT = 2;
	final static int MOVE = 3;
	final static int TURN = 4;
	
	public static void main(String[] args) {
		//key stitches
		Integer[] ch = {YO, PT};
		Integer[] sc = {INSERT, YO, PT, YO, PT, PT};
		Integer[] hdc = {INSERT, YO, PT, YO, PT, PT, PT};
		Integer[] dc = {INSERT, YO, PT, YO, PT, PT, YO, PT, PT};
		ArrayList<Integer> CH = new ArrayList<Integer>(Arrays.asList(ch));
		ArrayList<Integer> SC = new ArrayList<Integer>(Arrays.asList(sc));
		ArrayList<Integer> HDC = new ArrayList<Integer>(Arrays.asList(hdc));
		ArrayList<Integer> DC = new ArrayList<Integer>(Arrays.asList(dc));

		//decrease stems
		Integer[] decSC = {INSERT, YO, PT};
		Integer[] decHDC = {INSERT, YO, PT};
		Integer[] decDC = {INSERT, YO, PT, YO, PT, PT};

		//increase (2 in one stitch)
		ArrayList<Integer> incSC = new ArrayList<Integer>(Arrays.asList(sc));
		incSC.addAll(Arrays.asList(sc));
		ArrayList<Integer> incHDC = new ArrayList<Integer>(Arrays.asList(hdc));
		incHDC.addAll(Arrays.asList(hdc));
		ArrayList<Integer> incDC = new ArrayList<Integer>(Arrays.asList(dc));
		incDC.addAll(Arrays.asList(dc));

		//decrease (2 together)
		ArrayList<Integer> dec2SC = new ArrayList<Integer>(Arrays.asList(decSC));
		dec2SC.add(MOVE);
		dec2SC.addAll(Arrays.asList(decSC));
		dec2SC.add(YO);
		dec2SC.add(PT);
		dec2SC.add(PT);
		dec2SC.add(PT);
		dec2SC.add(PT);

		ArrayList<Integer> dec2HDC = new ArrayList<Integer>(Arrays.asList(decHDC));
		dec2HDC.add(MOVE);
		dec2HDC.addAll(Arrays.asList(decHDC));
		dec2HDC.add(YO);
		dec2HDC.add(PT);
		dec2HDC.add(PT);
		dec2HDC.add(PT);
		dec2HDC.add(PT);

		ArrayList<Integer> dec2DC = new ArrayList<Integer>(Arrays.asList(decDC));
		dec2DC.add(MOVE);
		dec2DC.addAll(Arrays.asList(decDC));
		dec2DC.add(YO);
		dec2DC.add(PT);
		dec2DC.add(PT);
		dec2DC.add(PT);
		dec2DC.add(PT);

		// create stitch hash map
		HashMap<String, ArrayList<Integer>> stitchMap = new HashMap<String, ArrayList<Integer>>();
		stitchMap.put("ch", CH);
		stitchMap.put("sc", SC);
		stitchMap.put("hdc", HDC);
		stitchMap.put("dc", DC);
		stitchMap.put("sc2tog", dec2SC);
		stitchMap.put("hdc2tog", dec2HDC);
		stitchMap.put("dc2tog", dec2DC);
		stitchMap.put("sc2inc", incSC);
		stitchMap.put("hdc2inc", incHDC);
		stitchMap.put("dc2inc", incDC);
	}
}

