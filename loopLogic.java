import java.util.ArrayList;
import java.util.Stack;

public class loopLogic {
    ArrayList<String> actions;
    ArrayList<ArrayList<loop>> loops;
    ArrayList<loop> currRow;
    Stack<loop> hookLoops;
    twoItems nextConnection;
    int actionIndex;
    int loopCount;
    int stitchCount;
    int row;
    boolean insertedLast;

    public loopLogic(ArrayList<String> actions){
        this.actions = actions;
        loops = new ArrayList<ArrayList<loop>>();
        hookLoops = new Stack<loop>();
        hookLoops.add(new loop(0, 0));
        currRow = new ArrayList<loop>();
        actionIndex = 0;
        stitchCount = 0;
        loopCount = 0;
        row = 0;
        insertedLast = false;
    }

    public int getActionIndex(){
        return actionIndex;
    }

    public ArrayList<ArrayList<loop>> getLoops(){
        return loops;
    }

    public Stack<loop> getHook(){
        return hookLoops;
    }

    public ArrayList<loop> getCurrRow(){
        return currRow;
    }

    public void processNextAction(){
        if(actionIndex >= actions.size()){
            System.out.println("No more actions to process.");
            return;
        }
        String action = actions.get(actionIndex);
        if(action.equals("yo")){
            //loops.add(new loop(actionIndex, stitchCount));
            loopCount++;
            loop newLoop = new loop(loopCount, stitchCount);
            newLoop.addConnection(hookLoops.peek());
            hookLoops.push(newLoop);
        }
        else if(action.equals("pt")){
            // remove the second to last loop from the hookLoops list and adds it to loops list
            loop lastLoop = hookLoops.pop();
            loop removedLoop = hookLoops.pop();
            if(hookLoops.size() == 0){
                removedLoop.top();
                stitchCount++;
                lastLoop.setStitchID(stitchCount);
                //each loop between this top and previous top should belong to the same stitch
                // for(int i = removedLoop.getID() - 1; i >= 0; i--){
                //     loop currentLoop = currRow.get(i);
                //     if(currentLoop.getStitch() == removedLoop.getStitch()){}
                // }
            }
            if(!insertedLast){
                currRow.add(removedLoop);
            }
            // if latest loop is not connected to removed loop, connect them
            if(lastLoop.getConnections().contains(removedLoop) == false){
                lastLoop.addConnection(removedLoop);
            }
            hookLoops.push(lastLoop);
            insertedLast = false;
        }
        else if(action.equals("insert top")){                                                                                                                                                                                                                                                          
            // add previous row's correct loop to hookLoops list
            ArrayList<loop> previousRow = loops.get(nextConnection.getRow());
            int connectionIndex = nextConnection.getIndex();

            for(int i = connectionIndex; i >= 0; i--){
                loop l = previousRow.get(i);
                if(l.isTop()){
                    hookLoops.push(l);
                    break;
                }
                connectionIndex--;
            }
            connectionIndex--;
            nextConnection.setIndex(connectionIndex);
            insertedLast = true;
        }
        else if(action.equals("move")){
            // if(hookLoops.size() == 1)
            //     stitchCount++;
        }
        else if(action.equals("turn")){
            loops.add(currRow);
            nextConnection = new twoItems(row, currRow.size() - 1);
            currRow = new ArrayList<loop>();
            row++;
        }
        else{}
        actionIndex++;
    }
}

// the last loop pushed off the hook to form a stitch is the top
