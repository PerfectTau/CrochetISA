import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class StitchDictionary {
    HashMap<String, ArrayList<String>> stitchMap;
    StitchDictionary(){
        stitchMap = new HashMap<>();
        // key stitches after stitch insertion
        String INSERT = "insert";
        String PT = "pt";
        String YO = "yo";
        String SK = "skip";
        String TURN = "turn"; 
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
		stitchMap.put("spsc", SC);
		stitchMap.put("hdcfl", HDC);
		stitchMap.put("hdcbl", HDC);
		stitchMap.put("fphdc", HDC);
		stitchMap.put("bphdc", HDC);
		stitchMap.put("sphdc", HDC);
		stitchMap.put("dcfl", DC);
		stitchMap.put("dcbl", DC);
		stitchMap.put("fpdc", DC);
		stitchMap.put("bpdc", DC);
		stitchMap.put("spdc", DC);
		stitchMap.put("ssfl", SS);
		stitchMap.put("ssbl", SS);
		stitchMap.put("fpss", SS);
		stitchMap.put("bpss", SS);
		stitchMap.put("spss", SS);
		stitchMap.put("trfl", TR);
		stitchMap.put("trbl", TR);
		stitchMap.put("fptr", TR);
		stitchMap.put("bptr", TR);
		stitchMap.put("sptr", TR);
		stitchMap.put("dtrfl", DTR);
		stitchMap.put("dtrbl", DTR);
		stitchMap.put("fpdtr", DTR);
		stitchMap.put("bpdtr", DTR);
		stitchMap.put("spdtr", DTR);
		stitchMap.put("sk", sk);
    }

    public boolean containsKey(String key){
        return stitchMap.containsKey(key);
    }

    public ArrayList<String> get(String key){
        return stitchMap.get(key);
    }

    public void addStitch(String key, ArrayList<String> actions){
        stitchMap.put(key, actions);
    }
}
