package infStrike.objects;

import java.util.ArrayList;
import java.awt.geom.Point2D;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.*;

public class navNode {
    private int id;
    private Point2D.Double Cor;
    private int cost;
    private ArrayList<navNode> neighbours = new ArrayList<>();
    private ArrayList<navEdge> edges = new ArrayList<>();

    Arc2D.Double vicinity;
    Graphics2D g2D;

    public navNode(int arg1, Point2D.Double arg2, int arg3) {
        id = arg1;
        Cor = arg2;
        cost = arg3;
    }

    public void addNeighbour(navNode arg1, int arg2) {
        neighbours.add(new navNode(arg1.getId(), arg1.getCor(), arg2));
        edges.add(new navEdge(Cor.x, Cor.y, arg1.getCor().x, arg1.getCor().y, arg1.getId(), arg2));
    }
    
    public void draw(Graphics g) {
        g2D = (Graphics2D)g;
        g.drawRect((int)Cor.x, (int)Cor.y, 1, 1);
        //System.out.println("Node "+id+" has "+neighbours.size()+" neighbours");
        
        for (int i=0; i < neighbours.size(); i++) { 
            navNode tmpNode = neighbours.get(i);
            g.drawLine((int)Cor.x, (int)Cor.y, (int)tmpNode.getCor().x, (int)tmpNode.getCor().y);
        }
        g.drawString(Integer.toString(cost), (int)Cor.x, (int)Cor.y);
    }  
    public void drawRadius(Graphics g) {
        //Arc2D.Double vicinity;
        g2D.setColor(new Color(0, 0, 250, 40));
        //vicinity = new Arc2D.Double(Cor.x-105, Cor.y-105, 210, 210, 0, 360, Arc2D.PIE);
        g2D.fill(new Arc2D.Double(Cor.x-105, Cor.y-105, 210, 210, 0, 360, Arc2D.PIE));
    }

    /**
    * Method used in AI classes.
    * A Point is given, and if it is within a certain square preimeter the node returns true;
    */
    public boolean pointNear(Point2D.Double arg1) {
        //could create a polygon object but that would have a memory hit, so use if statements instead
        if ((arg1.x > (Cor.x+50)) & (arg1.x < (Cor.x-50)) & ((arg1.y > (Cor.y+50)) & (arg1.y < (Cor.y-50))) ) { 
            return true;
        }
        return false;
    }
    
    public Point2D.Double getCor() { return Cor; }
    public int getId() { return id; }
    public int getCost() { return cost; }
}