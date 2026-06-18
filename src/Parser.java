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

    public Parser(String filename){
        file = new File(filename);
        ArrayList<String> fileLines = new ArrayList<String>();
        rows = new ArrayList<ArrayList<String>>();
        newStitches = false;

        try{
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine())
            {
                fileLines.add(scanner.nextLine());
            }
            scanner.close();
            int j = 0;
            for(String line : fileLines){
                // TODO: add user defined stitch capability
                line = line.trim();
                line = line.replace(" ", "");
                line = line.toLowerCase();
                ArrayList<String> tokens = new ArrayList<String>(Arrays.asList(line.split(",")));
                Pattern pattern = Pattern.compile("^(\\d+)(.*)");
                for(int i = 0; i < tokens.size(); i++){
                    String token = tokens.get(i);
                    Matcher matcher = pattern.matcher(token);
                    if(matcher.matches()){
                        // Process the matched groups
                        String count = matcher.group(1);
                        String stitch = matcher.group(2);
                        int countInt = Integer.parseInt(count);
                        for(int k = 0; k < countInt; k++){
                            tokens.add(i,stitch);
                            i++;
                        }
                        tokens.remove(token);
                    }
                }
                rows.add(tokens);
                System.out.println("Row " + j + ": " + tokens.toString());
                j++;
            }
        }
        catch(FileNotFoundException e){
            System.out.println("Error: File not found.");
            e.printStackTrace();
        }
    }

    public ArrayList<ArrayList<String>> getRows(){
        return rows;
    }

    public boolean newStitches(){
        return newStitches;
    }
}