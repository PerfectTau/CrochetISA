import java.util.ArrayList;
import java.util.Stack;

public class loopLogic {
    ArrayList<String> actions;
    ArrayList<ArrayList<Loop>> loops;
    ArrayList<Post> posts;
    ArrayList<BetweenSpace> betweenSpaces;
    ArrayList<Loop> currRow;
    Post currPost;
    Loop nextTop;
    BetweenSpace currBetween;
    Stack<HookElement> hookLoops;
    twoItems nextConnection;
    int actionIndex;
    int loopCount;
    int stitchCount;
    int row;
    int betweenCounter;
    boolean insertedLast;
    boolean constructingTop;
    boolean post;

    public loopLogic(ArrayList<String> actions) {
        this.actions = actions;
        loops = new ArrayList<ArrayList<Loop>>();
        posts = new ArrayList<Post>();
        betweenSpaces = new ArrayList<BetweenSpace>();
        hookLoops = new Stack<HookElement>();
        hookLoops.add(new Loop(0, 0));
        currRow = new ArrayList<Loop>();
        currPost = new Post(0);
        currBetween = new BetweenSpace(0);
        nextTop = new Loop(0, 0);
        actionIndex = 0;
        stitchCount = 0;
        loopCount = 0;
        row = 0;
        betweenCounter = 2;
        insertedLast = false;
        constructingTop = true;
        post = false;
    }

    public int getActionIndex() {
        return actionIndex;
    }

    public ArrayList<ArrayList<Loop>> getLoops() {
        return loops;
    }

    public Stack<HookElement> getHook() {
        return hookLoops;
    }

    public ArrayList<Loop> getCurrRow() {
        return currRow;
    }

    public boolean constructingTop() {
        return constructingTop;
    }

    public void processNextAction() {
        if (actionIndex >= actions.size()) {
            System.out.println("No more actions to process.");
            return;
        }
        String action = actions.get(actionIndex);
        if (action.equals("yo")) { // yarn over: adds a loop to hook
            loopCount++;
            Loop newLoop = new Loop(loopCount, stitchCount);
            // add connection to previous loop
            int loopID = loopCount - 1;
            HookElement previousLoop = hookLoops.peek();
            if (previousLoop instanceof Loop && ((Loop) previousLoop).getID() == loopID) {
                newLoop.addConnection(previousLoop);
            } else {
                for (int i = currRow.size() - 1; i >= 0; i--) {
                    Loop l = currRow.get(i);
                    if (l.getID() == loopID) {
                        newLoop.addConnection(l);
                        break;
                    }
                }
                if (previousLoop instanceof Loop) {
                    if (((Loop) previousLoop).getID() != loopID) {
                        ArrayList<Loop> previousRow = loops.get(loops.size() - 1);
                        for (int i = previousRow.size() - 1; i >= 0; i--) {
                            Loop l = previousRow.get(i);
                            if (l.getID() == loopID) {
                                newLoop.addConnection(l);
                                break;
                            }
                        }
                    }
                }
            }
            hookLoops.push(newLoop);
        } else if (action.equals("pt")) { // pull through
            // remove the second to last loop from the hookLoops list and adds it to loops list
            HookElement lastLoop = hookLoops.pop();
            HookElement removedLoop = hookLoops.pop();
            if (hookLoops.size() == 0) {
                // finish current stitch
                if (removedLoop instanceof Loop){
                    ((Loop) removedLoop).toggleTop();
                    //check if all loops have been added to between space
                    if(currBetween.size() > 0){
                        int id = nextTop.getID() + 1;
                        Loop curLoop = null;
                        for(int i = currRow.size()-1; i >=0; i--){
                            curLoop = currRow.get(i);
                            if(curLoop.getID() == id)
                                break;
                        }
                        if(!(curLoop == null)){
                            if(curLoop.getID() == id && !currBetween.contains(curLoop))
                                currBetween.addLoop(curLoop);
                        }
                    }
                    nextTop = (Loop) lastLoop;
                }
                else
                    throw new IllegalArgumentException("Finishing hook element is not a loop.");
                stitchCount++;
                lastLoop.setStitchID(stitchCount);
                constructingTop = true;
            }
            if (!insertedLast && removedLoop instanceof Loop) {
                currRow.add((Loop) removedLoop);
                if (!constructingTop)
                    currPost.addLoop((Loop) removedLoop);
            }
            // if latest loop is not connected to removed loop, connect them
            if (lastLoop instanceof Loop) {
                if (((Loop)lastLoop).getConnections().contains(removedLoop) == false) {
                    ((Loop)lastLoop).addConnection(removedLoop);
                }
            }
            hookLoops.push(lastLoop);
            insertedLast = false;

            // check if you finished a between space
            if(post && hookLoops.size() == betweenCounter) {
                System.out.println("Between Attach Point Completed");
                //currentBetween = new BetweenSpace(stitchCount);
                //currentBetween.addLoop((Loop) removedLoop);
                post = false;
            }
        } else if (action.equals("insert top") || action.equals("insert front loop") || action.equals("insert back loop")) {
            // add previous row's correct loop to hookLoops list
            // begins work on post
            constructingTop = false;

            // check if this stitch should create a post
            setPost();
            System.out.println("CurrPost: " + currPost + "; CurrPost Loops: " + currPost.getLoops());
            savePost();

            //Check if a between space is created and add loops to be included
            saveAndSetBetween();

            Loop toAdd = findNextTop();
            if(toAdd != null)
                hookLoops.push(toAdd);
            else
                throw new IllegalArgumentException("Couldn't find next top");
            insertedLast = true;

        } else if (action.equals("insert front post") || action.equals("insert back post")) {
            constructingTop = false;

            //check if a between space is created
            saveAndSetBetween();

            // get next connection's stitch's post
            // find the next stitch (find next top)
            Loop top = findNextTop();
            int nextStitchID = top.getStitchID();
            Post nextPost = null;
            // check if this stitch should create a post/between attach point
            setPost();
            System.out.println("CurrPost: " + currPost + "; CurrPost Loops: " + currPost.getLoops());
            savePost();

            // find the first post in the list from the stitch to connect to
            for (Post post : posts){
                if(post.getStitchID() == nextStitchID){
                    nextPost = post;
                    break;
                }
            }

            if (nextPost.size() > 0) {
                hookLoops.push(nextPost);
            } else
                throw new IllegalArgumentException("Cannot insert around post: Post does not exist");

            insertedLast = true;
        } else if(action.equals("insert into between/chain space")) {
            constructingTop = false;
            setPost();
            savePost();
            saveAndSetBetween();
            Loop top = findNextTop();
            int nextID = top.getStitchID();
            BetweenSpace nextSpace = null;
            for(BetweenSpace space : betweenSpaces){
                if(space.getStitchID() == nextID){
                    nextSpace = space;
                    break;
                }
            }

            if(nextSpace == null)
                throw new IllegalArgumentException("Between Space does not exist for stitch " + nextID);
            if(nextSpace.size() > 0){
                hookLoops.push(nextSpace);
            } else{
                throw new IllegalArgumentException("Could not insert into between/chain space. Space does not exist");
            }
            insertedLast = true;
        } else if (action.equals("move") || action.equals("skip")) { // increments next connection
            ArrayList<Loop> previousRow = loops.get(nextConnection.getRow());
            int connectionIndex = nextConnection.getIndex();

            for (int i = connectionIndex; i >= 0; i--) {
                Loop l = previousRow.get(i);
                if (l.isTop()) {
                    break;
                }
                connectionIndex--;
            }
            connectionIndex--;
            nextConnection.setIndex(connectionIndex);
        } else if (action.equals("turn")) { // begins a new row
            loops.add(currRow);
            nextConnection = new twoItems(row, currRow.size() - 1);
            currRow = new ArrayList<Loop>();
            row++;
        } else {}
        actionIndex++;
    }

    /**
     * Undoes the last action, if possible
     * Must check validity of undoing last action before calling
     **/
    public void undoLastAction() {
        actionIndex--;
        String lastAction = actions.get(actionIndex);
        if (lastAction.equals("yo")) { // yarn over: adds a loop to hook
            // removes latest hook element from list
            if(hookLoops.pop() instanceof Loop)
                loopCount--;
        } else if (lastAction.equals("pt")) { // pull through: remove second to last element from hook and add it to loops list
            // add back the second to last hookElement to hookLoop list, removing it from loops if applicable
            // check if you finished a between space
            if (!post && hookLoops.size() == betweenCounter - 1) {
                // System.out.println("Between Attach Point Completed");
                post = true;
            }

            HookElement lastElement = hookLoops.pop();
            Loop lastLoop = null;
            if(lastElement instanceof Loop)
                lastLoop = (Loop) lastElement;
            else
                throw new IllegalArgumentException("Last element is not a loop.");
            HookElement removedLoop = null;
            ArrayList<HookElement> connections = lastLoop.getConnections();
            // int lastID = connections.get(connections.size() - 1);
            // if (currRow.size() <= 0) { // if currRow is empty, get last inserted loop from previous row
            //     removedLoop = getLastInsertedLoop(lastID);
            //     if (removedLoop.equals(null))
            //         throw new IllegalArgumentException("Couldn't find last loop");
            // } else { // if currRow is not empty, try the latest loop pushed to the current row list
            //     removedLoop = currRow.get(currRow.size() - 1);
            // }
            // if (removedLoop.getID() != lastID) { // if the latest loop in currRow isn't the connected Element, get the connection from the previous row
            //     removedLoop = getLastInsertedLoop(lastID);
            //     if (removedLoop.equals(null))
            //         throw new IllegalArgumentException("Couldn't find last loop");

            // } else {
            //     // removed removedLoop from currRow
            //     currRow.remove(removedLoop);
            // }
            removedLoop = connections.get(connections.size() - 1);
            if(removedLoop instanceof Loop){
                if(currRow.remove(removedLoop) == false){
                    if(loops.size() > 0){
                        ArrayList<Loop> previousRow = loops.get(loops.size() - 1);
                        if(previousRow.remove(removedLoop) == false){
                            throw new IllegalArgumentException("Couldn't find last loop: " + ((Loop)removedLoop).getID());
                        }
                    } else {
                        throw new IllegalArgumentException("Couldn't find last loop: " + ((Loop)removedLoop).getID());
                    }
                }
            }

            if (hookLoops.size() == 0) {
                if(removedLoop instanceof Loop)
                    ((Loop) removedLoop).toggleTop();
                else
                    throw new IllegalArgumentException("Finishing hook element is not a loop.");
                stitchCount--;
                lastLoop.setStitchID(stitchCount - 1);
                constructingTop = false;
            }

            // if latest loop is connected to removed loop, remove the connection
            if(lastLoop instanceof Loop){
                if (((Loop)lastLoop).getConnections().contains(removedLoop)) {
                    ((Loop)lastLoop).removeConnection(removedLoop);
                }
            }
            if (!removedLoop.equals(null))
                hookLoops.push(removedLoop);
            else {
                System.out.println("Cannot undo last action: Removed Loop is NULL");
                return;
            }
            hookLoops.push(lastLoop);
        } else if (lastAction.equals("insert top") || lastAction.equals("insert front loop")
                || lastAction.equals("insert back loop")) {
            // add previous row's correct loop to hookLoops list
            ArrayList<Loop> previousRow = loops.get(loops.size() - 1);
            int connectionIndex = nextConnection.getIndex();

            // check if this stitch should create a between attach point

            for (int i = connectionIndex; i >= 0; i--) {
                Loop l = previousRow.get(i);
                if (l.isTop()) {
                    HookElement topLoop = hookLoops.pop();
                    if (!topLoop.equals(l))
                        throw new IllegalArgumentException(
                                "Popped loop (" + topLoop + ") doesn't match expected value (" + l + ").");
                    break;
                }
            }
            insertedLast = false;
            constructingTop = true;
        } else if (lastAction.equals("insert front post") || lastAction.equals("insert back post")) {
            ArrayList<Loop> previousRow = loops.get(loops.size() - 1);
            int connectionIndex = nextConnection.getIndex();
            Loop nextLoop = null;

            for (int i = connectionIndex; i >= 0; i--) {
                nextLoop = previousRow.get(i);
                if (nextLoop.isTop()) {
                    int nextStitchID = nextLoop.getStitchID();
                    Post nextPost = posts.get(nextStitchID);
                    if (nextPost.size() == 0)
                        throw new IllegalArgumentException("Cannot undo last action: Post does not exist");
                    HookElement topLoop = hookLoops.pop();
                    if (!topLoop.equals(nextPost)) {
                        throw new IllegalArgumentException(
                                "Popped loop (" + topLoop + ") doesn't match expected value (" + nextPost + ").");
                    }
                    break;
                }
            }
            if (post)
                post = false;
            insertedLast = false;
        } else if (lastAction.equals("move")) { // increments next connection
            ArrayList<Loop> previousRow = loops.get(loops.size() - 1);
            int connectionIndex = nextConnection.getIndex();

            for (int i = connectionIndex + 1; i < previousRow.size(); i++) {
                Loop l = previousRow.get(i);
                if (l.isTop()) {
                    break;
                }
                connectionIndex++;
            }
            connectionIndex++;
            nextConnection.setIndex(connectionIndex);
        } else if (lastAction.equals("turn")) { // begins a new row
            currRow = loops.get(loops.size() - 1);
            loops.remove(loops.size() - 1);
            row--;
            nextConnection = new twoItems(row, 0);
            //add last post to posts list if it has loops
            if(currPost != null && currPost.size() > 0)
                posts.add(currPost);
        } else if (lastAction.equals("skip")) {
            int currIndex = nextConnection.getIndex();
            nextConnection.setIndex(currIndex + 1);
        } else {
        }
    }

    // /**
    //  * Gets the last loop that the pattern inserted into using its ID
    //  * 
    //  * @param lastID the ID of the loop to find
    //  * @return the loop that was last inserted into
    //  */
    // private Loop getLastInsertedLoop(int lastID) {
    //     ArrayList<Loop> lastRow = loops.get(loops.size() - 1);
    //     for (int i = lastRow.size() - 1; i >= 0; i--) {
    //         Loop currLoop = lastRow.get(i);
    //         if (currLoop.getID() == lastID) {
    //             insertedLast = true;
    //             return currLoop;
    //         }
    //     }
    //     insertedLast = true;
    //     return null;
    // }

    /**
     * Finds next stitch top loop
     * @return the Top insertion point loop for the next stitch (excluding chains)
     */
    // private Loop findNextTop(){
    //     int connectionIndex = nextConnection.getIndex();
    //     ArrayList<Loop> previousRow = loops.get(nextConnection.getRow());
    //     Loop nextLoop = null;
    //     for (int i = connectionIndex; i >= 0; i--) {
    //         nextLoop = previousRow.get(i);
    //         if (nextLoop.isTop()){
    //             int nextIndex = i-1;
    //             if(nextIndex < previousRow.size()-1 && nextIndex > 0){
    //                 if(previousRow.get(i-1).isTop() && nextConnection.getRow() > 0)
    //                     continue;   // if the next loop is also a top, then it is a chain
    //                 else{
    //                     connectionIndex = i;
    //                     break;
    //                 }
    //             }
    //             else{
    //                 connectionIndex = i;
    //                 break;
    //             }
    //         }
    //     }
    //     if (nextLoop == null)
    //         throw new IllegalArgumentException("No stitches available in previous row (current row: " + row + ")");
    //     System.out.println("Next Top: " + nextLoop);
    //     nextConnection.setIndex(connectionIndex);
    //     return nextLoop;
    // }

    private Loop findNextTop(){
        int connectionIndex = nextConnection.getIndex();
        ArrayList<Loop> previousRow = loops.get(nextConnection.getRow());
        Loop prevLoop = null;
        for(int i = connectionIndex; i >=0; i--){
            prevLoop = previousRow.get(i);
            if(prevLoop.isTop())
                break;
        }
        return prevLoop;
    }

    private void savePost(){
        if(currPost != null && currPost.size() > 0)
            posts.add(currPost);
        if(post)
            currPost = new Post(stitchCount);
    }

    private void saveAndSetBetween(){
        if(currBetween != null && currBetween.size() > 0)
            betweenSpaces.add(currBetween);
        setBetweenSpace();
    }

    private void setBetweenSpace(){
        currBetween = new BetweenSpace(stitchCount);
        //check for chains
        int index = currRow.size() - 1;
        Loop prevLoop = null;
        if(index < currRow.size() - 1){
            prevLoop = currRow.get(index);
            while(prevLoop.isTop()){
                //add nextLoop
                currBetween.addLoop(prevLoop);
                index--;
                if(index < 0)
                    break;
                prevLoop = currRow.get(index);
            }
            //last loop added is the top of the previous stitch, so remove it from between space
            if(!(currBetween.size() == 0))
                currBetween.removeLoop(currBetween.size()-1);
        }
        //check if the current between space has the top of the current stitch
        if(currBetween.size() != 0 || post){
            currBetween.addLoop(nextTop);
            int nextID = nextTop.getID() + 1;
            for(HookElement e : hookLoops){
                if(e instanceof Loop){
                    if(((Loop)e).getID()==(nextID))
                        currBetween.addLoop((Loop)e);
                }
            }
        }
        if(!currBetween.contains(nextTop)){
            //this is a single crochet or slip stitch without any preceeding chains
            //check if the previous stitch contains 8 or more loops
            int numLoops = 1;
            index = currRow.size() - 1;
            if(index > -1)
                prevLoop = currRow.get(index);
            else
                return;
            // Loop prevTop = findNextTop();
            // ArrayList<Loop> prevRow = loops.get(loops.size() -1);
            // index = prevRow.indexOf(prevTop) - 1;
            // prevLoop = prevRow.get(index);
            int lastStitchID = stitchCount - 1;
            while(prevLoop.getStitchID() == lastStitchID){
                numLoops++;
                index--;
                if(index < 0)
                    break;
                prevLoop = currRow.get(index);
            }
            if(numLoops >= 4){
                //add current stitch's top and next loop (if able) to current between space
                currBetween.addLoop(nextTop);
                int nextID = nextTop.getID() + 1;
                for(HookElement e : hookLoops){
                    if(e instanceof Loop){
                        if(((Loop)e).getID()==(nextID))
                            currBetween.addLoop((Loop)e);
                    }
                }
            }
        }
    }

    /**
     * Checks if previous action was a yarn over to see if a between attach point
     * should be created for the current stitch
     */
    private void setPost() {
        if (actionIndex > 0) {
            if (actions.get(actionIndex - 1).equals("yo")) {
                post = true;
                int tempIndex = actionIndex - 1;
                String tempAction = actions.get(tempIndex);
                int tempLoopCount = hookLoops.size();
                while (tempAction.equals("yo")) {
                    tempIndex--;
                    tempAction = actions.get(tempIndex);
                    tempLoopCount--;
                }
                if (tempLoopCount > 1)
                    betweenCounter++;
                else
                    betweenCounter = 2;
            } else {
                post = false;
                betweenCounter = 2;
            }
        }
    }
}