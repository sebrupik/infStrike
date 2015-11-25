package infStrike.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.*;
import java.awt.geom.Point2D;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

public class arenaPlan {
    private String name;
    private int width;
    private int height; 
    private int gridsize;
    private topoObj topo;
    private Vector nodeVec = new Vector();
    private Vector featVec = new Vector();

    private featObj tmpFeatObj;
    private featBush tmpBush;
    private featForest tmpForest;
    private featLake tmpLake;
    private featBuilding tmpBuilding;

    private boolean drawNodeBoo = false;

    public arenaPlan(Vector arg1, topoObj arg2) {
        topo = arg2;
        gridsize = topo.getGridsize();
        for (int i=0; i<arg1.size(); i++) {
            if (arg1.elementAt(i) instanceof mapInfo) {
                mapInfo tMapI = (mapInfo)arg1.elementAt(i);
                name = tMapI.getName();
                width = tMapI.getWidth();
                height = tMapI.getHeight();
            }
            else
                featVec.add(arg1.elementAt(i));
        }
        
    }

    public void updateGraphics(double time, Graphics2D g2) {
        topo.draw(g2);

        for (int i=0; i<featVec.size(); i++) {
            /*if (featVec.elementAt(i) instanceof featForest) {
                tmpForest = (featForest)featVec.elementAt(i);
                tmpForest.draw(g2);
            }
            if (featVec.elementAt(i) instanceof featLake) {
                tmpLake = (featLake)featVec.elementAt(i);
                tmpLake.draw(g2);
            }
            if (featVec.elementAt(i) instanceof featBuilding) {
                tmpBuilding = (featBuilding)featVec.elementAt(i);
                tmpBuilding.draw(g2);
            }*/
            tmpFeatObj = (featObj)featVec.elementAt(i);
            tmpFeatObj.updateGraphics(time, g2);
        }

        if (drawNodeBoo) {
            navNode tmpNode; 
            for (int i=0; i<nodeVec.size(); i++) {
                tmpNode = (navNode)nodeVec.elementAt(i);
                tmpNode.draw(g2);
            }
        }
    }

//*********************
    /**
    * Method to make navigation node grid
    */
    /*public void makeNavGrid() {
        System.out.println("ARENAPLAN - Making Nav Grid");
        int tmpId = 1;
        Point2D.Double tmp;
        for (int x = (gridsize/2); x < width; x += gridsize) {
            for (int y = (gridsize/2); y < height; y += gridsize) {
                tmp = new Point2D.Double(x, y);
                if(pointInside(tmp) == false) { 
                    nodeVec.addElement(new navNode(tmpId, tmp, calcCost(tmp)));
                } 
                tmpId++; 
            }
        }
        System.out.println("ARENAPLAN - Finished Making Nav Grid");
        connectNodes();
    }*/
    /**
    * Checks to see if point is inside a lake or building
    */
    private boolean pointInside(Point2D.Double arg1) {
        featLake tmpLake;
        featBuilding tmpBuilding;
        for (int i=0; i<featVec.size(); i++) {     
            if (featVec.elementAt(i) instanceof featLake) {
                tmpLake = (featLake)featVec.elementAt(i); 
                if (tmpLake.contains(arg1))
                    return true;
            }
            else if (featVec.elementAt(i) instanceof featBuilding) {
                tmpBuilding = (featBuilding)featVec.elementAt(i); 
                if (tmpBuilding.contains(arg1))
                    return true;
            }
        }
        return false;
    }
    /**
    * Works out cost for a node. 1 for nothing, 2 for inside a forest
    */
    private int calcCost(Point2D.Double arg1) {
        featForest tmpForest;
        for (int i=0; i<featVec.size(); i++) {     
            if (featVec.elementAt(i) instanceof featForest) {
                tmpForest = (featForest)featVec.elementAt(i); 
                if (tmpForest.contains(arg1))
                    return 2;
            }
        }
        return 1;
    }
    /**
    * Works out the neighbours for each node within a 100 meter (pixel) radius
    */
    private void connectNodes() {
        System.out.println("ARENAPLAN - Connecting Nav Nodes");
        Polygon vicPoly = new Polygon();
        navNode tmpNode;
        navNode tmpNode2;
        for (int i =0; i < nodeVec.size(); i++) {           
            tmpNode = (navNode)nodeVec.elementAt(i);
            vicPoly.reset();
            vicPoly.addPoint((int)tmpNode.getCor().x-105, (int)tmpNode.getCor().y-105);
            vicPoly.addPoint((int)tmpNode.getCor().x+105, (int)tmpNode.getCor().y-105);
            vicPoly.addPoint((int)tmpNode.getCor().x+105, (int)tmpNode.getCor().y+105);
            vicPoly.addPoint((int)tmpNode.getCor().x-105, (int)tmpNode.getCor().y+105);

            for (int j=0; j < nodeVec.size(); j++) {
                tmpNode2 = (navNode)nodeVec.elementAt(j);
                if (tmpNode.getId() != tmpNode2.getId()) {
                    //System.out.println(tmpNode.getId()+" not the same as "+tmpNode2.getId());
                    if(vicPoly.contains(tmpNode2.getCor().x, tmpNode2.getCor().y)) {
                        //System.out.println("Node "+tmpNode.getId()+" is connected to "+tmpNode2.getId());
                        tmpNode.addNeighbour(tmpNode2, tmpNode2.getCost());
                    }
                }
            }
        }
        System.out.println("ARENAPLAN - Finished Connecting Nav Nodes");
    }

//****************** return methods
    public void calcHeight(int arg1, int arg2) { topo.calcHeight(arg1, arg2); }
    public void printInfo() { System.out.println("Arena Plan is "+width+","+height); }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getGridsize() { return topo.getGridsize(); }
    public Vector getFeatVec() { return featVec; }
    public topoObj getTopoObj() { return topo; }
    public Vector getNodeVec() { return nodeVec; }
}