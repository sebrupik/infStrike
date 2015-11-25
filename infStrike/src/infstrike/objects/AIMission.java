package infStrike.objects;

import java.awt.geom.Point2D;

public abstract class AIMission {
    private String type;
    private String ID;
    private Point2D.Double[] waypoints;
    private Base base;
    private int curWay;

    private Point2D.Double tempPoint;
    private boolean finished;  //mission is considered finished when the last waypoint is reached
    private boolean waiting;   //waiting is true if the platoon is to prevented for asking for more missions
    private boolean waitingSpecific;

    public AIMission(String type, String ID, Point2D.Double[] waypoints, Base base) {
        this.type = type;
        this.ID = ID;
        this.waypoints = waypoints;
        this.base = base;
        curWay = 0;
        //System.out.println("AIMission - New mission created "+type+" with "+waypoints.length+" waypoints");

        
    }
    public AIMission(String type, String ID, Point2D.Double arg2, Base base) {
        this(type, ID, new Point2D.Double[]{arg2}, base);
    }

    
    /**
    * If the point arg1 equals the point indexed by 'curWay' in the 'waypoints'
    * then the current waypoint has been reached so curWay is incremented by one 
    * and the next waypoint given.
    */
    public Point2D.Double getNextWaypoint(Point2D.Double arg1) { 
        if (arg1 == waypoints[curWay]) {                 //current waypoint reached
            if(arg1 != waypoints[waypoints.length-1]) {  // is it the last point?
                curWay++;
            }
            else { finished = true; }
        }
        return waypoints[curWay];
    }

    /**
    * When mission starts base is notified that platoon is leaving, so its occupied barrack
    * location is marked as vacant.
    */
    public void startMission(AIPlatoon arg1) { base.notifyBaseofDeparture(arg1); }

    /**
    * Base is notifyed of platoons arrival, so that endMission can be issued. this will either 
    * a waiting at base or a transfer mission if the base is at capacity.
    */
    public void endMission(AIPlatoon arg1) { base.notifyBaseofArrival(arg1); }

    //********utility
    public Point2D.Double getCurWaypoint() { return waypoints[curWay]; } 
    public Point2D.Double getFinalWaypoint() { return waypoints[waypoints.length-1]; } 

    public void setWaiting(boolean waiting) { this.waiting = waiting; }
    public void setWaitingSpecific(boolean waitingSpecific) { this.waitingSpecific = waitingSpecific; }
    public boolean getWaiting() {  return waiting; }
    public boolean getWaitingSpecific() {  return waitingSpecific; }

    public String getType() { return type; }
    public String getID() { return ID; }
    public boolean getFinished() { return finished; }    
    public Base getBase() { return base; }
}