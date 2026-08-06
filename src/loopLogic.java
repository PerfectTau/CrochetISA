import java.util.ArrayList;
import java.util.Stack;

public class loopLogic {
    ArrayList<String> actions;
    ArrayList<ArrayList<loop>> loops;
    ArrayList<Post> posts;
    ArrayList<loop> currRow;
    Post currPost;
    Stack<loop> hookLoops;
    twoItems nextConnection;
    int actionIndex;
    int loopCount;
    int stitchCount;
    int row;
    int betweenCounter;
    boolean insertedLast;
    boolean constructingTop;
    boolean between;

    public loopLogic(ArrayList<String> actions){
        this.actions = actions;
        loops = new ArrayList<ArrayList<loop>>();
        posts = new ArrayList<Post>();
        hookLoops = new Stack<loop>();
        hookLoops.add(new loop(0, 0));
        currRow = new ArrayList<loop>();
        currPost = new Post(0);
        actionIndex = 0;
        stitchCount = 0;
        loopCount = 0;
        row = 0;
        betweenCounter = 2;
        insertedLast = false;
        constructingTop = true;
        between = false;
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

    public boolean constructingTop(){
        return constructingTop;
    }

    public void processNextAction(){
        if(actionIndex >= actions.size()){
            System.out.println("No more actions to process.");
            return;
        }
        String action = actions.get(actionIndex);
        if(action.equals("yo")){    // yarn over: adds a loop to hook
            loopCount++;
            loop newLoop = new loop(loopCount, stitchCount);
            // add connection to previous loop
            newLoop.addConnection(loopCount - 1);
            hookLoops.push(newLoop);
        }
        else if(action.equals("pt")){   // pull through
            // remove the second to last loop from the hookLoops list and adds it to loops list
            loop lastLoop = hookLoops.pop();
            loop removedLoop = hookLoops.pop();
            if(hookLoops.size() == 0){
                // finishes post construction, back to working on top
                removedLoop.toggleTop();
                stitchCount++;
                posts.add(currPost);
                currPost = new Post(stitchCount);
                lastLoop.setStitchID(stitchCount);
                constructingTop = true;
            }
            if(!insertedLast){
                currRow.add(removedLoop);
                if(!constructingTop)
                    currPost.addLoop(removedLoop);
            }
            // if latest loop is not connected to removed loop, connect them
            int removedLoopID = removedLoop.getID();
            if(lastLoop.getConnections().contains(removedLoopID) == false){
                lastLoop.addConnection(removedLoopID);
            }
            hookLoops.push(lastLoop);
            insertedLast = false;

            //check if you finished a between space
            if(between && hookLoops.size() == betweenCounter){
                System.out.println("Between Attach Point Completed");
                between = false;
            }
        }
        else if(action.equals("insert top") || action.equals("insert front loop") || action.equals("insert back loop")){                                                                                                                                                                                                                                                          
            // add previous row's correct loop to hookLoops list
            //begins work on post
            constructingTop = false;
            
            ArrayList<loop> previousRow = loops.get(nextConnection.getRow());
            int connectionIndex = nextConnection.getIndex();

            //check if this stitch should create a post
            setBetween();

            for(int i = connectionIndex; i >= 0; i--){
                loop l = previousRow.get(i);
                if(l.isTop()){
                    hookLoops.push(l);
                    break;
                }
            }
            insertedLast = true;
            // posts.add(currPost);
            // currPost = new Post(stitchCount);
        }
        else if(action.equals("insert front post") || action.equals("insert back post")){
            //get next connection's stitch's post
            int connectionIndex = nextConnection.getIndex();
            loop nextLoop = loops.get(nextConnection.getRow()).get(connectionIndex);
            int nextStitchID = nextLoop.getStitch();
            Post nextPost = posts.get(nextStitchID);

            //check if this stitch should create a post/between attach point
            setBetween();

            if(nextPost.size() > 0){
                // valid attach point, add to hookLoops
                // TODO: figure out better way to add posts to hook
                ArrayList<loop> postLoops = nextPost.getLoops();
                hookLoops.push(postLoops.get(postLoops.size() - 1));
            }
            else
                throw new IllegalArgumentException("Cannot insert around post: Post does not exist");
            insertedLast = true;
            // posts.add(currPost);
            // currPost = new Post(stitchCount);
        }
        else if(action.equals("move")){     // increments next connection
            ArrayList<loop> previousRow = loops.get(nextConnection.getRow());
            int connectionIndex = nextConnection.getIndex();

            for(int i = connectionIndex; i >= 0; i--){
                loop l = previousRow.get(i);
                if(l.isTop()){
                    break;
                }
                connectionIndex--;
            }
            connectionIndex--;
            nextConnection.setIndex(connectionIndex);
        }
        else if(action.equals("turn")){     // begins a new row 
            loops.add(currRow);
            nextConnection = new twoItems(row, currRow.size() - 1);
            currRow = new ArrayList<loop>();
            row++;
        }
        else if(action.equals("skip")){
            int currIndex = nextConnection.getIndex();
            nextConnection.setIndex(currIndex - 1);
        }
        else{}
        actionIndex++;
    }


    /**
     * Undoes the last action, if possible
     * Must check validity of undoing last action before calling
     **/
    public void undoLastAction(){
        actionIndex--;
        String lastAction = actions.get(actionIndex);
        if(lastAction.equals("yo")){    // yarn over: adds a loop to hook
            loopCount--;
            hookLoops.pop();
        }
        else if(lastAction.equals("pt")){   // pull through
            // remove the second to last loop from the hookLoops list and adds it to loops list
            // add last loop from loops to second to last position in hookLoops

            //check if you finished a between space
            if(!between && hookLoops.size() == betweenCounter - 1){
                //System.out.println("Between Attach Point Completed");
                between = true;
            }

            loop lastLoop = hookLoops.pop();
            loop removedLoop = null;
            ArrayList<Integer> connections = lastLoop.getConnections();
            int lastID = connections.get(connections.size()-1);
            if(currRow.size() <= 0){
                removedLoop = getLastInsertedLoop(lastID);
                if(removedLoop.equals(null))
                    throw new IllegalArgumentException("Couldn't find last loop");
            }
            else{
                removedLoop = currRow.get(currRow.size()-1);
            }
            if(removedLoop.getID() != lastID){
                removedLoop = getLastInsertedLoop(lastID);
                if(removedLoop.equals(null))
                    throw new IllegalArgumentException("Couldn't find last loop");
                
            }
            else{
                //removed removedLoop from currRow
                currRow.remove(removedLoop);
            }
            
            if(hookLoops.size() == 0){
                removedLoop.toggleTop();
                stitchCount--;
                lastLoop.setStitchID(stitchCount-1);
                constructingTop = false;
            }

            // if latest loop is not connected to removed loop, connect them
            int removedLoopID = removedLoop.getID();
            if(lastLoop.getConnections().contains(removedLoopID)){
                lastLoop.removeConnection(removedLoopID);
            }
            if(!removedLoop.equals(null))
                hookLoops.push(removedLoop);
            else{
                System.out.println("Cannot undo last action: Removed Loop is NULL");
                return;
            }
            hookLoops.push(lastLoop);
        }
        else if(lastAction.equals("insert top") || lastAction.equals("insert front loop") || lastAction.equals("insert back loop")){                                                                                                                                                                                                                                                          
            // add previous row's correct loop to hookLoops list
            ArrayList<loop> previousRow = loops.get(nextConnection.getRow());
            int connectionIndex = nextConnection.getIndex();

             //check if this stitch should create a between attach point
            

            for(int i = connectionIndex; i >= 0; i--){
                loop l = previousRow.get(i);
                if(l.isTop()){
                    loop topLoop = hookLoops.pop();
                    if(!topLoop.equals(l)){
                        throw new IllegalArgumentException("Popped loop doesn't match expected value."); 
                        // System.out.println("Popped loop doesn't match expected value.");
                        // return;
                    }
                    //hookLoops.push(l);
                    break;
                }
            }
            insertedLast = false;
            constructingTop = true; 
        }
        else if(lastAction.equals("insert front post") || lastAction.equals("insert back post")){
            // check if there are two valid between spaces

        }
        else if(lastAction.equals("move")){     // increments next connection
            ArrayList<loop> previousRow = loops.get(nextConnection.getRow());
            int connectionIndex = nextConnection.getIndex();

            for(int i = connectionIndex + 1; i < previousRow.size(); i++){
                loop l = previousRow.get(i);
                if(l.isTop()){
                    break;
                }
                connectionIndex++;
            }
            connectionIndex++;
            nextConnection.setIndex(connectionIndex);
        }
        else if(lastAction.equals("turn")){     // begins a new row
            currRow = loops.get(loops.size()-1);
            loops.remove(loops.size()-1); 
            row--;
            nextConnection = new twoItems(row, 0);
        }
        else if(lastAction.equals("skip")){
            int currIndex = nextConnection.getIndex();
            nextConnection.setIndex(currIndex + 1);
        }
        else{}
    }

    /**
     * Gets the last loop that the pattern inserted into using its ID
     * @param lastID the ID of the loop to find
     * @return the loop that was last inserted into
     */
    private loop getLastInsertedLoop(int lastID){
        ArrayList<loop> lastRow = loops.get(loops.size()-1);
                for(int i = lastRow.size()-1; i >= 0; i--){
                    loop currLoop = lastRow.get(i);
                    if(currLoop.getID() == lastID){
                        insertedLast = true;
                        return currLoop;
                    }
                }
                insertedLast = true;
                return null;
    }

    /**
     * Checks if previous action was a yarn over to see if a between attach point should be created for the current stitch
     */
    private void setBetween(){
        if(actionIndex > 0){
                if(actions.get(actionIndex - 1).equals("yo")){
                    between = false;
                    int tempIndex = actionIndex - 1;
                    String tempAction = actions.get(tempIndex);
                    int tempLoopCount = hookLoops.size();
                    while(tempAction.equals("yo")){
                        tempIndex--;
                        tempAction = actions.get(tempIndex);
                        tempLoopCount--;
                    }
                    if(tempLoopCount > 1)
                        betweenCounter--;
                    else
                        betweenCounter = 2;
                }
                else{
                    between = false;
                    betweenCounter = 2;
                }
            }
    }
}