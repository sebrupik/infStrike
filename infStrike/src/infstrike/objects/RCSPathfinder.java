package infStrike.objects;

import java.awt.geom.Point2D;

public class RCSPathfinder {
    private worldObject world;

    public RCSPathfinder(worldObject world) {
        this.world = world;
    }


    public Point2D.Double makeDefence(Point2D.Double arg1) {
        //return new Point2D.Double[]{arg1};
        return arg1;
    }

    /**
    * return a four point patrol centered around center.
    */
    public Point2D.Double[] makeDefencePatrol(Point2D.Double center) {
        Point2D.Double[] pAr = new Point2D.Double[5];
        int dist = 150;

        pAr[0] = new Point2D.Double(center.x-dist, center.y-dist);
        pAr[1] = new Point2D.Double(center.x+dist, center.y-dist);
        pAr[2] = new Point2D.Double(center.x+dist, center.y+dist);
        pAr[3] = new Point2D.Double(center.x-dist, center.y+dist);
        pAr[4] = new Point2D.Double(center.x-dist, center.y-dist);

        return pAr;
        //return center;
    }
}