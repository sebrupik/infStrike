package infStrike.objects;

import java.awt.Graphics2D;
import java.util.Vector;

import java.util.ArrayList;

import java.awt.Color;  //delete this

/**
* Essentialy a 2-dimensional grid 
*
* No longer JUST a grid, it also maps platoon clusters
*/
public class RCSInfluenceMap {
    
    private double[][] grid;  //do I really need double precision for this?
    private int resolution;
    private Vector friendlyPositions;  //double array (platoon value, leader.x, leader.y) 
    private Vector enemyPositions;

    private double frontUmbrella = 500.0;

    private final String type_FRONT;
    private final String type_FLANK;
    private final String type_REAR;
    private final String type_UNKNOWN;
    

    public RCSInfluenceMap(int width, int height, int resolution, String type_FRONT, String type_FLANK, String type_REAR, String type_UNKNOWN) {
        this.grid = new double[width/resolution][height/resolution];
        this.resolution = resolution;
        this.type_FRONT = type_FRONT;
        this.type_FLANK = type_FLANK;
        this.type_REAR = type_REAR;
        this.type_UNKNOWN = type_UNKNOWN;


        this.friendlyPositions = new Vector();
        this.enemyPositions = new Vector();
    }

    public void updateGraphics(double time, Graphics2D g2) {
        for (int x=0; x<grid.length; x++) {
            for (int y=0; y<grid[0].length; y++) {
                if ( (grid[x][y] > 1.0 | grid[x][y] < -1.0) )  //so that 0's aren't drawn (faster!)
                    g2.drawString(Integer.toString((int)grid[x][y]), (x*resolution)+(resolution/2), (y*resolution)+(resolution/2));
            }
        }
        RCSInfluenceMapPosition mapPos;
        for(int i=0; i<friendlyPositions.size(); i++) {
            mapPos = (RCSInfluenceMapPosition)friendlyPositions.elementAt(i);
            mapPos.updateGraphics(g2);
        }
    }

    /**
    * fVec is a vector containing AIPlatoon objects for the friendly side
    * eIntel is a vector containing AIIntelligence objects
    */
    public void refreshInfluence(Vector fVec, ArrayList eIntel) {
        calculateInfluence(fVec, eIntel);
    }

    /**
    * Enemy first because all friendly positions are named relative to their
    * oriantation with the enemies.
    * the Vectors enemyPositions and friendlyPositions are populated when the method
    * calculateInfluence is run.
    */
    public void refreshFFR() {
        if (enemyPositions.size() > 0) {
            calculateFF(enemyPositions);
        }
        calculateFF(friendlyPositions);

        if (enemyPositions.size() > 0) {
            calculateR(friendlyPositions, enemyPositions);
            calculateR(enemyPositions, friendlyPositions);
            calculateAttack(friendlyPositions);
        }
    }


    /**
    * Itterates through a vector of platoons and extracts the relevant influence based information
    * and passes it to the method applyValue.
    */
    public void calculateInfluence(Vector platoons, ArrayList eIntel) {
        this.grid = new double[grid.length][grid[0].length];
        this.friendlyPositions.clear();
        this.enemyPositions.clear();
 
        int platoonValue = 10;
        int platoonDropOff = 2;
        double platoonX, platoonY;
        AIPlatoon aip;  
        AIIntelligence aii;  
  
        for(int i=0; i<platoons.size(); i++) {
            aip = (AIPlatoon)platoons.elementAt(i);
            //platoonValue = aip.getPlatoonValue();
            //platoonDropOff = aip.getPlatoonDropOff();
            platoonX = aip.getLeaderLocation().x;
            platoonY = aip.getLeaderLocation().y;

            mergeInfluence(friendlyPositions, new double[]{(double)platoonValue, platoonX, platoonY}, aip);
            applyValue( ((int)platoonX)/resolution, ((int)platoonY)/resolution, platoonValue, platoonDropOff);
        }

        for (int i=0; i<eIntel.size(); i++) {
            aii = (AIIntelligence)eIntel.get(i);
            platoonValue = (int)aii.getValue();
            //platoonDropOff = aii.getPlatoonDropOff();
            platoonX = aii.getWhere().x;
            platoonY = aii.getWhere().y;
          
            mergeInfluence(enemyPositions, new double[]{platoonValue, platoonX, platoonY}, null);
            applyValue( ((int)platoonX)/resolution, ((int)platoonY)/resolution, platoonValue, platoonDropOff);
        }
    }

    /**
    * Given a new platoon co-ord if it is within mergeDistance then the new platoons
    * value is merged to the one it is close to, else it is added as a seperate platoon.
    */
    private void mergeInfluence(Vector positions, double[] newData, AIPlatoon aip) {
        int index = -1;
        RCSInfluenceMapPosition mapPos;
        double[] tDoub;
        double mergeDistance = (double)resolution;
        
        for (int i=0; i<positions.size(); i++) {
            mapPos = (RCSInfluenceMapPosition)positions.elementAt(i);
            tDoub = mapPos.getCenter();
            if (java.awt.geom.Point2D.distance(tDoub[0], tDoub[1], newData[1], newData[2]) < mergeDistance) {
                mapPos.addPosition(newData);
                if (aip != null)   // only friendly platoons are added. adding enemy platoons would be cheating!
                    mapPos.addPlatoon(aip);
                index = i;
            }
        }
        if (index == -1) { // no where near so add seperatly
            positions.addElement(new RCSInfluenceMapPosition(newData, type_UNKNOWN));
        }
    }

    /**
    * Given a specific position on the grid this method propagates the value given to 
    * the surrounding elements decreasing it by the the value of dropOff for each square moved away.
    */
    private void applyValue(int startX, int startY, int value, int dropOff) {
        int gap=0;

        if ( (startX>=0 & startX < grid.length) & (startY>=0 & startY <grid[0].length) )
            grid[startX][startY] = grid[startX][startY]+value;
        if (value > 0) {  //positive value for friendly positions
        while(value > 0) {
            //System.out.println(value);
            value = value/dropOff;
            gap++;
            
            for(int x = startX-gap; x< startX+gap+1; x++) {
                for(int y = startY-gap; y< startY+gap+1; y++) {
                    if ( ((x == startX-gap) | (x == startX+gap)) | ((y == startY-gap) | (y == startY+gap)) ) {
                        if ( (x>=0 & x < grid.length) & (y>=0 & y <grid[0].length) ) { 
                            grid[x][y] = grid[x][y]+value;
                        }
                    }
                }
            }
        }
        }
        else {  //negative value for enemyPositions
            while(value < 0) {
            //System.out.println(value);
            value = value/dropOff;
            gap++;
            
            for(int x = startX-gap; x< startX+gap+1; x++) {
                for(int y = startY-gap; y< startY+gap+1; y++) {
                    if ( ((x == startX-gap) | (x == startX+gap)) | ((y == startY-gap) | (y == startY+gap)) ) {
                        if ( (x>=0 & x < grid.length) & (y>=0 & y <grid[0].length) ) { 
                            grid[x][y] = grid[x][y]+value;
                        }
                    }
                }
            }
        }
        }
    }
    

    /**
    * Vector v contains unsorted platoon clusters 
    * Vector tVec contains sorted and assigned positions
    */
    private void calculateFF(Vector v) {
        Vector tVec = new Vector();
        orderClusterValues(v, tVec);
        calculateFrontsAndFlanks(v, tVec);
    }

    /**
    * v1 : vector of RCSInfluenceMapPosition objects
    * v2 : empty vector. filled with double arrays.
    * the first element of which is the pos totalinfluence value
    * the second element the position of pos in the vector v
    */
    private void orderClusterValues(Vector x1, Vector x2) {
        RCSInfluenceMapPosition pos;

        for (int i=0; i<x1.size(); i++) {
            pos = (RCSInfluenceMapPosition)x1.elementAt(i);
            addDoubleHigh(x2, new double[] {pos.getTotalValue(), (double)i});
        }
    }

    /**
    * Takes the double arrays from v2, and takes the element refered to by the double
    * array and assigns it to pos1. v1 is then itterated through using pos2.
    * If pos2 is within 'frontUmbrella' then it is pos1's flank, else it is a new front.
    */
    private void calculateFrontsAndFlanks(Vector v1, Vector v2) {
        RCSInfluenceMapPosition pos1;
        RCSInfluenceMapPosition pos2;
        double[] d;

        for (int i=0; i< v2.size(); i++) {
            d = (double[])v2.elementAt(i);
            pos1 = (RCSInfluenceMapPosition)v1.elementAt((int)d[1]);
            if ( pos1.getType().equals(type_UNKNOWN)) {
                pos1.setType(type_FRONT);
                pos1.informUmbrella(frontUmbrella);
                pos1.setOwns(pos1);   //has to added to make itterations in RCSIMP easier
                for (int j=0; j<v1.size(); j++) {
                    pos2 = (RCSInfluenceMapPosition)v1.elementAt(j);
                    if ((int)d[1] != j & pos2.getType().equals(type_UNKNOWN)) {
                        if (java.awt.geom.Point2D.distance(pos1.getCenter()[0], pos1.getCenter()[1], pos2.getCenter()[0], pos2.getCenter()[1]) < frontUmbrella) {
                            pos2.setType(type_FLANK);
                            pos2.setOwner(pos1);
                            pos1.setOwns(pos2);
                        }
                    }
                }
            }
        }
    }


    /**
    * given a vector and the position of the enemy front, calculate rear
    * a rear position is one that is behind the influence of the front, when a line
    * is drawn from the enemy front to itself and passes through the friendly front
    */
    private void calculateR(Vector v, Vector v2) {
        RCSInfluenceMapPosition pos1;
        RCSInfluenceMapPosition pos2;
        int index = -1;
        double distance = Double.MAX_VALUE;

        // find the first friendly front
        for (int i=0; i<v.size(); i++) {
            pos1 = (RCSInfluenceMapPosition)v.elementAt(i);
            if (pos1.getType().equals(type_FRONT)) {
                for (int j=0; j<v2.size(); j++) {   //find the closest enemy
                    pos2 = (RCSInfluenceMapPosition)v2.elementAt(j);
                    if (pos2.getType().equals(type_FRONT)) {
                        if (java.awt.geom.Point2D.distance(pos1.getCenter()[0], pos1.getCenter()[1], pos2.getCenter()[0], pos2.getCenter()[1]) < distance ) {
                            distance  = java.awt.geom.Point2D.distance(pos1.getCenter()[0], pos1.getCenter()[1], pos2.getCenter()[0], pos2.getCenter()[1]);
                            index = j;
                        }
                    }
                }
                if (index != -1) {
                    pos2 = (RCSInfluenceMapPosition)v2.elementAt(index);   // the closest enemy front
                    pos1.passesThrough(pos2, type_REAR);  // now find the rear clusters
                }
            }
        }
    }

    private void calculateAttack(Vector v1) {
        RCSInfluenceMapPosition pos1;
        for (int i=0; i<v1.size(); i++) {
            pos1 = ((RCSInfluenceMapPosition)v1.elementAt(i));
            if (pos1.getType().equals(type_FRONT)) 
                pos1.calcAllAttackValues(new String[]{type_REAR, type_UNKNOWN});
        }
    }

    /**
    * Given a double array d, it will be added in to the vector v so that the 
    * highest value is a 0 and the lowest at n-1.
    */
    private void addDoubleHigh(Vector v, double[] d) {
        int index = 0;

        for (int i=0; i<v.size(); i++) {
            if ( ((double[])v.elementAt(i))[0] < d[0] ) 
                break;
            index = i;
        }
        
        if (index != v.size()-1) { 
            v.insertElementAt(d, index);
        } else { 
            v.addElement(d); 
        }
    }

    public Vector getFriendlyPositions() { return friendlyPositions; }
}