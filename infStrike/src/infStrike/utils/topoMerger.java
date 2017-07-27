package infStrike.utils;

import java.util.ArrayList;
import java.awt.*;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.*;
import java.awt.geom.Line2D.Double;
import java.util.*;
import java.util.HashMap;

public class topoMerger {
    private int pointsX, pointsY, gridSize;
    private int[][] topoValues;
    private ArrayList<Polygon> tmpPolyAl;
    private ArrayList topoVec, topoColourVec;
    private Polygon tmpPoly1, tmpPoly2;
    private Rectangle tmpRect;
    private String tmpStr;

    /**
    * Self-explantatory constructor.
    * NB. when adding graphical items to topoVec, be aware that any type can be used.
    * Just remember to change a few lines in the draw method of 'topoObj'.
    */
    public topoMerger(int arg1, int arg2, int arg3, int[][] arg4, ArrayList arg5, ArrayList arg6) {
        pointsX = arg1;
        pointsY = arg2;
        gridSize = arg3;
        topoValues = arg4;
        topoVec = arg5;
        topoColourVec = arg6;
    }

    /**
    * Topomerge- This method will try to merge as many topography polygons as possible. 
    * IF this works nice and gives the right ammount of performance boost, I'll make more
    * complex arenas.
    * This method will make or break the topology ideal!
    */ 
    public void topomerge() {
        HashMap<String, ArrayList> topoHash = new HashMap<>();

        int tmpX = 0;
        int tmpY = 0;
        for(int i=0; i<pointsY; i++) {   // the height of the array grid
            for (int j=0; j<pointsX; j++) {
                tmpX = gridSize*j;

                if(topoHash.containsKey(Integer.toString(topoValues[j][i]))) {
                    tmpPolyAl = topoHash.get(Integer.toString(topoValues[j][i]));
                    tmpPolyAl.add(new Polygon(new int[]{tmpX, (tmpX+gridSize), (tmpX+gridSize), tmpX}, new int[]{tmpY, tmpY, (tmpY+gridSize), (tmpY+gridSize)},4));
                }
                else {
                    tmpPolyAl = new ArrayList();
                    tmpPolyAl.add(new Polygon(new int[]{tmpX, (tmpX+gridSize), (tmpX+gridSize), tmpX}, new int[]{tmpY, tmpY, (tmpY+gridSize), (tmpY+gridSize)},4));
                    topoHash.put(Integer.toString(topoValues[j][i]), tmpPolyAl);
                }
            } 
            tmpX = 0;
            tmpY += gridSize;   
        }

        System.out.println("Give me all the fucking poly points! Mo-FO!");
        //Enumeration e2 = topoHash.keys();
        //while (e2.hasMoreElements()) {
        for(String s : topoHash.keySet()) {
            //tmpStr = (String)e2.nextElement();
            tmpPolyAl = topoHash.get(s);
            for(int j=0; j<tmpPolyAl.size(); j++) {
                tmpPoly1 = tmpPolyAl.get(j);
                System.out.println("("+tmpPoly1.xpoints[0]+","+tmpPoly1.ypoints[0]+")("+tmpPoly1.xpoints[1]+","+tmpPoly1.ypoints[1]+")("+
                                       tmpPoly1.xpoints[2]+","+tmpPoly1.ypoints[2]+")("+tmpPoly1.xpoints[3]+","+tmpPoly1.ypoints[3]+")");
            }
        }

        // All topography polygons have been segrgated into their heights, and put in their
        // respective bucket in topoHash.
        // Now I sort through each bucket at a time and merge neighbouring polygons.
        // these are then added to the Vector topoVec.
        
        littleSmasher(topoHash);
        //bigSmasher(topoHash);
    }
   
    private void littleSmasher(HashMap<String, ArrayList> topoHash) { 
        ArrayList<Polygon> v;      
        String str;

        //for (Enumeration e = topoHash.keys(); e.hasMoreElements();) {
        for(String s : topoHash.keySet()) {
            //str = (String)e.nextElement();
            v = topoHash.get(s);
            for (int i=0; i<v.size(); i++) {
                topoVec.add(v.get(i));
                topoColourVec.add(s);
            }
        }
    }

    private void bigSmasher(Hashtable<String, ArrayList> topoHash) {
        ArrayList v;
        ArrayList indexes;      
        String str;
        Polygon poly1, poly2;
        int[] sharedLine;
        //Iterator it;

        for (Enumeration e = topoHash.keys(); e.hasMoreElements();) {
            str = (String)e.nextElement();
            v = topoHash.get(str);
            indexes = new Vector();
            //for (int i=0; i<v.size(); i++) {
            for(Iterator it = (Iterator)v.elements(); it.hasNext();) {
                poly1 = (Polygon)it.next();


                for(int j=0; j<v.size(); j++) {
                    poly2 = tmpPolyAl.get(j);
                    if(poly1 != poly2) {
                        //System.out.println("diff poly found!");
                        sharedLine = checkPolysEdgeShare(poly1, poly2);
                        if(sharedLine != null) {
                            //System.out.println("just merging a poly whos top point was "+tmpPoly1.xpoints[0]+", "+tmpPoly1.ypoints[0]+" with another polys-- "+tmpPoly2.xpoints[0]+", "+tmpPoly2.ypoints[0]);
                            poly1 = mergePolys2(tmpPoly1, tmpPoly2, sharedLine);
                            //System.out.println("The main merged poly now has this many points : "+tmpPoly1.npoints);
                            indexes.add(poly2);
                            //System.out.println("whats the size FUCKER! "+tmpVec.size());
                        }
                    }
                    else {
                        System.out.println("same poly found");
                    }
                }
                v.removeAll(indexes);

                topoVec.add(poly1);
                topoColourVec.add(str);
            }
        }
    }
       

   /**
    * There is something massively wrong with this next set of iterations. it has
    * something to do with elements being missed out; which could be due to the remove method call
    * shifting the vector back one element. A work-around could be to keep a list of all indexes
    * of elements/polygons that have been added. A polygon would then be checked that it is not a memeber
    * of this list before being added. This means the tmpVec need never be messed with.
    */ 
    private void bigSmasherOld(Hashtable<String, ArrayList> topoHash) {
        int[] sharedLine;   
        for (Enumeration e = topoHash.keys(); e.hasMoreElements();) {
            tmpStr = (String)e.nextElement();
            tmpPolyAl = topoHash.get(tmpStr);
            //System.out.println("----------------------------------------------------");
            //System.out.println("--------Merging polys with height "+tmpStr+"--------");
            //System.out.println("----------------------------------------------------");

            for(int j=0; j<tmpPolyAl.size(); j++) {
                System.out.println("There are "+tmpPolyAl.size()+" polygons of size "+tmpStr);
                tmpPoly1 = tmpPolyAl.get(j);

                //System.out.println("Got a new primary poly*****************");
                /*for(int k=0; k<tmpVec.size(); k++) {
                    tmpPoly2 = (Polygon)tmpVec.elementAt(k);
                    if(tmpPoly1 != tmpPoly2) {
                        //System.out.println("diff poly found!");
                        sharedLine = checkPolysEdgeShare(tmpPoly1, tmpPoly2);
                        if(sharedLine != null) {
                            //System.out.println("just merging a poly whos top point was "+tmpPoly1.xpoints[0]+", "+tmpPoly1.ypoints[0]+" with another polys-- "+tmpPoly2.xpoints[0]+", "+tmpPoly2.ypoints[0]);
                            tmpPoly1 = mergePolys2(tmpPoly1, tmpPoly2, sharedLine);
                            //System.out.println("The main merged poly now has this many points : "+tmpPoly1.npoints);
                            tmpVec.remove(tmpPoly2);
                            //System.out.println("whats the size FUCKER! "+tmpVec.size());
                        }
                    }
                    //else 
                    //    System.out.println("same poly found");
                }*/
                topoVec.add(tmpPoly1);
                System.out.println("A polygon has been added");
                //System.out.println("removing poly with top point "+tmpPoly1.xpoints[0]+", "+tmpPoly1.ypoints[0]);
                tmpPolyAl.remove(tmpPoly1);
                
                topoColourVec.add(tmpStr);
            }
        }
        tmpPolyAl = null;
        tmpPoly1 = null;
        tmpPoly2 = null;
        tmpRect = null;

        System.out.println("And after all the hack'n'slash the topoVecs' size is "+topoVec.size());

        //System.out.println("Lets take a final look at all the masacred polygons");
        /*for(int a=0; a<topoVec.size(); a++) {
            tmpPoly1 = (Polygon)topoVec.elementAt(a);
            System.out.println("");
            System.out.println("Polygon number "+a+" : ");
            for(int i=0; i<tmpPoly1.npoints; i++) {
                System.out.print("("+tmpPoly1.xpoints[i]+","+tmpPoly1.ypoints[i]+")");
            }
        }*/
    }       

    /**
    * This method will work by finding which points two polygons share...if any edges are shared.
    * The array will then be merged according to this information.
    * This method seems like it would be the best one. 
    * Pretty tough to implement though!
    * Like some H-Core DNA bandit the arrays neeed to be SPLICED just right!
    * ....and remember there are NO duplicate points. IDIOT!
    */
    private Polygon mergePolys2(Polygon arg1, Polygon arg2, int[] arg3) {
        int[] test = arg3;
        int[] newX = null;
        int[] newY = null;

        if (test!=null) {
            /*System.out.println("mergePolys2 going ahead with a merge operation!");
            for(int i=0; i<test.length; i++) {
                System.out.print(test[i]+", ");
            }
            System.out.println("/n The first poly is : ");
            for(int i=0; i<arg1.npoints; i++) {
                System.out.print("("+arg1.xpoints[i]+","+arg1.ypoints[i]+")");
            }
            System.out.println("/n The second poly is : ");
            for(int i=0; i<arg2.npoints; i++) {
                System.out.print("("+arg2.xpoints[i]+","+arg2.ypoints[i]+")");
            }
            System.out.println("");*/

            // I now know that two polygons share a side, and which side that is!
            // Now I must splice their arrays at just the right position.
            int[] poly1X = arg1.xpoints;
            int[] poly1Y = arg1.ypoints;

            int[] poly2X = arg2.xpoints;
            int[] poly2Y = arg2.ypoints;

            int index =-1;
            int[] firstPoint = null;

            //find the position in poly(arg1) using the numbers in the array 'test'.
            for(int i=0; i<arg1.npoints; i++) {
                if (i != (arg1.npoints-1)) {
                    if((arg1.xpoints[i]==test[0]) & (arg1.ypoints[i]==test[1]) & (arg1.xpoints[i+1]==test[2]) & (arg1.ypoints[i+1]==test[3])) {
                        //System.out.println("Merge type 1");
                        index = i+1;
                        firstPoint = new int[]{test[0], test[1]};
                    }
                    if((arg1.xpoints[i]==test[2]) & (arg1.ypoints[i]==test[3]) & (arg1.xpoints[i+1]==test[0]) & (arg1.ypoints[i+1]==test[1])) {
                        //System.out.println("Merge type 2");
                        index = i+1;
                        firstPoint = new int[]{test[2], test[3]};
                    }
                }
                else {
                    // check the magic back edge
                    if( (arg1.xpoints[arg1.npoints-1]==test[0]) & (arg1.ypoints[arg1.npoints-1]==test[1]) & (arg1.xpoints[0]==test[2]) & (arg1.ypoints[0]==test[3]) ) {
                        //System.out.println("WARNING-------Merging a back edge!");
                        index = (arg1.npoints-1);
                        firstPoint = new int[]{test[2], test[3]};
                    }
                }
            }
            //System.out.println("first point values "+firstPoint[0]+", "+firstPoint[1]);
            // I should now have an array index, so that I know where to splice the new array!
            // I now need to re-order the elements in the second array so that the splice is seamless
            arrayLooper(poly2X, poly2Y, firstPoint);

            newX = new int[(arg1.npoints+arg2.npoints)-2];
            newY = new int[(arg1.npoints+arg2.npoints)-2];

            int ar1=0;
            for (int i=0; i< newX.length; i++) {
                if(i != index) {
                    newX[i] = arg1.xpoints[ar1];
                    newY[i] = arg1.ypoints[ar1];
                    ar1++;
                }
                else { //don't add the first and last points because we have these already in the first poly
                    for(int j=1; j<(arg2.npoints-1); j++) {
                        newX[i] = arg2.xpoints[j];
                        newY[i] = arg2.ypoints[j];
                        i++;
                    }
                    i--;
                }
            }
            
            // and that should do it?!
            //System.out.println("And the polys array looks like : ");
            for(int i=0; i<newX.length; i++) {
                System.out.print("("+newX[i]+","+newY[i]+")");
            }
        }
        return new Polygon(newX, newY, newX.length);
    }

    /*private void mergePolys4(Shape arg1, Shape arg2) {
        Area area1 = new Area(shp1);
        Area area2 = new Area(shp2);
        System.out.println("seb 2");
        area1.add(area2);
        System.out.println("seb 3");
        shp1 = (Shape)area1;
        System.out.println("seb 4");
        arg1 = (Polygon)area1;
    }*/


    /**
    * IMAGINE arg1 and arg2 are loops of numbers....well arrayLooper is like
    * a pair of hands that spins both loops by the same ammount until the
    * arg3[0] matches arg1[0] and arg3[1] matches arg2[0].
    * Understand? cool.
    */
    private void arrayLooper(int[] arg1, int[] arg2, int[] arg3) {
        int arg1FirstElem;
        int arg2FirstElem;

        while( !((arg1[0] == arg3[0]) & (arg2[0] == arg3[1])) ) {
            arg1FirstElem = arg1[0];
            arg2FirstElem = arg2[0];

            for(int i=0; i< (arg1.length-1); i++) {
                arg1[i] = arg1[i+1];
                arg2[i] = arg2[i+1];
            }
            arg1[arg1.length-1] = arg1FirstElem;
            arg2[arg2.length-1] = arg2FirstElem;
        }
        //System.out.println("arrayLooper ("+arg1[0]+","+arg3[0]+")("+arg2[0]+","+arg3[1]+")");
    }

    /**
    * Checks to see if two polygons share a single edge/ line
    * Will return an array containing int arrays for representing both ends 
    * line.
    */
    private int[] checkPolysEdgeShare(Polygon arg1, Polygon arg2) {
        Line2D.Double tmpLine1 = null;
        Line2D.Double tmpLine2 = null;
        //System.out.println("---------checkPolysEdgeShare starting-----------");
        for(int i=0; i < arg1.npoints; i++) {
                if (i != (arg1.npoints-1)) 
                    tmpLine1 = new Line2D.Double(arg1.xpoints[i], arg1.ypoints[i], arg1.xpoints[i+1], arg1.ypoints[i+1]);
                else 
                    tmpLine1 = new Line2D.Double(arg1.xpoints[arg1.npoints-1], arg1.ypoints[arg1.npoints-1], arg1.xpoints[0], arg1.ypoints[0]);

            for(int j=0; j < arg2.npoints; j++) {
                if (j != (arg2.npoints-1)) 
                    tmpLine2 = new Line2D.Double(arg2.xpoints[j], arg2.ypoints[j], arg2.xpoints[j+1], arg2.ypoints[j+1]);
                else 
                    tmpLine2 = new Line2D.Double(arg2.xpoints[arg2.npoints-1], arg2.ypoints[arg2.npoints-1], arg2.xpoints[0], arg2.ypoints[0]);
                

                if( ( (tmpLine1.getX1() == tmpLine2.getX1()) & (tmpLine1.getY1() == tmpLine2.getY1()) & (tmpLine1.getX2() == tmpLine2.getX2()) & (tmpLine1.getY2() == tmpLine2.getY2()) ) | 
                    ( (tmpLine1.getX1() == tmpLine2.getX2()) & (tmpLine1.getY1() == tmpLine2.getY2()) & (tmpLine1.getX2() == tmpLine2.getX1()) & (tmpLine1.getY2() == tmpLine2.getY1()) ) ) {
                    //System.out.println("Found a matching line");
                    //System.out.println("Line 1 : ("+tmpLine1.getX1()+", "+tmpLine1.getY1()+") ("+tmpLine1.getX2()+", "+tmpLine1.getY2()+")");
                    //System.out.println("Line 2 : ("+tmpLine2.getX1()+", "+tmpLine2.getY1()+") ("+tmpLine2.getX2()+", "+tmpLine2.getY2()+")");
                    return new int[]{(int)tmpLine1.x1, (int)tmpLine1.y1, (int)tmpLine1.x2, (int)tmpLine1.y2};
                }
            }
        }
        //System.out.println("---------checkPolysEdgeShare finished-----------");
        tmpLine1 = null;
        tmpLine2 = null;
        return null;
    }

    /**
    * Checks to see if two polygons share any points
    */
    private boolean checkPolysPointShare(Polygon arg1, Polygon arg2) {
        for(int i=0; i<arg1.npoints; i++) {
            for(int j=0; j<arg2.npoints; j++) {
                if((arg1.xpoints[i]==arg2.xpoints[j]) & (arg1.ypoints[i]==arg2.ypoints[j]))
                    return true;
            }
        }
        return false;
    }
}