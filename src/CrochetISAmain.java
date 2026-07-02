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
	final static String SK = "skip";

	final static int top = 0;
	final static int front_loop = 1;
	final static int back_loop = 2;
	final static int front_post = 3;
	final static int back_post = 4;
	final static int between = 5;

	public static void main(String[] args) {
		// key stitches after stitch insertion
		String[] ch = { YO, PT };
		String[] sc = { INSERT, YO, PT, "decrease", YO, PT, PT };
		String[] hdc = { YO, INSERT, YO, PT, "decrease", YO, PT, PT, PT };
		String[] dc = { YO, INSERT, YO, PT, YO, PT, PT, "decrease", YO, PT, PT };

		ArrayList<String> CH = new ArrayList<String>(Arrays.asList(ch));
		ArrayList<String> SC = new ArrayList<String>(Arrays.asList(sc));
		ArrayList<String> HDC = new ArrayList<String>(Arrays.asList(hdc));
		ArrayList<String> DC = new ArrayList<String>(Arrays.asList(dc));

		ArrayList<String> sk = new ArrayList<String>();
		sk.add(SK);
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
		stitchMap.put("sk", sk);

		StringBuilder output = new StringBuilder();
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the file path to pattern: ");
		String filename = scanner.nextLine();
		scanner.close();
		Parser parser = new Parser(filename);
		ArrayList<ArrayList<Stitch>> stitchRows = new ArrayList<ArrayList<Stitch>>();
		String attachPoint = TOP;
		int index = 0;
		int rowIndex = 0;
		Stitch nextConnection = null;
		int attach;
		int connectionIndex = 0;

		// Parse initial strings to action values
		if (parser.newStitches) {
			// implement this functionality
		}
		ArrayList<ArrayList<String>> rows = parser.getRows();
		int currentLoops = 0;

		//Parse first row (must be chain stitches)
		ArrayList<Stitch> firstRow = new ArrayList<Stitch>();
		for (int i = 0; i < rows.get(0).size(); i++) {
			String stitch = rows.get(0).get(i);
			// Process the first row
			if (!stitch.equals("ch") && !stitch.equals("turn")) {
				throw new IllegalArgumentException("Invalid stitch in first row: " + stitch);
			}
			Stitch currentStitch = new Stitch(rowIndex, index, stitch);
			if (stitchMap.containsKey(stitch)) {
				ArrayList<String> actions = stitchMap.get(stitch);
				for (String action : actions) {
					if (action.equals(YO) || action.equals(INSERT)) {
						currentLoops++;
					}
					if (action.equals(PT)) {
						currentLoops--;
					}

					if (action.equals(INSERT)) {
						output.append(attachPoint + ", ");
						continue;
					}
					output.append(action + ", ");
					if (action.equals(TURN)) {
						// deal with stitch row
						nextConnection = firstRow.get(i-1);
						connectionIndex = i - 1;
						output.append("\n");
					}

				}
				if (!stitch.equals(TURN))
					firstRow.add(currentStitch);
			} else {
				// System.out.println("Error: Unrecognized stitch '" + stitch + "'.");
				throw new IllegalArgumentException("Unrecognized stitch: " + stitch);
			}
			index++;
		}
		stitchRows.add(firstRow);
		rowIndex++;

		// Process rest of the pattern
		for (int z = 1; z < rows.size(); z++) {
			ArrayList<String> row = rows.get(z);
			ArrayList<Stitch> currRow = new ArrayList<Stitch>();
			ArrayList<Stitch> prevRow = stitchRows.get(z-1);
			index = 0;
			connectionIndex = prevRow.size() - 1;
			if(connectionIndex != nextConnection.getIndex()){
				connectionIndex = nextConnection.getIndex();
			}
			boolean endOfRow = false;
			String firstStitch = row.get(0);
			if(!firstStitch.equals("ch")){
				int height = 1;
				if(Pattern.matches(".*sc.*", firstStitch) || Pattern.matches(".*hdc.*", firstStitch)){
					height = 1;
				}
				else if(Pattern.matches(".*dc.*", firstStitch)){
					height = 2;
				}

				for(int t = 1; t <= height; t++){
					Stitch prevStitch = prevRow.get(prevRow.size() - t);
					output.append(SK + ", ");
					if(!prevStitch.getType().equals("ch")){
						throw new IllegalArgumentException("Must have a turning chain of at least " + height + " stitches");
					}
				}
				connectionIndex = prevRow.size() - height - 1;
				nextConnection = prevRow.get(connectionIndex);
			}
			for (int s = 0; s < row.size(); s++) {
				String stitch = row.get(s);
				// check attach point
				attach = top;
				if (Pattern.matches(".*fl", stitch)) {
					attachPoint = FRONT_LOOP;
					attach = front_loop;
				} else if (Pattern.matches(".*bl", stitch)) {
					attachPoint = BACK_LOOP;
					attach = back_loop;
				} else if (Pattern.matches("^fp.*", stitch)) {
					attachPoint = FRONT_POST;
					attach = front_post;
				} else if (Pattern.matches("^bp.*", stitch)) {
					attachPoint = BACK_POST;
					attach = back_post;
				}

				// Create stitch object with stitch type and attach point
				Stitch currentStitch = new Stitch(z, index, stitch);
				currentStitch.setAttachPoint(attach);

				// check increase/decrease
				Pattern incDecPattern = Pattern.compile("^(.*)(\\d+)(tog|inc)$");
				Matcher matcher = incDecPattern.matcher(stitch);
				if (matcher.matches()) {
					String stitchType = matcher.group(1);
					int count = Integer.parseInt(matcher.group(2));
					String operation = matcher.group(3);
					System.out.println("Increase/Decrease: " + count + " " + stitchType + " " + operation);

					// Increase
					if (operation.equals("inc")) {
						if (stitchMap.containsKey(stitchType)) {
							ArrayList<String> actions = stitchMap.get(stitchType);
							for (int i = 0; i < count; i++) {
								for (String action : actions) {
									if (action.equals(YO) || action.equals(INSERT)) {
										currentLoops++;
									}
									if (action.equals(PT)) {
										currentLoops--;
									}
									if (action.equals(PT_ALL)) {
										for (int j = 0; j < currentLoops; j++) {
											output.append(PT + ", ");
										}
										currentLoops = 0;
										continue;
									}
									if (action.equals(INSERT)) {
										output.append(attachPoint + ", ");
										continue;
									}
									output.append(action + ", ");
								}
								Stitch newStitch = new Stitch(z, index, stitch);
								newStitch.addConnection(nextConnection);
								index++;
								currRow.add(newStitch);
							}
						}
						index--;
					} else {
						// Decrease
						if (stitchMap.containsKey(stitchType)) {
							ArrayList<String> actions = stitchMap.get(stitchType);
							currentStitch.addConnection(nextConnection);
							// separate action stems for decreasing
							int actionIndex = actions.indexOf("decrease");
							for (int i = 0; i < count; i++) {
								// decrease stems
								for (int j = 0; j < actionIndex; j++) {
									String action = actions.get(j);
									if (action.equals(YO) || action.equals(INSERT)) {
										currentLoops++;
									}
									if (action.equals(PT)) {
										currentLoops--;
									}
									if (action.equals(INSERT)) {
										output.append(attachPoint + ", ");
										continue;
									}
									output.append(action + ", ");
								}
								if (i < count - 1) {
									output.append(MOVE + ", ");
								}
								if(i < count - 1) {
									connectionIndex--;
									nextConnection = prevRow.get(connectionIndex);
									currentStitch.addConnection(nextConnection);
								}
							}

							// Finish decrease (YO, PT_ALL)
							output.append(YO + ", ");
							for (int k = 0; k < currentLoops + 1; k++) {
								output.append(PT + ", ");
							}
							currRow.add(currentStitch);
						}
					}
				}
				// Standard (non-increase/decrease) Stitch parsing
				else if (stitchMap.containsKey(stitch)) {
					ArrayList<String> actions = stitchMap.get(stitch);
					for (String action : actions) {
						if (action.equals("decrease")) {
							continue;
						}
						if (action.equals(YO) || action.equals(INSERT)) {
							currentLoops++;
						}
						if (action.equals(PT)) {
							currentLoops--;
						}
						if (action.equals(PT_ALL)) {
							for (int i = 0; i < currentLoops; i++) {
								output.append(PT + ", ");
							}
							currentLoops = 0;
							continue;
						}
						if (action.equals(INSERT)) {
							output.append(attachPoint + ", ");
							continue;
						}
						if(action.equals(SK)){
							connectionIndex--;
						}
						output.append(action + ", ");
						if (action.equals(TURN)) {
							// deal with stitch row
							nextConnection = currRow.get(index-1);
							output.append("\n");
							endOfRow = true;
						}

					}
					if (!stitch.equals(TURN)) {
						if (!stitch.equals("ch")) {
							output.append(MOVE + ", ");
							currentStitch.addConnection(nextConnection);
						}
						currRow.add(currentStitch);
					}
				} else {
					// System.out.println("Error: Unrecognized stitch '" + stitch + "'.");
					throw new IllegalArgumentException("Unrecognized stitch: " + stitch);
				}
				index++;
				if(connectionIndex < 0){
					throw new IllegalArgumentException("Row " + z + " is too long. You may be missing increases.");
				}
				if (s < row.size() - 1) {
					String nextStitch = row.get(s + 1);
					if(connectionIndex - 1 < 0 && !(nextStitch.equals("ch") || nextStitch.equals(TURN) || nextStitch.equals(SK))){
						System.out.println("Current Stitch: " + stitch + ", Next Stitch: " + nextStitch);
						throw new IllegalArgumentException("Row " + z + " is too long. You may be missing increases.");
					}
					if (!stitch.equals("ch") && !endOfRow && !(row.get(s+1).equals(TURN) || row.get(s+1).equals("ch"))) {
						connectionIndex--;
						nextConnection = prevRow.get(connectionIndex);
						//connectionIndex--;
					}
				}
			}
			stitchRows.add(currRow);
		}
		System.out.println("Actions: ");
		System.out.println(output.toString());
		System.out.println("Stitches:");
		for (ArrayList<Stitch> row : stitchRows) {
			System.out.println("Row: ");
			for (Stitch s : row) {
				System.out.println(s.toString());
			}
		}

	}
}
