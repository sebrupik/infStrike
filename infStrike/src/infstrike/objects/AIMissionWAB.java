package infStrike.objects;

import java.awt.geom.Point2D;

public class AIMissionWAB extends AIMission {

    public AIMissionWAB(String type, String ID, Point2D.Double[] waypoints, Base base) {
        super(type, ID, waypoints, base);   
    }
    public AIMissionWAB(String type, String ID, Point2D.Double arg2, Base base) {
        this(type, ID, new Point2D.Double[]{arg2}, base);
    }

    public void startMission(AIPlatoon arg1) {  
        getBase().occupyBarrack(arg1, getFinalWaypoint());
        //setWaiting(true);
    }
    public void endMission(AIPlatoon arg1) { setWaiting(true); }
}