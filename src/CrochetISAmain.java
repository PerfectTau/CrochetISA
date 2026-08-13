import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Processes a stitch level crochet pattern to action level and allows the user
 * to step through the action level instructions
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
		String[] ss = { INSERT, YO, PT, PT };
		String[] treble = { YO, YO, INSERT, YO, PT, YO, PT, PT, YO, PT, PT, "decrease", YO, PT, PT };
		String[] dtr = { YO, YO, YO, INSERT, YO, PT, YO, PT, PT, YO, PT, PT, YO, PT, PT, "decrease", YO, PT, PT };

		ArrayList<String> CH = new ArrayList<String>(Arrays.asList(ch));
		ArrayList<String> SC = new ArrayList<String>(Arrays.asList(sc));
		ArrayList<String> HDC = new ArrayList<String>(Arrays.asList(hdc));
		ArrayList<String> DC = new ArrayList<String>(Arrays.asList(dc));
		ArrayList<String> SS = new ArrayList<String>(Arrays.asList(ss));
		ArrayList<String> TR = new ArrayList<String>(Arrays.asList(treble));
		ArrayList<String> DTR = new ArrayList<String>(Arrays.asList(dtr));

		ArrayList<String> sk = new ArrayList<String>();
		sk.add(SK);
		ArrayList<String> turn = new ArrayList<String>();
		turn.add(TURN);

		// create stitch hash map
		HashMap<String, ArrayList<String>> stitchMap = new HashMap<String, ArrayList<String>>();
		stitchMap.put("turn", turn);
		stitchMap.put("ch", CH);
		stitchMap.put("ss", SS);
		stitchMap.put("sc", SC);
		stitchMap.put("hdc", HDC);
		stitchMap.put("dc", DC);
		stitchMap.put("tr", TR);
		stitchMap.put("dtr", DTR);
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
		stitchMap.put("ssfl", SS);
		stitchMap.put("ssbl", SS);
		stitchMap.put("fpss", SS);
		stitchMap.put("bpss", SS);
		stitchMap.put("trfl", TR);
		stitchMap.put("trbl", TR);
		stitchMap.put("fptr", TR);
		stitchMap.put("bptr", TR);
		stitchMap.put("dtrfl", DTR);
		stitchMap.put("dtrbl", DTR);
		stitchMap.put("fpdtr", DTR);
		stitchMap.put("bpdtr", DTR);
		stitchMap.put("sk", sk);

		StringBuilder output = new StringBuilder();
		ArrayList<String> actionsList = new ArrayList<String>();
		Scanner scanner = new Scanner(System.in);
		String filename;
		if (args.length == 1)
			filename = args[0];
		else {
			System.out.println("Enter the file path to pattern: ");
			filename = scanner.nextLine();
		}
		filename = filename.replaceAll("\"", "");
		Parser parser = new Parser(filename);
		ArrayList<ArrayList<Stitch>> stitchRows = new ArrayList<ArrayList<Stitch>>();
		String attachPoint = TOP;
		int index = 0;
		// int rowIndex = 0;
		Stitch nextConnection = null;
		twoItems nextConnectionItem = null;
		int attach;
		int connectionIndex = 0;

		// Parse initial strings to action values
		if (parser.newStitches) {
			// implement this functionality
		}
		ArrayList<ArrayList<String>> rows = parser.getRows();
		int currentLoops = 0;

		// Process first row (must be chain stitches)
		ArrayList<Stitch> firstRow = new ArrayList<Stitch>();
		for (int i = 0; i < rows.get(0).size(); i++) {
			String stitch = rows.get(0).get(i);
			// Process the first row
			if (!stitch.equals("ch") && !stitch.equals("turn")) {
				scanner.close();
				throw new IllegalArgumentException("Invalid stitch in first row: " + stitch);
			}
			Stitch currentStitch = new Stitch(0, index, stitch);
			if (stitchMap.containsKey(stitch)) {
				ArrayList<String> actions = stitchMap.get(stitch);
				currentStitch.setActions(actions);
				for (String action : actions) {
					if (action.equals(YO) || action.equals(INSERT)) {
						currentLoops++;
					}
					if (action.equals(PT)) {
						currentLoops--;
					}

					if (action.equals(INSERT)) {
						output.append(attachPoint + ", ");
						actionsList.add(attachPoint);
						continue;
					}
					output.append(action + ", ");
					actionsList.add(action);
					if (action.equals(TURN)) {
						// deal with stitch row
						nextConnection = firstRow.get(i - 1);
						nextConnectionItem = new twoItems(nextConnection.row, nextConnection.index);
						connectionIndex = i - 1;
						output.append("\n");
					}

				}
				if (!stitch.equals(TURN))
					firstRow.add(currentStitch);
			} else {
				scanner.close();
				throw new IllegalArgumentException("Unrecognized stitch: " + stitch);
			}
			index++;
		}
		stitchRows.add(firstRow);

		// Process rest of the pattern
		for (int z = 1; z < rows.size(); z++) {
			ArrayList<String> row = rows.get(z);
			ArrayList<Stitch> currRow = new ArrayList<Stitch>();
			ArrayList<Stitch> prevRow = stitchRows.get(z - 1);
			index = 0;
			connectionIndex = prevRow.size() - 1;
			if (connectionIndex != nextConnection.getIndex()) {
				connectionIndex = nextConnection.getIndex();
			}
			boolean endOfRow = false;
			boolean firstStitchCH = true;
			String firstStitch = row.get(0);
			Pattern incDecPattern = Pattern.compile("^(.*)(\\d+)(tog|inc)$");
			Matcher firstStitchMatcher = incDecPattern.matcher(firstStitch);
			if (firstStitchMatcher.matches()) {
				firstStitch = firstStitchMatcher.group(1);
			}
			ArrayList<String> firstStitchActions = new ArrayList<String>();
			if (stitchMap.containsKey(firstStitch))
				firstStitchActions = stitchMap.get(firstStitch);
			int height = findStitchHeight(new Stitch(0, 0, firstStitch), firstStitchActions);
			// System.out.println("Stitch: " + firstStitch + " Height: " + height);
			if (!firstStitch.equals("ch")) {
				firstStitchCH = false;
				for (int t = 1; t <= height; t++) {
					int insertStitchIndex = prevRow.size() - t;
					if (insertStitchIndex < 0) {
						scanner.close();
						throw new IllegalArgumentException(
								"Must have a turning chain of at least " + height + " stitches");
					}
					Stitch prevStitch = prevRow.get(insertStitchIndex);
					output.append(SK + ", ");
					actionsList.add(SK);
					if (!prevStitch.getType().equals("ch")) {
						scanner.close();
						throw new IllegalArgumentException(
								"Must have a turning chain of at least " + height + " stitches");
					}
				}
				connectionIndex = prevRow.size() - height - 1;
				nextConnection = prevRow.get(connectionIndex);
				nextConnectionItem = new twoItems(nextConnection.row, nextConnection.index);
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

				Matcher matcher = incDecPattern.matcher(stitch);
				ArrayList<String> actions = new ArrayList<String>();
				if (matcher.matches()) {
					String stitchType = matcher.group(1);
					int count = Integer.parseInt(matcher.group(2));
					String operation = matcher.group(3);
					System.out.println("Increase/Decrease: " + count + " " + stitchType + " " + operation);
					if (stitchMap.containsKey(stitchType)) {
						actions = stitchMap.get(stitchType);
						currentStitch.setActions(actions);
						height = findStitchHeight(currentStitch, actions);
						// System.out.println("Stitch: " + currentStitch + " Height: " + height);
						currentStitch.setHeight(height);
					} else {
						scanner.close();
						throw new IllegalArgumentException("Unrecognized stitch: " + stitch);
					}

					// Increase
					if (operation.equals("inc")) {
						// actions = stitchMap.get(stitchType);
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
										actionsList.add(PT);
									}
									currentLoops = 0;
									continue;
								}
								if (action.equals(INSERT)) {
									output.append(attachPoint + ", ");
									actionsList.add(attachPoint);
									continue;
								}
								if (action.equals("decrease")) {
									continue;
								}
								output.append(action + ", ");
								actionsList.add(action);
							}
							Stitch newStitch = new Stitch(z, index, stitch);
							newStitch.setActions(actions);
							newStitch.addConnection(nextConnectionItem);
							index++;
							currRow.add(newStitch);
						}
						index--;
						output.append(MOVE + ", ");
						actionsList.add(MOVE);
					} else {
						// Decrease
						currentStitch.addConnection(nextConnectionItem);
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
									actionsList.add(attachPoint);
									continue;
								}
								output.append(action + ", ");
								actionsList.add(action);
							}
							if (i < count - 1) {
								output.append(MOVE + ", ");
								actionsList.add(MOVE);
							}
							if (i < count - 1) {
								connectionIndex--;
								nextConnection = prevRow.get(connectionIndex);
								nextConnectionItem = new twoItems(nextConnection.row, nextConnection.index);
								currentStitch.addConnection(nextConnectionItem);
							}
						}

						// Finish decrease (YO, PT_ALL)
						output.append(YO + ", ");
						actionsList.add(YO);
						for (int k = 0; k < currentLoops + 1; k++) {
							output.append(PT + ", ");
							actionsList.add(PT);
						}
						currRow.add(currentStitch);

					}
				}
				// Standard (non-increase/decrease) Stitch processing
				else if (stitchMap.containsKey(stitch)) {
					actions = stitchMap.get(stitch);
					currentStitch.setActions(actions);
					height = findStitchHeight(currentStitch, actions);
					currentStitch.setHeight(height);
					// check if first non-chain stitch in row
					if (firstStitchCH) {
						boolean firstNonChain = true;
						int chCount = 0;
						for (Stitch rowStitch : currRow) {
							if (!rowStitch.getType().equals("ch"))
								firstNonChain = false;
							else
								chCount++;
						}
						if (firstNonChain && !stitch.equals("ch") && !stitch.equals(TURN) && !stitch.equals(SK)) {
							if (chCount < height) {
								scanner.close();
								throw new IllegalArgumentException(
										"Must have a turning chain of at least " + height + " on row " + z);
							} else if (chCount > height + 1) {
								scanner.close();
								throw new IllegalArgumentException("Turning chain is greater than stitch height on row "
										+ z + ". Did you mean to add a turn?");
							}
						}
					}

					// System.out.println("Stitch: " + currentStitch + "Height: "+ height);
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
								actionsList.add(PT);
							}
							currentLoops = 0;
							continue;
						}
						if (action.equals(INSERT)) {
							output.append(attachPoint + ", ");
							actionsList.add(attachPoint);
							continue;
						}
						if (action.equals(SK)) {
							connectionIndex--;
						}
						output.append(action + ", ");
						actionsList.add(action);
						if (action.equals(TURN)) {
							// deal with stitch row
							nextConnection = currRow.get(index - 1);
							nextConnectionItem = new twoItems(nextConnection.row, nextConnection.index);
							output.append("\n");
							endOfRow = true;
						}

					}
					if (!stitch.equals(TURN)) {
						if (!stitch.equals("ch")) {
							output.append(MOVE + ", ");
							actionsList.add(MOVE);
							currentStitch.addConnection(nextConnectionItem);
						}
						currRow.add(currentStitch);
					}
				} else {
					scanner.close();
					throw new IllegalArgumentException("Unrecognized stitch: " + stitch);
				}
				index++;
				if (connectionIndex < 0) {
					scanner.close();
					throw new IllegalArgumentException("Row " + z + " is too long. You may be missing increases.");
				}
				if (s < row.size() - 1) {
					String nextStitch = row.get(s + 1);
					if (!(connectionIndex == 0 && stitch.equals("ch"))) {
						if (connectionIndex - 1 < 0
								&& !(nextStitch.equals("ch") || nextStitch.equals(TURN) || nextStitch.equals(SK))) {
							System.out.println("Current Stitch: " + stitch + ", Next Stitch: " + nextStitch);
							scanner.close();
							throw new IllegalArgumentException(
									"Row " + z + " is too long. You may be missing increases.");
						}
					}
					if (!stitch.equals("ch") && !endOfRow
							&& !(row.get(s + 1).equals(TURN) || row.get(s + 1).equals("ch"))) {
						connectionIndex--;
						nextConnection = prevRow.get(connectionIndex);
						nextConnectionItem = new twoItems(nextConnection.row, nextConnection.index);
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
		System.out.println();
		stepThroughPattern(actionsList, scanner);
		// drawGraph graph = new drawGraph(stitchRows);
		// graph.paintComponent(graph.getGraphics());
		scanner.close();

	}

	/**
	 * Handles Console Loop Level visualization program
	 * 
	 * @param actions   the list of actions for the pattern to be stepped through
	 * @param inputScan a scanner connected to System.in for accepting user commands
	 */
	private static void stepThroughPattern(ArrayList<String> actions, Scanner inputScan) {
		loopLogic logic = new loopLogic(actions);
		boolean done = false;
		System.out.println("Press 'h' for help");
		System.out.println("To process next action press 'n' ");
		System.out.println("To go back one action press 'b' ");
		System.out.println("To exit type 'exit' ");

		while (true) {
			// print current action
			int actionIndex = logic.getActionIndex();
			String currAction = "";

			ArrayList<ArrayList<Loop>> loops = logic.getLoops();
			for (int i = 0; i < loops.size(); i++) {
				ArrayList<Loop> row = loops.get(i);
				System.out.println("Row " + i + ": " + row.toString());
			}
			System.out.println("Row " + loops.size() + ": " + logic.getCurrRow());
			System.out.println("Hook: " + logic.getHook());
			if (logic.constructingTop())
				System.out.println("Constructing Top");
			else
				System.out.println("Constructing Post");
			if (actionIndex >= actions.size()) {
				if (done)
					System.out.println("Done.");
				else {
					System.out.println("Processing Final Action");
					done = true;
				}
			} else {
				currAction = actions.get(actionIndex);
				System.out.println("Next Action: " + currAction);
			}
			String nextLine = inputScan.next();

			if (nextLine.equals("N") || nextLine.equals("n")) {
				if (done)
					System.out.println("Done processing Pattern.");
				else
					logic.processNextAction();
			} else if (nextLine.equals("B") || nextLine.equals("b")) {
				if (actionIndex > 0)
					logic.undoLastAction();
				else
					System.out.println("Unable to undo initialization.");
			} else if (nextLine.equals("h") || nextLine.equals("H")) {
				System.out.println("To process next action press 'n' ");
				System.out.println("To go back one action press 'b' ");
				System.out.println("To exit type 'exit' ");
			}
			if (nextLine.equals("exit"))
				break;
		}
	}

	/**
	 * Finds the height of the given stitch
	 * 
	 * @param stitch        the stitch to be assigned a height
	 * @param stitchActions the actions associated with the given stitch
	 * @return the post height of the given stitch
	 */
	private static int findStitchHeight(Stitch stitch, ArrayList<String> stitchActions) {
		// get height from repeated yo, pt, pt
		// ins, yo, pt = height 1
		int currHeight = stitch.getHeight();
		ArrayList<String> actionCopy = new ArrayList<String>(stitchActions);
		String[] insertSubList = { INSERT, YO, PT };
		String[] subList = { YO, PT, PT };
		if (stitchActions.size() == 0) {
			throw new IllegalArgumentException("Stitch " + stitch.getType() + " has no actions associated with it.");
		}
		if (stitchActions.get(0).equals(YO) && !stitch.getType().equals("ch"))
			stitch.setBetween(true);
		int index = Collections.indexOfSubList(actionCopy, Arrays.asList(insertSubList));
		if (index != -1) {
			// contains insert, yo, pt
			currHeight++;
			for (int i = 0; i < 3; i++) {
				actionCopy.remove(index);
				// insert++;
			}
		}

		index = Collections.indexOfSubList(actionCopy, Arrays.asList(subList));
		int counter = 0;
		while (index != -1) {
			currHeight++;
			for (int i = 0; i < 3; i++) {
				actionCopy.remove(index);
				// index++;
			}
			index = Collections.indexOfSubList(actionCopy, Arrays.asList(subList));
			counter++;
		}
		if (counter > 1)
			stitch.setBetween(true);
		currHeight--;
		stitch.setHeight(currHeight);
		return currHeight;
	}
}
