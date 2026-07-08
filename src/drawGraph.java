import java.util.ArrayList;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class drawGraph extends JPanel {
    private ArrayList<ArrayList<Stitch>> rows;
    private ArrayList<ArrayList<graphNode>> graphNodes;
    private int stitchSize = 20;
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

    // public drawGraph(ArrayList<ArrayList<Stitch>> rows) {
    //     this.rows = rows;
    // }

    private void calculateNodes(){
        boolean positive = true;
        for (int i = 0; i < rows.size(); i++) {
            ArrayList<Stitch> row = rows.get(i);
            ArrayList<graphNode> nodeRow = new ArrayList<graphNode>();
            for (int j = 0; j < row.size(); j++) {
                Stitch stitch = row.get(j);
                int x = j * stitchSize + 300; // Add some padding
                if(i != 0){
                    int xOffset = stitchSize * rows.get(i-1).size();
                    if(positive)
                        x = j * stitchSize + 300 - xOffset; // Add some padding
                    else
                        x = -j * stitchSize + xOffset + 300;
                }
                //int x = startX * stitchSize + 300; // Add some padding
                int y = -i * rowSpacing + 400; // Add some padding
                nodeRow.add(new graphNode(x, y, stitch));

            }
            positive = !positive;
            graphNodes.add(nodeRow);
            System.out.println("Row " + i + ": " + nodeRow);
        }
        for(ArrayList<graphNode> nodeRow : graphNodes){
            System.out.println("Node row: " + nodeRow);
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
            g.drawLine(currentNode.getX() + stitchSize / 2, currentNode.getY() + stitchSize / 2, location.getRow() + stitchSize / 2, location.getIndex() + stitchSize / 2);
        }

    }

    private void drawStitch(Graphics g, graphNode node){
        Stitch stitch = node.getStitch();
        int x = node.getX();
        int y = node.getY();
        g.drawString(stitch.getIndex() + "", x, y + stitchSize + 15); // Draw the index
        g.drawOval(x, y, stitchSize, stitchSize);
        g.drawString(stitch.getType(), x, y - 5); // Draw the type above the stitch
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
        // for (int i = 0; i < rows.size(); i++) {
        //     ArrayList<Stitch> row = rows.get(i);
        //     ArrayList<graphNode> nodeRow = new ArrayList<graphNode>();
        //     for (int j = 0; j < row.size(); j++) {
        //         Stitch stitch = row.get(j);
        //         int x = j * stitchSize + 300; // Add some padding
        //         if(i != 0){
        //             int xOffset = stitchSize * rows.get(i-1).size();
        //             if(positive)
        //                 x = j * stitchSize + 300 - xOffset; // Add some padding
        //             else
        //                 x = -j * stitchSize + xOffset + 300;
        //         }
        //         //int x = startX * stitchSize + 300; // Add some padding
        //         int y = -i * rowSpacing + 400; // Add some padding
        //         graphNode node = new graphNode(x, y, stitch);
        //         nodeRow.add(node);
        //         g.drawString(stitch.getIndex() + "", x, y + stitchSize + 15); // Draw the index
        //         g.drawOval(x, y, stitchSize, stitchSize);
        //         g.drawString(stitch.getType(), x, y - 5); // Draw the type above the stitch

        //         int counter = 0;
        //         if(stitch.connectedTo.size() > 0) { 
        //             if(counter == 0)              
        //                 System.out.println("Current stitch: " + stitch);
        //             for(twoItems connectedStitch : stitch.connectedTo) {
        //                 int connectedRow = connectedStitch.getRow();
        //                 int connectedIndex = connectedStitch.getIndex();
        //                 graphNode connectedNode = graphNodes.get(connectedRow).get(connectedIndex);
        //                 if(counter == 0)
        //                     System.out.println("Connected stitch: " + connectedNode.getStitch());

        //                 int connectedX = connectedNode.getX();
        //                 int connectedY = connectedNode.getY();
        //                 //int connectedX = connectedIndex * stitchSize + 300;
        //                 //int connectedY = -connectedRow * rowSpacing + 400;
        //                 counter++;
        //                 g.drawLine(x + stitchSize / 2, y + stitchSize / 2, connectedX + stitchSize / 2, connectedY + stitchSize / 2);
        //             }
        //         }
        //     }
        //     positive = !positive;
        //     graphNodes.add(nodeRow);
        //     System.out.println("Row " + i + ": " + nodeRow);
        // }
        // for(ArrayList<graphNode> nodeRow : graphNodes){
        //     System.out.println("Node row: " + nodeRow);
        // }
        //System.out.println("Graph nodes: " + graphNodes);
    }
    
}
