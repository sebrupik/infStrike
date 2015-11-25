package infStrike.objects;

import java.awt.geom.Point2D;

public class AIMissionRTB extends AIMission {

    public AIMissionRTB(String type, String ID, Point2D.Double[] waypoints, Base base) {
        super(type, ID, waypoints, base);
        
    }
    public AIMissionRTB(String type, String ID, Point2D.Double arg2, Base base) {
        this(type, ID, new Point2D.Double[]{arg2}, base);
    }

    public void endMission(AIPlatoon arg1) {
        getBase().notifyBaseofArrival(arg1);
        this.setWaiting(true);
    }
}