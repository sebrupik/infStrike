package infStrike.objects;

import java.awt.geom.Point2D;

public class AIMissionTRANS extends AIMission {
    private Base origin;

    public AIMissionTRANS(String type, String ID, Point2D.Double[] waypoints, Base destination, Base origin) {
        super(type, ID, waypoints, destination);
        this.origin = origin;
    }
    public AIMissionTRANS(String type, String ID, Point2D.Double arg2, Base destination, Base origin) {
        this(type, ID, new Point2D.Double[]{arg2}, destination, origin);
    }

    public void startMission(AIPlatoon arg1) {  
        origin.notifyBaseofTransfer(arg1);
    }

    public void endMission(AIPlatoon arg1) {  
        getBase().notifyBaseofArrival(arg1);
        this.setWaiting(true);
    }
}