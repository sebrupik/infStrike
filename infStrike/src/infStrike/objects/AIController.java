package infStrike.objects;

import java.util.ArrayList;
import java.awt.Graphics2D;

public abstract class AIController {
    private int side;
    private worldObject world;

    private int view;
    private int vArc;
    private int platSize;
    private int numBase;
    private boolean picWeap;
    private boolean advWeap;
    private boolean platoon;
    private boolean advPath;

    public AIController(int side, worldObject world) {
        this.side = side;
        this.world = world;
    }
    
    public abstract void action(double time);
    public abstract void updateGraphics(double time, Graphics2D g2, boolean AIDraw);
    public abstract void addSpecificMission(AIPlatoon platoon, AIMission mission);
    public abstract void receiveRequest(AIRequest arg1);

    public void setVars(int view, int vArc, int platSize, int numBase, boolean picWeap, boolean advWeap, boolean platoon, boolean advPath) {
        this.view =  view;
        this.vArc = vArc;
        this.platSize = platSize;
        this.numBase = numBase;
        this.picWeap = picWeap;
        this.advWeap = advWeap;
        this.platoon = platoon;
        this.advPath = advPath;
    }

    public Base getNearestBase(double[] d) {
        double distance = Double.MAX_VALUE;
        int index = -1;
        Base base1;    
 
        for (int i=0; i<world.bases[side].size(); i++) {
            base1 = (Base)world.bases[side].get(i);
            if (java.awt.geom.Point2D.distance(d[0], d[1], base1.getCor().x, base1.getCor().y) < distance) {
                distance = java.awt.geom.Point2D.distance(d[0], d[1], base1.getCor().x, base1.getCor().y);
                index = i;
            }
        }
        if (index != -1) 
            return (Base)world.bases[side].get(index);
        else 
            return null;
    }

    public Base getNearestBase(Agent a) {
        return this.getNearestBase(new double[]{a.getCor().x, a.getCor().y});
    }

    /**
        Checks platoons missions to see which base they are affiliated with. Base with least one or no
        affiliated platoons will receive transfer mission from base if its capacity is exceeded.
    */
    public Base needTransfer() {
        ArrayList<Base> v = (ArrayList)world.bases[side]; 
        
        Base base = null;
        //base = (Base)v.elementAt(0);
 
        for (int i=0; i<v.size(); i++) {
            if( base==null ) 
                base = v.get(i);
            
            if ( v.get(i).calcMembers() < base.calcMembers() ) 
                base = v.get(i);
        }
        return base;
    }


    public int getSide() { return side; }
    public worldObject getWorldObject() { return world; }
}