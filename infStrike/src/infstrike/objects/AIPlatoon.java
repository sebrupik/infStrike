package infStrike.objects;

import java.awt.geom.Point2D;
import java.util.Vector;
import java.awt.Graphics2D;


public class AIPlatoon {
    private String platoonID;
    private Agent[] members;
    private AIMission mission;
    private AIFormation formation;

    private worldObject world;
    private AIController AIC;

    private boolean requestedMission;
    

    public AIPlatoon(String platoonID, worldObject world, AIController AIC) {
        this.platoonID = platoonID;
        this.world = world;
        this.AIC = AIC;
        this.members = null;
    }

    public void addMember(Agent arg1) { 
        Agent[] members2;

        if (members == null) {
            members2 = new Agent[1];
        }
        else {
            members2 = new Agent[members.length+1];
            for (int i=0; i<members.length; i++) {
                members2[i] = members[i];
            }
        }
        members2[members2.length-1] = arg1;
        members = members2;
        members2 = null;
    }

    /**
    * Sets the platoons mission to that passed in the argument
    * The platoons formation is then adjusted depending on the mission type.
    */
    public void setMission(AIMission mission) {
        System.out.println("AIPlatoon/setMission - "+platoonID+" mission being set : "+mission.getType());
        this.mission = mission; 
        requestedMission = false;  //mission received so request must have been granted
         
        if(mission.getType().equals("DEFEND")) {   // box
            this.formation = new AIFormation(this.getSize(), "WEDGE", this.getLeader().getCor(), this.getLeader().getRot());
        }
        else if (mission.getType().startsWith("Return to base")) {  //column
            this.formation = new AIFormation(this.getSize(), "COLUMN", this.getLeader().getCor(), this.getLeader().getRot());
        }
        else {  //default formation was WEDGE
            this.formation = new AIFormation(this.getSize(), "WEDGE", this.getLeader().getCor(), this.getLeader().getRot());
        }
        //start the mission, which notifies the base of departure.
        this.mission.startMission(this);
    }

    public void updateGraphics(double time, Graphics2D g2, boolean platoonDraw) {
        if (platoonDraw)
            g2.drawString(platoonID+" : "+members.length+" : "+getLeader().getName()+" : "+mission.getType(), (int)getLeader().getCor().x, (int)getLeader().getCor().y);
    }
    

    public void action(double time) {

        //leader is dead so remove and reset the formation 
        
        
        // no mission so wait by nearest base
        if (mission == null) {
            this.setMission(new AIMissionRTB("RTB", "Return to base "+((int)(Math.random()*1000)), AIC.getNearestBase(this.getLeader()).getCor(), AIC.getNearestBase(this.getLeader()) ));
        }

        //request a mission from the blackboard
        // these conditions are true if the current mission is a WAB, RTB, TRANS
        if ( mission.getFinished() & mission.getWaiting() & !requestedMission) { 
        //if ( mission.getFinished() & !requestedMission) { 
            System.out.println("AIPlatoon/action - "+platoonID+" requesting mission");
            requestedMission = true;
            AIC.receiveRequest(new AIRequest(this, "MISSION", 1.0));
        }
        
        
        // preform checks to see if members have reached the current waypoint
        //System.out.println(platoonID+"  Cor : "+this.getLeader().getCor()+" // Des : "+this.getLeader().getDes());
        if ( this.getLeader().getCor().equals(this.getLeader().getDes()) ) {  
            //System.out.println("AIPlatoon - "+platoonID+" need a new waypoint");
            this.getLeader().setDes(mission.getNextWaypoint(this.getLeader().getCor()));
        }
        
  
        //if mission has ended, notify base that issued the mission to request a final waypoint      
        if ( mission.getFinished() & !mission.getWaiting() & !mission.getWaitingSpecific()) {
            System.out.println(this.getID()+" is recieving an end mission");
            mission.endMission(this);
        }
        
        //update formation and rotation
        if (this.formation != null) {
            if (!this.getLeader().getWaiting()) {  //if the leader is stationary, no need to update destinations
                this.formation.update(this.getLeader().getCor(), this.getLeader().getRot());
                setPlatoonDes();
            }
        }
    }

    /**
    * Set the destination of all platoon memebers except for the leader.
    */
    private void setPlatoonDes() {
        for (int i=1; i<members.length; i++) {
            members[i].setDes(this.formation.getSpecificPosition(i));
        }
    }

//*******************************
    public String getMissionType() { return mission.getType(); }
    public int getSize() { 
        if (members !=null) { return members.length;
        } else { return 0; }
    }
    public String getID() { return platoonID; }
    public String getIDLeader() { return platoonID+"<"+this.getLeader().getName()+">"; }
    public AIMission getMission() { return mission; }
    public Point2D.Double getLeaderLocation() { return this.getLeader().getCor(); }
    public Agent getLeader() { return members[0]; }
    public Base getBase() { return mission.getBase(); }



    public void printAllMembers() {
        String str;
        System.out.println("Platoon name : "+platoonID+" with "+members.length+" members.");
        for (int i=0; i<members.length; i++) {
            System.out.println( members[i].getName() );
        }
    }

    //public 
}