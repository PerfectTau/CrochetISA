import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Arrays;

public class Parser {
    File file;
    ArrayList<ArrayList<String>> rows;
    boolean newStitches;

    public Parser(String filename) {
        file = new File(filename);
        ArrayList<String> fileLines = new ArrayList<String>();
        rows = new ArrayList<ArrayList<String>>();
        newStitches = false;

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                fileLines.add(scanner.nextLine());
            }
            scanner.close();
            for (int i = 0; i < fileLines.size(); i++) {
                String line = fileLines.get(i);
                if (!line.endsWith("turn") && i < fileLines.size() - 1) {
                    throw new IllegalArgumentException(
                            "Error: non-final lines must end with 'turn': Line " + i + ": " + line);
                }
                // TODO: add user defined stitch capability
                line = line.trim();
                line = line.replace(" ", "");
                line = line.toLowerCase();
                ArrayList<String> tokens = new ArrayList<String>(Arrays.asList(line.split(",")));
                Pattern pattern = Pattern.compile("^(\\d?)(\\[)(.*)");
                for (int j = 0; j < tokens.size(); j++) {
                    String token = tokens.get(j);
                    if (token.equals("turn") && j != tokens.size() - 1) {
                        throw new IllegalArgumentException("Error: 'turn' must be the last token on a line: " + line);
                    }
                    Matcher matcher = pattern.matcher(token);
                    // if(matcher.matches()){
                    // // Process the matched groups
                    // String count = matcher.group(1);
                    // String stitch = matcher.group(2);
                    // int blockCount = Integer.parseInt(count);
                    // //check if block repeat
                    // ArrayList<String> stitches = new ArrayList<String>();
                    if (matcher.matches()) {
                        String count = matcher.group(1);
                        String stitch = matcher.group(3);
                        int blockCount = 1;
                        if (count.length() < 1)
                            System.out.println("WARNING: Block repeats are typically repeated (e.g 4[sc, 3ch, sc]) (row: " + i + ")");
                        else
                            blockCount = Integer.parseInt(count);
                        ArrayList<String> stitches = new ArrayList<String>();
                        // String firstStitch = stitch.substring(1);
                        if (!stitch.matches(".*]$")) {
                            int index = checkMultiple(stitch, stitches, 0);
                            if (index == 0) // add
                                stitches.add(stitch);
                            tokens.remove(token);
                            // j++;
                            if (j < tokens.size()) {
                                token = tokens.get(j);
                                while (!token.matches(".*]$")) {
                                    index = checkMultiple(token, stitches, j);
                                    if (index == j) { // if the index stayed the same, then the stitch needs to be added
                                        stitches.add(token);
                                        tokens.remove(token);
                                    }
                                    token = tokens.get(j);
                                }
                                // add last token minus ']'
                                String lastStitch = token.substring(0, token.length()-1);
                                int prevSize = stitches.size();
                                index = checkMultiple(lastStitch, stitches, stitches.size());
                                if(stitches.size() == prevSize)
                                    stitches.add(lastStitch);
                                //stitches.add(token.substring(0, token.length() - 1));
                                tokens.remove(token);
                            }
                        } else{
                            System.out.println("WARNING: Block repeats ([]) typically include multiple stitches (row " + i + ")");
                            stitch = stitch.substring(0, stitch.length()-1);
                            int index = checkMultiple(stitch, stitches, 0);
                            if(index == 0)
                                stitches.add(stitch);
                            tokens.remove(token);
                        }
                        // add count number of stitches array
                        for (int k = 0; k < blockCount; k++) {
                            for (int a = 0; a < stitches.size(); a++) {
                                tokens.add(j, stitches.get(a));
                                j++;
                            }
                        }
                    } else if (token.matches("^\\d+.*"))
                        j = checkMultiple(token, tokens, j);
                    // }
                }
                rows.add(tokens);
                System.out.println("Row " + i + ": " + tokens.toString());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
            e.printStackTrace();
        }
    }

    public ArrayList<ArrayList<String>> getRows() {
        return rows;
    }

    public boolean newStitches() {
        return newStitches;
    }

    private int checkMultiple(String token, ArrayList<String> tokens, int index) {
        Pattern pattern = Pattern.compile("^(\\d+)(.*)");
        Matcher matcher = pattern.matcher(token);
        if (matcher.matches()) {
            // Process the matched groups
            String count = matcher.group(1);
            String stitch = matcher.group(2);
            int countInt = Integer.parseInt(count);
            for (int k = 0; k < countInt; k++) {
                tokens.add(index, stitch);
                index++;
            }
            index--;
            tokens.remove(token);
        }
        return index;
    }
}