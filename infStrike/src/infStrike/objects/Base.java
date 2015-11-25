package infStrike.objects;

import java.awt.geom.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Vector;

/**
* The base is responsible for:
* 	ammunition
* 	healing
* 	platoon creation (within certain range of base)
*/


public class Base {
    private final static double BASE_HEALTH = 100.0;
    private String BASE_NAME;
    private int capacity;      //how many soldiers can it sustain?
    private int platoonSize;
    private Rectangle2D.Double baseRect;
    private Color color;

    private final Point2D.Double Cor;
    int side;
    worldObject world;
    AIController AIC;

    dataCallsigns callsigns;
    private Vector platoons;                   // Vector of aiPlatoons who are stationed at this base.
    //private Point2D.Double[] barrackLocations; //array of points around the base where platoons can wait for missions
    //private String[] barrackOccupiers;

    private barrackLocation[] barrackLocations;

    //utility
    private boolean tempBoo;


    public Base(Point2D.Double Cor, int side, String BASE_NAME, int capacity, int platoonSize, worldObject world, AIController AIC) {
        this.Cor = Cor;
        this.side = side;
        this.BASE_NAME = BASE_NAME;
        this.capacity = capacity;
        this.platoonSize = platoonSize;
        this.world = world;
        this.AIC = AIC;
        baseRect = new Rectangle2D.Double((Cor.x-10), (Cor.y-10), 20, 20);

        callsigns = new dataCallsigns(side);

        
        platoons = new Vector();
        //barrackLocations = new Point2D.Double[10];
        //barrackOccupiers = new String[10];
        barrackLocations = new barrackLocation[10];
        calcBarracks();
    }
    
//**************** significant methods ******************************

    public void makePlatoons() {
        AIPlatoon tempPlatoon = new AIPlatoon(side+"-"+callsigns.makeCallsign(world.platoons[side]), world, AIC);
        for (int i=0; i<world.sides[side].size(); i++) { 
            if(!((Agent)world.sides[side].elementAt(i)).getInPlatoon()) {  //agent not in platoon
                tempPlatoon.addMember((Agent)world.sides[side].elementAt(i));
                ((Agent)world.sides[side].elementAt(i)).setInPlatoon(true);
            }
            if (tempPlatoon.getSize() >= platoonSize) {
                world.platoons[side].addElement(tempPlatoon);
                System.out.println(BASE_NAME+" just created a platoon : ");
                tempPlatoon.printAllMembers();
                tempPlatoon = new AIPlatoon(side+"-"+callsigns.makeCallsign(world.platoons[side]), world, AIC);
            }
        } 
        if (tempPlatoon.getSize() > 0) {
            world.platoons[side].addElement(tempPlatoon);
        }

        tempPlatoon = null;

        /*System.out.println("BASE ++++++++++++++++++++++++++ "+BASE_NAME);
        for (int i=0; i<world.platoons[side].size(); i++) {
            ((AIPlatoon)world.platoons[side].elementAt(i)).printAllMembers();
        }*/
    }

   
    /**
    * Checks to see if a mission is being executed to protect the base.
    * If there is not, or the number of participants is too low
    * then a new/ additional mission is generated.
    */
    public void makeDefenceMission() {
        int num = 2;
        boolean boo = false;
        AIMission mission;

        //System.out.println("Base/makeDefenceMisson - platoons size is : "+platoons.size());
        for (int i=0; i<platoons.size(); i++) {
            mission = ((AIPlatoon)platoons.elementAt(i)).getMission();
            if ( mission.getType().equals("DEFEND") & mission.getBase() == this) {
                boo = true;
                //num++;
            }
            if ( mission.getType().equals("DEFEND_PATROL") & mission.getBase() == this) 
                num--;
        }
        if (!boo)   //no defence!!
            AIC.receiveRequest(new AIRequest(this, "DEFEND", 1.0));

        for (int i = 0; i<num; i++)
            AIC.receiveRequest(new AIRequest(this, "DEFEND_PATROL", (double)1.0/num));
    }

    /**
    * Checks if there are any platoons stationed at this base. If not, generates and posts a transfer
    * mission.
    * you want 2 platoons, 1 for defence and 1 for other missions
    */
    public void makeTransferMission() {
        if (platoons.size() < 2) {
            AIC.receiveRequest(new AIRequest(this, "TRANSFER", 0.2));
        }
    }


    /**
    * Used to end missions by a platoon leader telling the base it has arrived.
    * the base will then give it a free barrack location, random location or
    * transfer mission to other base.
    */
    public void notifyBaseofArrival(AIPlatoon arg1) {
        barrackLocation bLoc = getBarrackLocation(arg1);
        if (bLoc == null) {  //move them on
            System.out.println("Base/notifyBaseofArrival - sending "+arg1.getID()+" away with transfer");
            Base b = AIC.needTransfer();
            AIC.addSpecificMission(arg1, new AIMissionTRANS("TRANSFER", "TRANSFER", b.getCor(), b, this));
        } else {    // give them a barrack to live in
            System.out.println("Base/notifyBaseofArrival - giving "+arg1.getID()+" a barrack");
            bLoc.setOccupier(arg1);
            AIC.addSpecificMission(arg1, new AIMissionWAB("WAIT_AT_BASE", "WAB", bLoc.getLocation(), this));
        }
    }

    /**
    * Platoon leader notifys base that it is departing to ensure that its barrack location
    * is marked as empty.
    */
    public void notifyBaseofDeparture(AIPlatoon arg1) { 
        this.leaveBarrack(arg1);
    }
   
    /**
    * Platoon is transfering to another base so enusre its barrack is made empty
    * and that its reference is removed;
    */
    public void notifyBaseofTransfer(AIPlatoon arg1) {
        this.leaveBarrack(arg1);
        this.removePlatoon(arg1);
    }
    
    /**
    * Takes a platoon and distributes it amoung other waiting platoons to ensure all platoons
    * are at maximum size.
    */
    public void processPlatoon(AIPlatoon arg1) {
    }
//**************** updated methods **********************************

    public void updateGraphics(double time, Graphics2D g2, boolean baseDraw) {
        //super.updateGraphics(time, g2);
        g2.setColor(color);
        //g2.draw(baseRect);
        g2.draw(new Rectangle2D.Double((Cor.x-10), (Cor.y-10), 20, 20));
        if (baseDraw) {
            g2.drawString(BASE_NAME+" - "+platoons.size(), (int)getCor().x, (int)getCor().y);

            for (int i=0; i<barrackLocations.length; i++) {
                g2.fillRect((int)barrackLocations[i].getLocation().x, (int)barrackLocations[i].getLocation().y, 10, 10);
            }
        }
    } 

    public void action(double time) {
        makePlatoons();
        //check defended (and by enough units!)
        makeDefenceMission();
    }


//**************** least important/ complex methods *****************
    /** 
    * Given basicUnitInfo objects, they are converted into Soldiers and added into the world
    * Soldiers added this way are typicaly user defined units. 
    * HINT: bases may make own units ;-)
    */ 
    public void addSoldier(basicUnitInfo bui) {
        if (bui.getSide() == side) {
            world.add(new Soldier(new Point2D.Double((Cor.x + Math.random()*100)-50, (Cor.y+Math.random()*100)-50), bui, world, AIC));
            System.out.println("objects/Base/addSoldier- "+side+" just added "+bui.getName()+" to the ranks");
        }
        else System.out.println("objects/Base/addSoldier- Unit not added. Belongs to wrong side");
    } 

    /**
    * Returns a precalculated barrack location
    * or null if none are available
    * checks for AIP incase the barrack deleagtion system breaks down and fails
    * to stop platoons asking for multiple locations
    */
    public barrackLocation getBarrackLocation(AIPlatoon AIP) {
        if (!alreadyPresent(AIP)) {

        for (int i=0; i<barrackLocations.length; i++) {
            if (barrackLocations[i].isOccupied()==false) {
                return barrackLocations[i];
            }
        }
        }
        return null;
    }

    private boolean alreadyPresent(AIPlatoon arg1) {
        for (int i=0; i<barrackLocations.length; i++) {
            if (barrackLocations[i].isOccupied() && barrackLocations[i].getOccupier().getID().equals(arg1.getID())) {
                return true;
            }
        }
        return false;
    }

    public void occupyBarrack(AIPlatoon platoon, Point2D.Double bLocation) {
        for (int i=0; i<barrackLocations.length; i++) {
            if (barrackLocations[i].getLocation().equals(bLocation)) {
                barrackLocations[i].setOccupier(platoon);
                this.addPlatoon(platoon);
            }
        }
    }

    public void leaveBarrack(AIPlatoon platoon) {
        for (int i=0; i<barrackLocations.length; i++) {
            if (barrackLocations[i].getOccupier() == platoon) {
                barrackLocations[i].removeOccupier();
            }
        }
    }

    /**
    * populates the barrackLocations array with points near the bases location
    * essential like the landing pads in Apache-Havoc / Commanche-Hokum
    */
    private void calcBarracks() {
        System.out.println("Base/calcBarracks - calculating barracks");
        int offsetX = 50;
        int offsetY = 50;
        int cnt = 1;
        
        for(int i=0; i<barrackLocations.length; i++) {
            barrackLocations[i] = new barrackLocation(new Point2D.Double(Cor.x-150+(offsetX*cnt), (Cor.y-((barrackLocations.length/4)*offsetY))+(offsetY*(i/2))));
            barrackLocations[i].removeOccupier();
            System.out.println("Base/calcBarracks - barrack at : "+barrackLocations[i].getLocation().x+" , "+barrackLocations[i].getLocation().y);
            cnt++;
            if (cnt ==3)
                cnt = 1;
        }
    }

    /**
    * Calculates the current number of agents affiliated to this base
    */  
    public int calcMembers() {
        int tempInt = 0;
        for(int i=0; i<platoons.size(); i++) {
            tempInt += ((AIPlatoon)platoons.elementAt(i)).getSize();
        }
        return tempInt;
    }

    /**
        Add a platoon to the vector of platoons currently stationed at this base.
    */
    public void addPlatoon(AIPlatoon platoon) {
        if (!platoons.contains(platoon)) {
            platoons.addElement(platoon);
        }

    }
    private void removePlatoon(AIPlatoon platoon) {
        int index = -1;
        for (int i=0; i<platoons.size(); i++) {
            if( ((AIPlatoon)platoons.elementAt(i)).getID().equals(platoon.getID()) ) {  //check if platoon not already registered
                index = i;
            }
        }
        if (index != -1)
            platoons.removeElementAt(index);
    }


    public void setColor(Color color) { this.color = color; }
    public int getSide() { return side; }    
    /**
    * Cor is cloned instead of just returned to stop the base wandering around with the agents.
    * You remember that don't you!!!!!! >:(
    */
    public Point2D.Double getCor() { return (Point2D.Double)Cor.clone(); }
    public String getName() { return BASE_NAME; }
}

class barrackLocation {

    private Point2D.Double location;
    private AIPlatoon occupier;

    public barrackLocation(Point2D.Double location) {
        this.location = location;
    }

    public boolean isOccupied() {
        if (occupier != null)
            return true;
        else
            return false;
    }
    
    public Point2D.Double getLocation() { return (Point2D.Double)location.clone(); }
    public AIPlatoon getOccupier() { return occupier; }
    public void setOccupier(AIPlatoon occupier) { this.occupier = occupier; }
    public void removeOccupier() { occupier = null; }
}