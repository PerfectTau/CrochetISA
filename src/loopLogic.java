import java.util.ArrayList;
import java.util.Stack;

public class loopLogic {
    ArrayList<String> actions;
    ArrayList<ArrayList<Loop>> loops;
    ArrayList<Post> posts;
    ArrayList<Loop> currRow;
    Post currPost;
    Stack<HookElement> hookLoops;
    twoItems nextConnection;
    int actionIndex;
    int loopCount;
    int stitchCount;
    int row;
    int betweenCounter;
    boolean insertedLast;
    boolean constructingTop;
    boolean between;

    public loopLogic(ArrayList<String> actions) {
        this.actions = actions;
        loops = new ArrayList<ArrayList<Loop>>();
        posts = new ArrayList<Post>();
        hookLoops = new Stack<HookElement>();
        hookLoops.add(new Loop(0, 0));
        currRow = new ArrayList<Loop>();
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
                // finishes post construction, back to working on top
                if (removedLoop instanceof Loop)
                    ((Loop) removedLoop).toggleTop();
                else
                    throw new IllegalArgumentException("Finishing hook element is not a loop.");
                stitchCount++;
                posts.add(currPost);
                currPost = new Post(stitchCount);
                lastLoop.setStitchID(stitchCount);
                constructingTop = true;
            }
            if (!insertedLast && removedLoop instanceof Loop) {
                currRow.add((Loop) removedLoop);
                if (!constructingTop)
                    currPost.addLoop((Loop) removedLoop);
            }
            // if latest loop is not connected to removed loop, connect them
            // int removedLoopID = removedLoop.getID();
            if (lastLoop instanceof Loop) {
                if (((Loop)lastLoop).getConnections().contains(removedLoop) == false) {
                    ((Loop)lastLoop).addConnection(removedLoop);
                }
            }
            hookLoops.push(lastLoop);
            insertedLast = false;

            // check if you finished a between space
            if (between && hookLoops.size() == betweenCounter) {
                System.out.println("Between Attach Point Completed");
                between = false;
            }
        } else if (action.equals("insert top") || action.equals("insert front loop") || action.equals("insert back loop")) {
            // add previous row's correct loop to hookLoops list
            // begins work on post
            constructingTop = false;

            ArrayList<Loop> previousRow = loops.get(nextConnection.getRow());
            int connectionIndex = nextConnection.getIndex();

            // check if this stitch should create a post
            setBetween();

            for (int i = connectionIndex; i >= 0; i--) {
                Loop l = previousRow.get(i);
                if (l.isTop()) {
                    hookLoops.push(l);
                    break;
                }
            }
            insertedLast = true;

        } else if (action.equals("insert front post") || action.equals("insert back post")) {
            constructingTop = false;
            // get next connection's stitch's post
            // find the next stitch (find next top)
            int connectionIndex = nextConnection.getIndex();
            ArrayList<Loop> previousRow = loops.get(nextConnection.getRow());
            Loop nextLoop = null;
            for (int i = connectionIndex; i >= 0; i--) {
                nextLoop = previousRow.get(i);
                if (nextLoop.isTop())
                    break;
            }
            if (nextLoop == null)
                throw new IllegalArgumentException("No stitches available in previous row (current row: " + row + ")");
            int nextStitchID = nextLoop.getStitchID();
            Post nextPost = posts.get(nextStitchID);

            // check if this stitch should create a post/between attach point
            setBetween();

            if (nextPost.size() > 0) {
                hookLoops.push(nextPost);
            } else
                throw new IllegalArgumentException("Cannot insert around post: Post does not exist");
            insertedLast = true;
            // posts.add(currPost);
            // currPost = new Post(stitchCount);
        } else if (action.equals("move")) { // increments next connection
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
        } else if (action.equals("skip")) {
            int currIndex = nextConnection.getIndex();
            nextConnection.setIndex(currIndex - 1);
        } else {
        }
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
            if (!between && hookLoops.size() == betweenCounter - 1) {
                // System.out.println("Between Attach Point Completed");
                between = true;
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
            if (between)
                between = false;
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
     * Checks if previous action was a yarn over to see if a between attach point
     * should be created for the current stitch
     */
    private void setBetween() {
        if (actionIndex > 0) {
            if (actions.get(actionIndex - 1).equals("yo")) {
                between = true;
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
                between = false;
                betweenCounter = 2;
            }
        }
    }
}