package infStrike.objects;

import infStrike.utils.topoMerger;

import java.awt.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Polygon;
//import java.awt.geom.*;
import java.io.*;
import java.util.Vector;
//import java.util.*;

public class topoObj {
    private int pointsX;
    private int pointsY;
    private int gridSize;
    private int[][] topoValues;
    private int maxTopoHeight;
    private double topoPercent;

    private Vector topoVec;
    private Vector topoColourVec;
    //private Vector tmpVec;
    private Polygon tmpPoly1;

    private topoMerger tMerger;

    /**
    * To the best of my recolection the topoObj only needs be able to give out a height value given any 2D co-od, 
    * and also to provide a draw method. Anything else goes for this set to monster-mash classes.
    */

    public topoObj(int arg1, int arg2, int arg3, int arg4, int[] arg5) {
        pointsX = arg1;
        pointsY = arg2;
        gridSize = arg3; 
        maxTopoHeight = arg4;
        topoPercent = 255.0/maxTopoHeight;
        topoValues = new int[pointsX][pointsY];
        int tmpY = 0;
        int tmpX = 0;
        for (int i=0; i<arg5.length;i++) {
            if(i % pointsX == 0 & i !=0) {
                tmpX = 0;
                tmpY++;
            }
            topoValues[tmpX][tmpY] = arg5[i];
            tmpX++;
        }
        mkWireFrame();
        topomerge();   
    }

    public void topomerge() {
        topoVec = new Vector();
        topoColourVec = new Vector();
        tMerger = new topoMerger(pointsX, pointsY, gridSize, topoValues, topoVec, topoColourVec);
        tMerger.topomerge();
        tMerger = null;
    }
    
    /**
    * It may be more sensible to add an initialisation method that creates a Vector
    * and converts each array element into a polygon. These polygons are then merged 
    * with their neighbours if they are of the same height. The draw method then draws
    * all the polygons in the Vector. This SHOULD give a fairly significant performance 
    * boost (depending on the hit incurred from using polygons).
    * If I cannot solve the lag experienced on big maps as a result ot drawing the topography
    * I may consider dropping it all together.
    * This is a last resort. (11/7/02)
    */
    public void draw(Graphics g) {
        if (topoVec != null) {
            for (int i=0; i<topoVec.size(); i++) {
                tmpPoly1 = (Polygon)topoVec.elementAt(i);
                g.setColor(new Color(120, 200, (int)(255-(topoPercent*Integer.parseInt((String)topoColourVec.elementAt(i))))));
                g.fillPolygon(tmpPoly1);
                //g.drawPolygon(tmpPoly1);
            }
        }
        else {
            g.drawString("objects/topoObj - no enhanced topography to draw", 10, 10);
            g.drawRect(0,0,(pointsX*gridSize), (pointsY*gridSize));
        }

        /*int tmpX = 0;
        int tmpY = 0;
        for(int i=0; i<pointsY; i++) {   // the height of the array grid
            for (int j=0; j<pointsX; j++) {
                g.setColor(new Color(255, (int)(255-(topoPercent*topoValues[j][i])), 255));
                tmpX = gridSize*j;
                g.fillRect(tmpX, tmpY, gridSize, gridSize);
            } 
            tmpX = 0;
            tmpY += gridSize;   
        }*/
    }

    /**
    * Given x, y coords (arg1, arg2) will return the height of that loaction.
    */
    public int calcHeight(int arg1, int arg2) {
        int xPos = arg1;
        int yPos = arg2;

        return 0;
    }

    /**
    * Builds a wireframe model of the topography of the map. Currently not working properly
    */
    private void mkWireFrame() {
        try {         
            FileWriter stuff = new FileWriter("testWire.obj");
            int tmpX = 0;
            int tmpY = 0;
            /*for(int i=0; i<pointsY; i++) {   // the height of the array grid
                for (int j=0; j<pointsX; j++) {
                    tmpX = gridSize*j;
                    stuff.write("v "+(tmpX+50)+" "+(tmpY+50)+" "+(topoValues[j][i]*40)+"\n");
                } 
                tmpX = 0;
                tmpY += gridSize;   
            }*/
            // for mirroring of the points. not necessarily correct but necessary for the wireframe applet
            for(int i=(pointsY-1); i>=0; i--) {   // the height of the array grid
                for (int j=0; j<pointsX; j++) {
                    tmpX = gridSize*j;
                    stuff.write("v "+(tmpX+(gridSize/2))+" "+(tmpY+(gridSize/2))+" "+(topoValues[j][i]*4)+"\n");
                    //stuff.write("v "+(tmpX+(gridSize/2))+" "+(tmpY+(gridSize/2))+" "+topoValues[j][i]+"\n");
                } 
                tmpX = 0;
                tmpY += gridSize;   
            }
            
            int tmp = (pointsX * pointsY)-pointsX;
            for (int i=1; i<tmp; i++) {
                if (!(i % pointsX ==0 & i !=0)) {
                    /*stuff.write("f "+i +" "+ 
                                 (i+21) +" "+
                                 (i+20) +"\n");

                    stuff.write("f "+i +" "+ 
                                 (i+1) +" "+
                                 (i+21) +"\n");*/
                    
                    stuff.write("f "+i +" "+ 
                                 (i+(pointsX+1)) +" "+
                                 (i+pointsX) +"\n");

                    stuff.write("f "+i +" "+ 
                                 (i+1) +" "+
                                 (i+(pointsX+1)) +"\n");
                }
            }
            stuff.close();
        }
        catch (IOException e) {
            System.out.println("Error -- "+ e.toString());
        }
    }

// ******************** return methods **********************
    public int[][] getTopoValues() { return topoValues; }
    public int getGridsize() { return gridSize; }
}