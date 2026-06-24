import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 */
public class CrochetISAmain {
	// insertion points
	final static String TOP = "insert top";
	final static String FRONT_LOOP = "insert front loop";
	final static String BACK_LOOP = "insert back loop";
	final static String FRONT_POST = "insert front post";
	final static String BACK_POST = "insert back post";
	final static String BETWEEN = "insert between";

	// stitch types
	final static String INSERT = "insert";
	final static String YO = "yo";
	final static String PT = "pt";
	final static String MOVE = "move"; 
	final static String TURN = "turn";
	final static String PT_ALL = "pt_all";

	final static int top = 0;
	final static int front_loop = 1;
	final static int back_loop = 2;
	final static int front_post = 3;
	final static int back_post = 4;
	final static int between = 5;

	public static void main(String[] args) {
		//key stitches after stitch insertion
		String[] ch = {YO, PT};
		String[] sc = {INSERT, YO, PT, "decrease", YO, PT, PT};
		String[] hdc = {YO, INSERT, YO, PT, "decrease", YO, PT, PT, PT};
		String[] dc = {YO, INSERT, YO, PT, YO, PT, PT, "decrease",YO, PT, PT};

		ArrayList<String> CH = new ArrayList<String>(Arrays.asList(ch));
		ArrayList<String> SC = new ArrayList<String>(Arrays.asList(sc));
		ArrayList<String> HDC = new ArrayList<String>(Arrays.asList(hdc));
		ArrayList<String> DC = new ArrayList<String>(Arrays.asList(dc));

// Single Crochet
// 		ArrayList<String> SC = new ArrayList<String>();
// 		SC.add(TOP);
// 		SC.addAll(Arrays.asList(sc));

// 		ArrayList<String> SCFL = new ArrayList<String>(SC);
// 		SCFL.set(0, FRONT_LOOP);
// 		ArrayList<String> SCBL = new ArrayList<String>(SC);
// 		SCBL.set(0, BACK_LOOP);

// 		ArrayList<String> FPSC = new ArrayList<String>(SC);
// 		FPSC.set(0, FRONT_POST);
// 		ArrayList<String> BPSC = new ArrayList<String>(SC);
// 		BPSC.set(0, BACK_POST);
// // Half Double Crochet
// 		ArrayList<String> HDC = new ArrayList<String>();
// 		HDC.add(YO);
// 		HDC.add(TOP);
// 		HDC.addAll(Arrays.asList(hdc));

// 		ArrayList<String> HDCFL = new ArrayList<String>(HDC);
// 		HDCFL.set(1, FRONT_LOOP);
// 		ArrayList<String> HDCBL = new ArrayList<String>(HDC);
// 		HDCBL.set(1, BACK_LOOP);
// 		ArrayList<String> FPHDC = new ArrayList<String>(HDC);
// 		FPHDC.set(1, FRONT_POST);
// 		ArrayList<String> BPHDC = new ArrayList<String>(HDC);
// 		BPHDC.set(1, BACK_POST);
// // Double Crochet		
// 		ArrayList<String> DC = new ArrayList<String>();
// 		DC.add(YO);
// 		DC.add(TOP);
// 		DC.addAll(Arrays.asList(dc));

// 		ArrayList<String> DCFL = new ArrayList<String>(DC);
// 		DCFL.set(1, FRONT_LOOP);
// 		ArrayList<String> DCBL = new ArrayList<String>(DC);
// 		DCBL.set(1, BACK_LOOP);
// 		ArrayList<String> FPDC = new ArrayList<String>(DC);
// 		FPDC.set(1, FRONT_POST);
// 		ArrayList<String> BPDC = new ArrayList<String>(DC);
// 		BPDC.set(1, BACK_POST);

		ArrayList<String> turn = new ArrayList<String>();
		turn.add(TURN);

		// create stitch hash map
		HashMap<String, ArrayList<String>> stitchMap = new HashMap<String, ArrayList<String>>();
		stitchMap.put("turn", turn);
		stitchMap.put("ch", CH);
		stitchMap.put("sc", SC);
		stitchMap.put("hdc", HDC);
		stitchMap.put("dc", DC);
		stitchMap.put("scfl", SC);
		stitchMap.put("scbl", SC);
		stitchMap.put("fpsc", SC);
		stitchMap.put("bpsc", SC);
		stitchMap.put("hdcfl", HDC);
		stitchMap.put("hdcbl", HDC);
		stitchMap.put("fphdc", HDC);
		stitchMap.put("bphdc", HDC);
		stitchMap.put("dcfl", DC);
		stitchMap.put("dcbl", DC);
		stitchMap.put("fpdc", DC);
		stitchMap.put("bpdc", DC);
		// stitchMap.put("scfl", SCFL);
		// stitchMap.put("scbl", SCBL);
		// stitchMap.put("fpsc", FPSC);
		// stitchMap.put("bpsc", BPSC);
		// stitchMap.put("hdcfl", HDCFL);
		// stitchMap.put("hdcbl", HDCBL);
		// stitchMap.put("fphdc", FPHDC);
		// stitchMap.put("bphdc", BPHDC);
		// stitchMap.put("dcfl", DCFL);
		// stitchMap.put("dcbl", DCBL);
		// stitchMap.put("fpdc", FPDC);
		// stitchMap.put("bpdc", BPDC);

		StringBuilder output = new StringBuilder();
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the file path to pattern: ");
		String filename = scanner.nextLine();
		scanner.close();
		Parser parser = new Parser(filename);
		ArrayList<ArrayList<Stitch>> stitchRows = new ArrayList<ArrayList<Stitch>>();
		String attachPoint = TOP;
		int id = 0;
		int nextConnection = 0;
		int attach;
		boolean increasing = false;

		//Parse initial strings to action values
		if(parser.newStitches){
			//implement this functionality
		}
		ArrayList<ArrayList<String>> rows = parser.getRows();
		int currentLoops = 0;
		ArrayList<Stitch> firstRow = new ArrayList<Stitch>();
		for(String stitch : rows.get(0)){
			// Process the first row
			if(!stitch.equals("ch") && !stitch.equals("turn")){
				throw new IllegalArgumentException("Invalid stitch in first row: " + stitch);
			}
				Stitch currentStitch = new Stitch(id, stitch);
				if(stitchMap.containsKey(stitch)){
					ArrayList<String> actions = stitchMap.get(stitch);
					for(String action : actions){
						if(action.equals("decrease")){
							continue;
						}
						if(action.equals(YO) || action.equals(INSERT)){
							currentLoops++;
						}
						if(action.equals(PT)){
							currentLoops--;
						}
						if(action.equals(PT_ALL)){
							for(int i = 0; i < currentLoops; i++){
								output.append(PT + ", ");
							}
							currentLoops = 0;
							continue;
						}
						if(action.equals(INSERT)){
							output.append(attachPoint + ", ");
							continue;
						}
						output.append(action + ", ");
						if(action.equals(TURN)){
							// deal with stitch row
							nextConnection = id;
							output.append("\n");
						}
						
					}
					if(!stitch.equals(TURN))
						firstRow.add(currentStitch);
				}
				else{
					//System.out.println("Error: Unrecognized stitch '" + stitch + "'.");
					throw new IllegalArgumentException("Unrecognized stitch: " + stitch);
				}
				id++;
		}
		id--;
		stitchRows.add(firstRow);

		//Process rest of the pattern
		for(int z = 1; z < rows.size(); z++){
			ArrayList<String> row = rows.get(z);
			ArrayList<Stitch> currRow = new ArrayList<Stitch>();
			for(String stitch : row){				
				//check attach point
				attach = top;
				if(Pattern.matches(".*fl", stitch)){
					attachPoint = FRONT_LOOP;
					attach = front_loop;
				}
				else if(Pattern.matches(".*bl", stitch)){
					attachPoint = BACK_LOOP;
					attach = back_loop;
				}
				else if(Pattern.matches("^fp.*", stitch)){
					attachPoint = FRONT_POST;
					attach = front_post;
				}
				else if(Pattern.matches("^bp.*", stitch)){
					attachPoint = BACK_POST;
					attach = back_post;
				}

				// Create stitch object with stitch type and attach point
				Stitch currentStitch = new Stitch(id, stitch);
				currentStitch.setAttachPoint(attach);

				//check increase/decrease
				Pattern incDecPattern = Pattern.compile("^(.*)(\\d+)(tog|inc)$");
				Matcher matcher = incDecPattern.matcher(stitch);
				if(matcher.matches()){
					String stitchType = matcher.group(1);
					int count = Integer.parseInt(matcher.group(2));
					String operation = matcher.group(3);
					System.out.println("Increase/Decrease: " + count + " " + stitchType + " " + operation);

					//Increase
					if(operation.equals("inc")){
						if(stitchMap.containsKey(stitchType)){
							currentStitch.addConnection(nextConnection);
							ArrayList<String> actions = stitchMap.get(stitchType);
							for(int i = 0; i < count; i++){
								for(String action : actions){ 
									if(action.equals(YO) || action.equals(INSERT)){
										currentLoops++;
									}
									if(action.equals(PT)){
										currentLoops--;
									}
									if(action.equals(PT_ALL)){
										for(int j = 0; j < currentLoops; j++){
											output.append(PT + ", ");
										}
										currentLoops = 0;
										continue;
									}
									if(action.equals(INSERT)){
										output.append(attachPoint + ", ");
										continue;
									}
									output.append(action + ", ");
									currentStitch.addConnection(nextConnection);
									currRow.add(currentStitch);
								}
							}
						}
					}
					else{
						//Decrease
						if(stitchMap.containsKey(stitchType)){
							ArrayList<String> actions = stitchMap.get(stitchType);
							currentStitch.addConnection(nextConnection);
							// separate action stems for decreasing
							int index = actions.indexOf("decrease");
							for(int i = 0; i < count; i++){
								for(int j = 0; j < index; j++){
									String action = actions.get(j);
									if(action.equals(YO) || action.equals(INSERT)){
										currentLoops++;
									}
									if(action.equals(PT)){
										currentLoops--;
									}
									if(action.equals(INSERT)){
										output.append(attachPoint + ", ");
										continue;
									}
									output.append(action + ", ");
								}
								if(i < count - 1){
									output.append(MOVE + ", ");
								}
								if(increasing){
									nextConnection++;
								}
								else{
									nextConnection--;
								}
								currentStitch.addConnection(nextConnection);
							}

							output.append(YO + ", ");
							for(int k = 0; k < currentLoops + 1; k++){
								output.append(PT + ", ");
							}
							currRow.add(currentStitch);
						}

					}
				}
				else if(stitchMap.containsKey(stitch)){
					ArrayList<String> actions = stitchMap.get(stitch);
					for(String action : actions){
						if(action.equals("decrease")){
							continue;
						}
						if(action.equals(YO) || action.equals(INSERT)){
							currentLoops++;
						}
						if(action.equals(PT)){
							currentLoops--;
						}
						if(action.equals(PT_ALL)){
							for(int i = 0; i < currentLoops; i++){
								output.append(PT + ", ");
							}
							currentLoops = 0;
							continue;
						}
						if(action.equals(INSERT)){
							output.append(attachPoint + ", ");
							continue;
						}
						output.append(action + ", ");
						if(action.equals(TURN)){
							// deal with stitch row
							increasing = !increasing;
							nextConnection = id;
							output.append("\n");
							id--;
						}
						
					}
					if(!stitch.equals(TURN)){
						if(!stitch.equals("ch")){
							output.append(MOVE + ", ");
							currentStitch.addConnection(nextConnection);
					}
						currRow.add(currentStitch);
					}
				}
				else{
					//System.out.println("Error: Unrecognized stitch '" + stitch + "'.");
					throw new IllegalArgumentException("Unrecognized stitch: " + stitch);
				}
				id++;
				if(!stitch.equals("ch")){
					if(increasing){
						nextConnection++;
					}else{
						nextConnection--;
					}
				}
			}
			stitchRows.add(currRow);
		}
		System.out.println("Actions: ");
		System.out.println(output.toString());
		System.out.println("Stitches:");
		for(ArrayList<Stitch> row : stitchRows){
			System.out.println("Row: ");
			for(Stitch s : row){
				System.out.println(s.toString());
			}
		}
		
}}
