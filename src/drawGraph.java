import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class drawGraph extends JPanel {
    private ArrayList<ArrayList<Stitch>> rows;
    private ArrayList<ArrayList<graphNode>> graphNodes;
    private int stitchSize = 40;
    private int rowSpacing = 70;
    private JFrame frame;

    public drawGraph(ArrayList<ArrayList<Stitch>> rows) {
        graphNodes = new ArrayList<ArrayList<graphNode>>();
        this.rows = rows;
        this.frame = new JFrame("Crochet Connection Graph");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(800, 600);
        this.frame.setLocation(300, 400);
        this.frame.add(this);
        this.frame.setVisible(true);
        calculateNodes();
    }

    private void calculateNodes(){
        int positive = 1;
        int currentX = stitchSize + 300;
        for (int i = 0; i < rows.size(); i++) {
            ArrayList<Stitch> row = rows.get(i);
            ArrayList<graphNode> nodeRow = new ArrayList<graphNode>();
            for(int t = 0; t < row.size(); t++){
                nodeRow.add(null);
            }
            for (int j = 0; j < row.size(); j++) {
                Stitch stitch = row.get(j);
                if(i != 0){
                    //if(!stitch.getType().equals("ch")){
                        ArrayList<twoItems> connectedTo = stitch.connectedTo;
                        if(connectedTo.size() > 0 && j == 0){
                            int xAvg = 0;
                            for(int k = 0; k < connectedTo.size(); k++){
                                twoItems connection = connectedTo.get(k);
                                int connectedRow = connection.getRow();
                                int connectedIndex = connection.getIndex();
                                graphNode connectedNode = graphNodes.get(connectedRow).get(connectedIndex);
                                xAvg += connectedNode.getX();
                            }
                            //if(connectedTo.size() > 1)
                            currentX = xAvg / connectedTo.size();
                        }
                        //else{}
                        Pattern incPattern = Pattern.compile("^(.*)(\\d+)(inc)$");
			            Matcher matcher = incPattern.matcher(stitch.getType());
                        int newX = 0;
				        if (matcher.matches()) {
					        int count = Integer.parseInt(matcher.group(2));
                            int firstX = currentX - (1 - stitchSize/count) * positive;
                            for(int z = 0; z < count; z++){
                                newX = firstX + z * stitchSize;
                                int index;
                                if(positive == 1)
                                    index = z;
                                else
                                    index = (count - 1) - z;
                                nodeRow.set(j + index, new graphNode(newX, -i * rowSpacing + 400, row.get(j+index)));
                            }
                            j+= count - 1;
                            currentX += (count) * stitchSize * positive;
                            continue; 
                        }
                    
                }
                int y = -i * rowSpacing + 400; // Add some padding
                //currentX += stitchSize * positive;
                nodeRow.set(j, new graphNode(currentX, y, stitch));
                currentX += stitchSize * positive;

            }
            positive *= -1;
            graphNodes.add(nodeRow);
            System.out.println("Row " + i + ": " + nodeRow);
        }
    }

    private ArrayList<twoItems> calculateConnections(Stitch stitch){
        ArrayList<twoItems> connections = new ArrayList<>();
        int counter = 0;
                if(stitch.connectedTo.size() > 0) { 
                    if(counter == 0)              
                        System.out.println("Current stitch: " + stitch);
                    for(twoItems connectedStitch : stitch.connectedTo) {
                        int connectedRow = connectedStitch.getRow();
                        int connectedIndex = connectedStitch.getIndex();
                        graphNode connectedNode = graphNodes.get(connectedRow).get(connectedIndex);
                        if(counter == 0)
                            System.out.println("Connected stitch: " + connectedNode.getStitch());

                        int connectedX = connectedNode.getX();
                        int connectedY = connectedNode.getY();
                        counter++;
                        connections.add(new twoItems(connectedX, connectedY));
                    }
                    return connections;
                }
                return null;
    }

    private void drawConnections(Graphics g, graphNode currentNode, ArrayList<twoItems> locations){
        for(twoItems location : locations){
            g.drawLine(currentNode.getX() + stitchSize/2, currentNode.getY() + stitchSize - 10, location.getRow() + stitchSize/2, location.getIndex() + stitchSize/2);
        }

    }

    private void drawStitch(Graphics g, graphNode node){
        Stitch stitch = node.getStitch();
        int x = node.getX();
        int y = node.getY();
        
        // if(stitch.getType().equals("ch"))
        //     g.drawOval(x, y, stitchSize-10, stitchSize-10);
        // else
            g.drawOval(x, y, stitchSize, stitchSize);
        g.drawString(stitch.getType(), x, y - 5); // Draw the type above the stitch
        g.drawString(stitch.getIndex() + "", x, y + stitchSize + 15); // Draw the index
    }

    @Override
    protected void paintComponent(Graphics g) {
        //boolean positive = true;
        super.paintComponent(g);
        for (ArrayList<graphNode> nodeRow : graphNodes) {
            for (graphNode node : nodeRow) {
                drawStitch(g, node);
                ArrayList<twoItems> connections = calculateConnections(node.getStitch());
                if(connections != null){
                    drawConnections(g, node, connections);
                }
            }
        }
    }
    
}
