package infStrike.objects;

import java.awt.Graphics2D;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Iterator;

import java.util.Hashtable;
import java.util.Enumeration;

/**
* The AI component of the RCS.
* It will maintain representations 
*/

public class RCSController extends AIController{

    //character variables
    private double trait_aggresion = 0.6; //0.0 (defends) - 1.0 (attacks)
    private double trait_flexability; //0.0(sticks rigidly to plan) - 1.0 (off the cuff) --- 0.5 (ideal)
    private double trait_caution;  //0.0 (underestimates enemy numbers)  - 1.0 (over estimates enemy numbers)  ---- 0.5 (ideal)

    private Blackboard bb;  //blackboard
    private RCSInfluenceMap IM;
    private RCSPathfinder PF;

    private int mission_defence;
    private int mission_wait_at_base;
    private int mission_defence_patrol;
    private int mission_transfer;
    private int mission_support;
    private int mission_fire_support;
    private int mission_attack;
    private int mission_seek_and_destroy; 
    private int mission_recon;  // go to a point and return
    private int mission_locate; // go to a point and circle concentricaly outwards and then return

    private ArrayList platoonRequests;
    private Hashtable platoonRequestsHT;
    private ArrayList tempAL;
    private ArrayList intelligence;

    private final String type_FRONT = "FRONT";
    private final String type_FLANK = "FLANK";
    private final String type_REAR = "REAR";
    private final String type_UNKNOWN = "UNKNOWN";

    //NB add new mission types to AIMP!!
    private final String mission_MISSION = "MISSION";
    private final String mission_WAIT_AT_BASE = "WAIT_AT_BASE";
    private final String mission_ATTACK = "ATTACK";
    private final String mission_ATTACK_LOCATION = "ATTACK_LOCATION";
    private final String mission_DEFEND = "DEFEND"; 
    private final String mission_DEFEND_PATROL = "DEFEND_PATROL"; 
    private final String mission_SUPPORT = "SUPPORT";
    private final String mission_FIRE_SUPPORT = "FIRE_SUPPORT";
    private final String mission_TRANSFER = "TRANSFER";
    private final String mission_RECON = "RECON";
    //NB add new mission types to AIMP!!

    private AIMissionPriority AIMP;


    public RCSController(int side, worldObject world) {
        super(side, world);
  
        this.bb = new Blackboard(side);
        this.IM = new RCSInfluenceMap(world.getWidth(), world.getHeight(), world.getArenaPlan().getGridsize(), type_FRONT, type_FLANK, type_REAR, type_UNKNOWN);
        this.PF = new RCSPathfinder(world);

        this.platoonRequests = new ArrayList();
        this.platoonRequestsHT = new Hashtable();
        this.intelligence = new ArrayList();

        this.AIMP = new AIMissionPriority(new String[]{mission_MISSION, mission_WAIT_AT_BASE, mission_ATTACK, mission_ATTACK_LOCATION, 
                                          mission_DEFEND, mission_DEFEND_PATROL, mission_SUPPORT, mission_FIRE_SUPPORT, 
                                          mission_TRANSFER, mission_RECON});

        //test info
        //intelligence.add(new AIIntelligence(null, null, new java.awt.geom.Point2D.Double(1000.0, 1000.0), -50.0, 100.0  ));
        //intelligence.add(new AIIntelligence(null, null, new java.awt.geom.Point2D.Double(1500.0, 1500.0), -50.0, 100.0  ));
    }

    public void updateGraphics(double time, Graphics2D g2, boolean AIDraw) {
        //super.updateGraphics(time, g2);
        if (AIDraw) {
            //System.out.println("Controller colour is "+g2.getColor().toString()+" for side "+getSide());
            IM.updateGraphics(time, g2);
        }
    }


    /**
    * Missions given varying priorites depending on current tactical situation (hostile, defeding, attacking)
    * and tacticians character traits.
    * Perhaps the method calls in this method should not be called in one itteration as it may have
    * an adverse affect on performance. Maybe one method for each itteration, and once they have all been 
    * executed put the newly created missions on the blackboard at once.
    * The more this method can be split between time steps the better the performance!
    */
    public void action(double time) {
        //System.out.println("*********************************** RCSControler for updating for side "+getSide());
        //take recon data from blackboard and use it to build and maintain influence map.
        //if (time == 5.25) {
            this.IM.refreshInfluence(getWorldObject().platoons[getSide()], intelligence);
            this.IM.refreshFFR();
        //}

        //create defence mission for objective buildings

        //create attack missions for sighted enemies
        issueMissions(IM.getFriendlyPositions());

        //create recon mission which avoid or lightly probe the occupied areas

        //create search and destroy mission    


        //issuse the missions
        processMissionRequests(platoonRequestsHT); 

        // remove out-of-date intelligence
        trimOldIntelligence(time);
    }

    /**
    * itterates through 'friendlyPositions' finding all fronts and assesing their 
    * tactical standing using the arguments passed in this method.
    * Subsequent mission will be either attack, support or move types.
    */
    public void issueMissions(Vector friendlyPositions) {
        RCSInfluenceMapPosition pos1;
        for (int i=0 ;i<friendlyPositions.size(); i++) {
            pos1 = (RCSInfluenceMapPosition)friendlyPositions.elementAt(i);   
            if (pos1.getType().equals(type_FRONT)) {
                if (pos1.getTacticalStanding() * ((2*trait_aggresion)-1) > 0.0 ) {   //can attack
                    issueAttackMissions(pos1.getOwns(), 0.0);
                }
                else {  //should reposition its platoons to make it tacticaly stronger
                    //System.out.println("support missions to be issued "+pos1.getTacticalStanding() * ((2*trait_aggresion)-1));
                }
            }
        }
    }

    /**
    * the argument 'filter' is used to decide which clusters can attack
    */
    private void issueAttackMissions(Vector v1, double filter) {
        //System.out.println("RCSController/issueAttackMissions - begining execution");
        RCSInfluenceMapPosition pos1;
        RCSInfluenceMapPosition pos2;

        for (int i=0; i<v1.size(); i++) {
            pos1 = (RCSInfluenceMapPosition)v1.elementAt(i);
            if (pos1.getTargets().size() > 0) {
                if (((objectTarget)pos1.getTargets().elementAt(0)).attack_value > filter ) { 
                    pos2 = ((objectTarget)pos1.getTargets().elementAt(0)).target;
                    bb.addPlatoonSpecificMission(pos1.availableForMission(new AIMissionOTHER(mission_ATTACK_LOCATION, "Special Attack", new java.awt.geom.Point2D.Double(pos2.getCenter()[0], pos2.getCenter()[1]), super.getNearestBase(pos2.getCenter()))));
                }
            }
        }
    }

    
    /**
    * 
    */
    public void receiveRequest(AIRequest arg1) { 
        //System.out.println("RCSController"+this.getSide()+"Request from "+arg1.getRequester().getClass()+" for "+arg1.getRequest() );
        processRequest(arg1);
    }

    public void addSpecificMission(AIPlatoon platoon, AIMission mission) {
        bb.addPlatoonSpecificMission(new BlackboardPlatoonSpecificMission(platoon.getID(), mission));
    }

    /**
    * Method for accepting primarily enemy sightings information, which is used
    * in constructing the influence map.
    * If the object of the intel has already been reported the value indicated by the 
    * intel, the time of the report and controllers traits will determine which report
    * to use.
    */
    public void receiveIntelligence(AIIntelligence arg1) {
        double tDoub = 0.0;
        boolean tBoo = false;
        int index = -1;

        //check you don't have similar intelligence already...cheating below!!
        for (int i=0; i<intelligence.size(); i++) {
            if ( ((AIIntelligence)intelligence.get(i)).getWhat().equals(arg1.getWhat()) ) {  //is it the same?
                if ( ((AIIntelligence)intelligence.get(i)).getTime() < arg1.getTime() ) {  //is it newer ?
                    tBoo = true;
                }
                //whats the value difference ?  if tInt is negative new info has higher value
                tDoub = arg1.getValue() - ((AIIntelligence)intelligence.get(i)).getValue();
                index = i;
            }
        }

        // controller decides if intel is to be added
        // if info is newer or of higher value then added it
        if (tBoo | tDoub > 0) {
            if (index != -1) {
                intelligence.remove(index);
                intelligence.add(arg1);
            }
        }
    }

    /**
    * remove old intelligence, but send a recon mission to location just to make sure.
    * but cancel all pending specific ATTACK_LOCATION missions
    */
    private void trimOldIntelligence(double time) {
        double intellAge = 200;
        AIIntelligence AIC;

        for (Iterator e = intelligence.iterator() ; e.hasNext() ;) { 
            AIC = (AIIntelligence)e.next();
            if ( (time - AIC.getTime()) > intellAge) {
                mission_recon++;
                bb.removePlatoonSpecificMission(mission_ATTACK_LOCATION, AIC.getWhere());
                bb.OMPut(new AIMissionOTHER(mission_RECON, mission_RECON+" "+String.valueOf(mission_recon), AIC.getWhere(), this.getNearestBase(new double[]{AIC.getWhere().x, AIC.getWhere().y}) ));
                e.remove();
            }
        }
    }

    /**
    * Given a request, a mission is generated depending on character traits
    */
    private void processRequest(AIRequest arg1) {
        if (arg1.getRequester() instanceof Base) {
            if( arg1.getRequest().equals(mission_TRANSFER) ) {
                mission_transfer++;
                Base b = this.needTransfer();
                bb.OMPut(new AIMissionTRANS(mission_TRANSFER, mission_TRANSFER+" "+String.valueOf(mission_transfer), b.getCor(), b, (Base)arg1.getRequester()));
                System.out.println("RCSController/processRequest - Adding a TRANSFER mission for "+((Base)arg1.getRequester()).getName());
            }
            else if ( arg1.getRequest().equals(mission_DEFEND) ) {  //only one defend mission should be posted at any one time
                if (bb.OMContains(mission_DEFEND, ((Base)arg1.getRequester()).getName()) == 0) {
                    mission_defence++;
                    bb.OMPut(new AIMissionOCCUPY(mission_DEFEND, "DEFEND "+String.valueOf(mission_defence), PF.makeDefence(((Base)arg1.getRequester()).getCor()), (Base)arg1.getRequester()));
                    System.out.println("RCSController/processRequest - Adding a DEFEND mission for "+((Base)arg1.getRequester()).getName());
                }
            }
            else if ( arg1.getRequest().equals(mission_DEFEND_PATROL) ) {  //only two defnece patrols per base
                if (bb.OMContains(mission_DEFEND_PATROL, ((Base)arg1.getRequester()).getName()) <= 1) {
                    mission_defence_patrol++;
                    bb.OMPut(new AIMissionOTHER(mission_DEFEND_PATROL, "DEFEND PATROL "+String.valueOf(mission_defence_patrol), PF.makeDefencePatrol(((Base)arg1.getRequester()).getCor()), (Base)arg1.getRequester()));
                    System.out.println("RCSController/processRequest - Adding a DEFEND PATROL mission for "+((Base)arg1.getRequester()).getName());
                }
            }
            else if ( arg1.getRequest().equals(mission_SUPPORT) ) {
                mission_support++;
            }
        }
        else if (arg1.getRequester() instanceof AIPlatoon) {
            if( arg1.getRequest().equals(mission_MISSION) ) {
                System.out.println("RCSController/processRequest - mission request has been added.");

                if (platoonRequestsHT.containsKey( ((AIPlatoon)arg1.getRequester()).getBase().getName() ) ) {
                    tempAL = (ArrayList) platoonRequestsHT.get(((AIPlatoon)arg1.getRequester()).getBase().getName());
                    tempAL.add(arg1.getRequester());
                } else {
                    tempAL = new ArrayList();
                    tempAL.add(arg1.getRequester());
                    platoonRequestsHT.put(((AIPlatoon)arg1.getRequester()).getBase().getName(), tempAL);
                }

            }
            else if ( arg1.getRequest().equals(mission_SUPPORT) ) {
                mission_support++;
            }
            else if ( arg1.getRequest().equals(mission_FIRE_SUPPORT) ) { //specialist mission for mortars, artillery or tanks!
                mission_fire_support++;
            }
        }
    }

    /**
    * The controller must choose a suitabe mission
    * prehaps have a 2 element array. The first containing a double 0.0-1.0 represeting
    * priority and the 2nd element the mission type.
    * pass this info to the balckboard to try and get relevant missions.
    * how do I come up with these numbers?!
    */
    private void processMissionRequests(Hashtable ht) {
        String tmpStr; 

        for (Enumeration e = ht.keys() ; e.hasMoreElements() ;) {
             tmpStr = (String)e.nextElement();
             bb.getMissions((ArrayList)ht.get(tmpStr), AIMP, tmpStr);  //pass the arraylist and base name
        }
    }
}