package infStrike.objects;

import java.awt.Color;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

public class RCSInfluenceMapPosition {
    private double totalValue;
    private double[] center = new double[2];
    private Vector positions;               // vector of double arrays containing co-ords for all member AIPlatoons
    private Vector platoons;                // all the AIPlatoons memebrs of this positions
    private String type;                    // front, flank, rear
    private RCSInfluenceMapPosition owner;  // FLANKS ONLY! the FRONT that this FLANK belongs to.
    private RCSInfluenceMapPosition enemy;  // the closest enemy front
    private Vector owns;                    // FRONTS ONLY! vector for postions owned by this FRONT
      
    private Vector targets;                 // vector of objectTargets'
    private double tactical_standing;

    private double[] enemyPos;
    private double umb = -1.0;
    Color c1;

    private int amplifier, amplified;

    public RCSInfluenceMapPosition(double[] arg1, String type) {
        this.center = new double[2];
        this.positions = new Vector();
        this.platoons = new Vector();
        this.addPosition(arg1);

        this.amplifier = 1;
        this.type = type;

        this.owns = new Vector();
        this.targets = new Vector();
    }

    public void updateGraphics(Graphics2D g2) {
        c1 = g2.getColor();
        amplified = (int)totalValue * amplifier;
        g2.drawOval((int)(center[0]-(amplified/2)), (int)(center[1]-(amplified/2)), amplified, amplified);
 
        double[] tDoub;
        for (int i=0; i<positions.size(); i++) {
            tDoub = (double[])positions.elementAt(i);
            g2.drawLine((int)center[0], (int)center[1], (int)tDoub[1], (int)tDoub[2]);
            g2.drawString(type+" ("+totalValue+")"+"("+tactical_standing+")", (int)center[0], (int)center[1]);
        }

        RCSInfluenceMapPosition pos1;
        g2.setColor(Color.yellow);
        for (int i=0; i<owns.size(); i++) {
            pos1 = (RCSInfluenceMapPosition)owns.elementAt(i);
            g2.drawLine((int)center[0], (int)center[1], (int)pos1.getCenter()[0], (int)pos1.getCenter()[1]);
        }
        g2.setColor(c1);


        if (enemyPos != null) {
            g2.setColor(Color.white);
            g2.drawLine((int)center[0], (int)center[1], (int)enemyPos[0], (int)enemyPos[1]);
            if (umb != -1) {
                g2.drawOval( (int)(center[0]-(umb/2)), (int)(center[1]-(umb/2)), (int)umb, (int)umb );
            }
            g2.setColor(c1);
        }
    }
   
    /**
    * Add a new position so the center and value need to be
    * recalculated
    */
    public void addPosition(double[] position) {
        positions.addElement(position);
        recalculate();
    }

    public void addPlatoon(AIPlatoon aip) {
        platoons.addElement(aip);
    }

    /**
    * Recalculate the center and total influcence value for this cluster of platoons
    */
    private void recalculate() { 
        double[] tDoub = (double[])positions.elementAt(0);

        totalValue = tDoub[0];
        center[0] = tDoub[1];  // x
        center[1] = tDoub[2];  // y
        
        for (int i=1; i<positions.size(); i++) {
            tDoub = (double[])positions.elementAt(i);
            totalValue += tDoub[0];
            center[0] += tDoub[1];  // x
            center[1] += tDoub[2];  // y
        }
 
        center[0] = center[0]/positions.size();
        center[1] = center[1]/positions.size();
    }

    /**
    * Given a co-ord checks to see if a line from it passes through its influence circle to
    * the subsequent flanks associated with it. If it does then it is a rear unit.
    */
    public void passesThrough(RCSInfluenceMapPosition enemy, String type_REAR) {
        this.enemy = enemy;
        this.enemyPos = enemy.getCenter();

        RCSInfluenceMapPosition pos1;
        Rectangle2D.Double rect = new Rectangle2D.Double(center[0]-(totalValue/2), center[1]-(totalValue/2), totalValue, totalValue);
 
        // i =1 because the first element (0) is the front itself.
        for (int i=1; i<owns.size(); i++) {
            pos1 = (RCSInfluenceMapPosition)owns.elementAt(i);
             
            if ( rect.intersectsLine(enemyPos[0], enemyPos[1], pos1.getCenter()[0], pos1.getCenter()[1])) {
                pos1.setType(type_REAR);
            }
        }
    }

    /**
    * Method only to be used by RCSIMPs designated as fronts
    */
    public void calcAllAttackValues(String[] noAttack) {
        targets.clear();             //is this nessecary?
        for (int i=0; i<owns.size(); i++) { 
            enemy.calcAttackValue((RCSInfluenceMapPosition)owns.elementAt(i), noAttack);
        }
        calcTacticalStanding();
        //System.out.println("do I have any targets? : "+targets.size());
    }

    /**
    * Given pos1 the attack values are calculated between it and the positions
    * held in 'owns' (pos2).
    * the attack_value and pos2 are then added to result[] and added to pos1's
    * 'targets' vector.
    */
    public void calcAttackValue(RCSInfluenceMapPosition pos1, String[] noAttack) {
        RCSInfluenceMapPosition pos2;
        double attack_value;

        for (int i=0; i<owns.size(); i++) {
            pos2 = (RCSInfluenceMapPosition)owns.elementAt(i); 
            if ( isMember(pos2.getType(), noAttack) == false ) {
                attack_value = (pos1.getTotalValue()-pos2.getTotalValue()) / java.awt.geom.Point2D.distance(pos1.getCenter()[0], pos1.getCenter()[1], pos2.getCenter()[0], pos2.getCenter()[1]);
                pos1.addDoubleHigh(pos1.getTargets(), new objectTarget( attack_value, pos2 ));
            }
        }
    }

    private void calcTacticalStanding() {
        double tDoub = 0.0;

        for (int i=0; i<owns.size(); i++) {
            tDoub += ((RCSInfluenceMapPosition)owns.elementAt(i)).getTotalValue();
        }
        tactical_standing = tDoub/owns.size();
    }

    /**
    * Method to force all platoons who are members of this RCSIMP to take the new mission
    * I can only assume that this method would be used if a base was under attack
    */
    public void recieveMission(AIMission mission) {
        System.out.println("A mission is being recieved of type "+mission.getType());
        for (int i=0; i<platoons.size(); i++) {
            ((AIPlatoon)platoons.elementAt(i)).setMission(mission);
        }
    }

    /**
    * Method to get the names of platoons who are waiting, and are therefore
    * available for the new mission
    * returns a vector of BlackboardPlatoonSpecificMission's
    */
    public Vector availableForMission(AIMission mission) {
        Vector v = new Vector();
        AIPlatoon aip;

        for (int i=0; i<platoons.size(); i++) {
            aip = (AIPlatoon)platoons.elementAt(i);
            if (aip.getMission() != null && aip.getMission().getWaiting()) 
                v.addElement(new BlackboardPlatoonSpecificMission(aip.getID(), mission));
            
        }
        return v;
    }


    private boolean isMember(String what, String[] noAttack) {
        for (int i=0; i<noAttack.length; i++) {
            if (what.equals(noAttack[i]))
                return true;
        }
        return false;
    }


    /**
    * Given a object array d, it will be added in to the vector v so that the 
    * highest value is a 0 and the lowest at n-1.
    */
    private void addDoubleHigh(Vector v, objectTarget d) {
        int index = 0;
        for (int i=0; i<v.size(); i++) {
            if ( ((objectTarget)v.elementAt(i)).attack_value < d.attack_value ) 
                break;
            index = i;
        }
        
        if (index != v.size()-1) { 
            v.insertElementAt(d, index);
        } else { v.addElement(d); }
    }

    public void informUmbrella(double umb) { this.umb= umb; }

    public void setType(String type) { this.type = type; }
    public void setOwns(RCSInfluenceMapPosition mp) { this.owns.addElement(mp); }
    public void setOwner(RCSInfluenceMapPosition owner) { this.owner = owner; }

    public double[] getCenter() { return center; }
    public double getTotalValue() { return totalValue; }
    public double getTacticalStanding() { return tactical_standing; };
    public String getType() { return type; }
    public Vector getTargets() { return targets; }
    public Vector getOwns() { return owns; }
}