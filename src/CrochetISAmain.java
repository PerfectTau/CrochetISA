import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/**
 * 
 */
public class CrochetISAmain {
	//enumerate insertion points
	final static String TOP = "top";
	final static String FRONT_LOOP = "front_loop";
	final static String BACK_LOOP = "back_loop";
	final static String FRONT_POST = "front_post";
	final static String BACK_POST = "back_post";
	final static String BETWEEN = "between";

	//enumerate stitch types
	final static String INSERT = "insert";
	final static String YO = "yo";
	final static String PT = "pt";
	final static String MOVE = "move";
	final static String TURN = "turn";
	final static String PT_ALL = "pt_all";
	
	public static void main(String[] args) {
		//key stitches
		String[] ch = {YO, PT};
		String[] sc = {INSERT, YO, PT, YO, PT, PT};
		String[] hdc = {YO, INSERT, YO, PT, YO, PT, PT, PT};
		String[] dc = {YO, INSERT, YO, PT, YO, PT, PT, YO, PT, PT};
		ArrayList<String> CH = new ArrayList<String>(Arrays.asList(ch));
		ArrayList<String> SC = new ArrayList<String>(Arrays.asList(sc));
		ArrayList<String> HDC = new ArrayList<String>(Arrays.asList(hdc));
		ArrayList<String> DC = new ArrayList<String>(Arrays.asList(dc));
		ArrayList<String> turn = new ArrayList<String>();
		turn.add(TURN);

		//decrease stems
		String[] decSC = {INSERT, YO, PT};
		String[] decHDC = {YO, INSERT, YO, PT};
		String[] decDC = {YO, INSERT, YO, PT, YO, PT, PT};

		//increase (2 in one stitch)
		ArrayList<String> incSC = new ArrayList<String>(Arrays.asList(sc));
		incSC.addAll(Arrays.asList(sc));
		ArrayList<String> incHDC = new ArrayList<String>(Arrays.asList(hdc));
		incHDC.addAll(Arrays.asList(hdc));
		ArrayList<String> incDC = new ArrayList<String>(Arrays.asList(dc));
		incDC.addAll(Arrays.asList(dc));

		//decrease (2 together)
		ArrayList<String> dec2SC = new ArrayList<String>(Arrays.asList(decSC));
		dec2SC.add(MOVE);
		dec2SC.addAll(Arrays.asList(decSC));
		dec2SC.add(YO);
		dec2SC.add(PT_ALL);

		ArrayList<String> dec2HDC = new ArrayList<String>(Arrays.asList(decHDC));
		dec2HDC.add(MOVE);
		dec2HDC.addAll(Arrays.asList(decHDC));
		dec2HDC.add(YO);
		dec2HDC.add(PT_ALL);

		ArrayList<String> dec2DC = new ArrayList<String>(Arrays.asList(decDC));
		dec2DC.add(MOVE);
		dec2DC.addAll(Arrays.asList(decDC));
		dec2DC.add(YO);
		dec2DC.add(PT_ALL);


		// create stitch hash map
		HashMap<String, ArrayList<String>> stitchMap = new HashMap<String, ArrayList<String>>();
		stitchMap.put("turn", turn);
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

		StringBuilder output = new StringBuilder();
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the file path to pattern: ");
		String filename = scanner.nextLine();
		scanner.close();
		Parser parser = new Parser(filename);

		//Parse initial strings to action values
		if(parser.newStitches){
			//implement this functionality
		}
		ArrayList<ArrayList<String>> rows = parser.getRows();
		int currentLoops = 0;
		for(ArrayList<String> row : rows){
			for(String stitch : row){
				if(stitchMap.containsKey(stitch)){
					ArrayList<String> actions = stitchMap.get(stitch);
					for(String action : actions){
						if(action == YO){
							currentLoops++;
						}
						if(action == PT){
							currentLoops--;
						}
						if(action == PT_ALL){
							for(int i = 0; i < currentLoops; i++){
								output.append(PT + " ");
							}
							currentLoops = 0;
							continue;
						}
						if(action == INSERT){
							output.append(MOVE + " ");
						}
						output.append(action + " ");
						if(action == TURN){
							output.append("\n");
						}
					}
				}
				else{
					System.out.println("Error: Unrecognized stitch '" + stitch + "'.");
				}
			}
		}
		System.out.println(output.toString());
	}
}

