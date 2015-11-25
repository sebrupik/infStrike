package infStrike.objects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.*;
import java.awt.geom.Point2D;

/**
* Feature Object-
* Base class for non-moving polygon based objects (forests, lakes, etc)
*/

public abstract class featObj {
    private int npoints;
    private int[] x;
    private int[] y;
    private Polygon poly;
    private Color color;
    private String name;

    private String tmpStr;

    public featObj(int arg1, int[] arg2, int[] arg3, Color arg4, String arg5) {
        this.npoints = arg1;
        this.x = arg2;
        this.y = arg3;
        this.color = arg4;
        this.name = arg5;

        poly = new Polygon(x, y, npoints);
    }
 
    public void updateGraphics(double time, Graphics2D g2) {
        g2.setColor(color);
        g2.fillPolygon(poly);
    }

    public int npoints() { return npoints; }
    public int[] getX() { return x; }
    public int[] getY() { return y; } 
    public Polygon getPoly() { return poly; }
    public String getName() { return name; }

    public boolean containsPoly(Rectangle2D arg1) { return poly.intersects(arg1); }
    public boolean contains(Point2D.Double arg1) { return poly.contains(arg1); }

    public int getNumPoints() { return poly.npoints; }

    public String getXPoints() {
        tmpStr = "";
        for(int i=0; i<npoints; i++) {
            tmpStr += Integer.toString(x[i]);
            if((i+1) != npoints)
                tmpStr += ", ";
        }
        return tmpStr;
    }
    public String getYPoints() {
        tmpStr = "";
        for(int i=0; i<npoints; i++) {
            tmpStr += Integer.toString(y[i]);
            if((i+1) != npoints)
                tmpStr += ", ";
        }
        return tmpStr;
    }
}