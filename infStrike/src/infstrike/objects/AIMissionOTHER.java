package infStrike.objects;

import java.awt.geom.Point2D;

public class AIMissionOTHER extends AIMission {

    public AIMissionOTHER(String type, String ID, Point2D.Double[] waypoints, Base base) {
        super(type, ID, waypoints, base); 
    }
    public AIMissionOTHER(String type, String ID, Point2D.Double arg2, Base base) {
        this(type, ID, new Point2D.Double[]{arg2}, base);
    }
 
    public void endMission(AIPlatoon arg1) { arg1.setMission(new AIMissionRTB("RTB", "RTB", getBase().getCor(), getBase())); }
}