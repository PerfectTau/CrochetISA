import java.io.File;
import java.io.FileNotFoundException; 
import java.util.Scanner;
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
            int i = 0;
            for(String line : fileLines){
                // TODO: add user defined stitch capability
                line = line.trim();
                line = line.replace(" ", "");
                line = line.toLowerCase();
                ArrayList<String> tokens = new ArrayList<String>(Arrays.asList(line.split(",")));
                rows.add(tokens);
                System.out.println("Row " + i + ": " + tokens.toString());
                i++;
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