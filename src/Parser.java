import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Parser {
    File file;
    ArrayList<ArrayList<String>> rows;
    boolean newStitches;
    HashMap<String, ArrayList<String>> newStitchMap = new HashMap<String, ArrayList<String>>();

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
               
                line = line.trim();
                line = line.replace(" ", "");
                if(line.length() < 1 || line.startsWith("#")) {
                    continue;
                }
                line = line.toLowerCase();
                ArrayList<String> tokens = new ArrayList<String>(Arrays.asList(line.split(",")));
                Pattern pattern = Pattern.compile("^(\\d?)(\\[)(.*)");
                boolean newStitchFound = false;
                for (int j = 0; j < tokens.size(); j++) {
                    String token = tokens.get(j);
                    Pattern newStitchPattern = Pattern.compile("^(new)(.*)(=)(.*)");
                    Matcher newStitchMatcher = newStitchPattern.matcher(token);
                    if(newStitchMatcher.matches()) {
                        // next token is the name of the new stitch, and the following tokens are the actions for that stitch
                        String newStitchName = newStitchMatcher.group(2);
                        String newStitchActionsString = newStitchMatcher.group(4);
                        ArrayList<String> newStitchActions = new ArrayList<String>(Arrays.asList(newStitchActionsString.split(",")));
                        for(int k = j+1; k < tokens.size(); k++){
                            newStitchActions.add(tokens.get(k));
                        }
                        checkNewStitchValidity(newStitchActions);
                        newStitchMap.put(newStitchName, newStitchActions);
                        newStitchFound = true;
                        break;
                    }
                    if (token.equals("turn") && j != tokens.size() - 1) {
                        throw new IllegalArgumentException("Error: 'turn' must be the last token on a line: " + line);
                    }
                    Matcher matcher = pattern.matcher(token);
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
                            if (index == 0) // didn't change index, so needs to be added
                                stitches.add(stitch);
                            tokens.remove(token);
                            
                            if (j < tokens.size()) {
                                token = tokens.get(j);
                                while (!token.matches(".*]$")) {
                                    index = checkMultiple(token, stitches, j);
                                    if (index == j) { // if the index stayed the same, then the stitch needs to be added
                                        stitches.add(token);
                                    }
                                    tokens.remove(token);
                                    token = tokens.get(j);
                                }
                                // add last token minus ']'
                                String lastStitch = token.substring(0, token.length()-1);
                                int prevSize = stitches.size();
                                index = checkMultiple(lastStitch, stitches, stitches.size());
                                if(stitches.size() == prevSize)
                                    stitches.add(lastStitch);
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
                        j--;
                    } else {
                        j = checkMultiple(token, tokens, j);
                    }
                }
                if(!newStitchFound){
                rows.add(tokens);
                System.out.println("Row " + i + ": " + tokens.toString());
                 if (!line.endsWith("turn") && i < fileLines.size() - 1) {
                    throw new IllegalArgumentException(
                            "Error: non-final lines must end with 'turn': Line " + i + ": " + line);
                }
            }
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

    public HashMap<String, ArrayList<String>> getNewStitches() {
        return newStitchMap;
    }

    private int checkMultiple(String token, ArrayList<String> tokens, int index) {
        Pattern pattern = Pattern.compile("^(\\d+)(.*)");
        Pattern beginningCurly = Pattern.compile("^\\{.*");
        boolean begCurl = false;
        Pattern endCurly = Pattern.compile(".*\\}$");
        boolean endCurl = false;
        Matcher matcher = pattern.matcher(token);
        Matcher beginning = beginningCurly.matcher(token);
        Matcher ending = endCurly.matcher(token);
        begCurl = beginning.matches();
        endCurl = ending.matches();
        int startIndex = index;
        String startToken = token;
        if(begCurl){
            token = token.substring(1);
            matcher = pattern.matcher(token);
        }

        if (matcher.matches()) {
            // Process the matched groups
            String count = matcher.group(1);
            String stitch = matcher.group(2);

            if(endCurl){
                stitch = stitch.substring(0, stitch.length() - 1);
            }
            int countInt = Integer.parseInt(count);
            for (int k = 0; k < countInt; k++) {
                tokens.add(index, stitch);
                index++;
            }
            index--;
            tokens.remove(startToken);
        }
        if(begCurl){
            //add beginning curly brace to first stitch
            if(tokens.size() > startIndex){
                String firstStitch = tokens.get(startIndex);
                if(!firstStitch.matches("^\\{.*")){
                    tokens.remove(index);
                    firstStitch = "{" + firstStitch;
                    tokens.add(startIndex, firstStitch);
                }
            }
        }
        if(endCurl){
            if(tokens.size() > index){
                String lastStitch = tokens.get(index);
                if(!lastStitch.matches(".*\\}$")){
                    tokens.remove(index);
                    lastStitch = lastStitch + "}";
                    tokens.add(index, lastStitch);
                }
            }
        }
        return index;
    }

    private void checkNewStitchValidity(ArrayList<String> actions) {
        int yoCount = 0;
        int insCount = 0;
        int ptCount = 0;
        String lastAction = "";
        for (String action : actions){
            if(action.equals("yo")){
                yoCount++;
            } else if(action.equals("insert") || action.equals("ins")){
                insCount++;
            } else if(action.equals("pt")){
                ptCount++;
                if(lastAction.equals("ins")){
                    throw new IllegalArgumentException("Error in stitch definition: Cannot pull through an attach point (ins, pt) is invalid. Did you mean (ins, yo, pt)?");
                }
            } else if(action.equals("sk")){

            } else{
                System.out.println("WARNING: Invalid action in stitch definition: " + action + ". Valid actions are: yo, ins, pt, sk");
            }
            lastAction = action;
        }
        if(ptCount < (insCount + yoCount)){
            System.out.println("yoCount: " + yoCount + ", insCount: " + insCount + ", ptCount: " + ptCount);
            throw new IllegalArgumentException("Error in stitch definition: Not enough pull through instructions to finish stitch. pt must be equals to ins + yo");
        }
        newStitches = true;
    }
}