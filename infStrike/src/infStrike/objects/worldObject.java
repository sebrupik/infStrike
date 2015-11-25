package infStrike.objects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.Vector;
import java.awt.geom.Arc2D;


/**
   Version 2 of worldObject. Changed 4/12/02 to allow different AI systems to be used by introducing
   the AIController super class.
*/

public class worldObject {
    
    AIController[] controllers;  //contains all the diffent AIControllers for all sides
    Vector[] sides;     //each element contains a vector of agents
    Vector[] platoons;  //each element contains a vector platoons for that side
    Vector[] bases;     //each element contains a vector of bases
    Vector agents;
    Color[] colours;

    private int numTeams;
    private arenaPlan arena; 
    private boolean platoonDraw, AIDraw, baseDraw, agentDraw;

    public worldObject(int numTeams, arenaPlan arena) {
        this.numTeams = numTeams;
        this.arena = arena;
 
        controllers = new AIController[numTeams];
        sides = new Vector[numTeams];
        platoons = new Vector[numTeams];
        bases = new Vector[numTeams];
        agents = new Vector();

        for (int i=0; i < numTeams; i++) {
            sides[i] = new Vector();
            platoons[i] = new Vector();
            bases[i] = new Vector();
            colours = new Color[numTeams];
        }

        if (numTeams>0) colours[0] = Color.red;
        if (numTeams>1) colours[1] = Color.blue;
        if (numTeams>2) colours[2] = Color.green;
        if (numTeams>3) colours[3] = Color.cyan;
        if (numTeams>4) colours[4] = Color.orange;
        if (numTeams>5) colours[5] = Color.pink;
        //etc...
    }

    /**
    *  bui passed to relevant base which then converters it 
    *  into an agent and calls the add method in worldObject.
    */
    public void add(basicUnitInfo bui) { ((Base)bases[bui.getSide()].elementAt(0)).addSoldier(bui); }
    public void add(Base base) { bases[base.getSide()].addElement(base); }
    public void add(AIController AICont) { this.controllers[AICont.getSide()] = AICont; }
    public void add(Agent a) { agents.addElement(a); sides[a.getSide()].addElement(a); }

    public void updateGraphics(double time, Graphics2D g2, boolean platoonDraw, int AIDraw, boolean baseDraw, boolean agentDraw) {
        arena.updateGraphics(time, g2);
        for (int i=0; i < numTeams; i++) {
            g2.setPaint(colours[i]);
            for (int j=0; j < platoons[i].size(); j++) {
                ((AIPlatoon)platoons[i].elementAt(j)).updateGraphics(time, g2, platoonDraw);
            }
            for (int j=0; j< bases[i].size(); j++) {
                ((Base)bases[i].elementAt(j)).updateGraphics(time, g2, baseDraw);
            }
            for (int j=0; j < sides[i].size(); j++) {
                ((Agent)sides[i].elementAt(j)).updateGraphics(time, g2, agentDraw);
            }
            if (AIDraw != -1) {
                controllers[AIDraw].updateGraphics(time, g2, true);
            }
        }
    }

    public void update(double time) {
        System.out.println("*********************************************************");
        System.out.println(time);
        //System.out.println("*********************************************************");
        Vector v;
        //System.out.println("************** platoons");
        for (int i=0; i< platoons.length; i++) {
            v = (Vector)platoons[i];
            for (int j=0; j < v.size(); j++) {
                ((AIPlatoon)v.elementAt(j)).action(time);
            }
        }
        v = null;
        //System.out.println("************** agents");
        for (int i=0; i<agents.size(); i++) {
            ((Agent)agents.elementAt(i)).action(time);
        }
        //System.out.println("************** bases");
        for (int i=0; i<bases.length; i++) {
            v = (Vector)bases[i];
            for (int j=0; j<v.size(); j++) { 
                ((Base)v.elementAt(j)).action(time);
            }
        }
        v=null;
        //System.out.println("************** controllers");
        for (int i=0; i<controllers.length; i++) {
            controllers[i].action(time);
        }
    }



    /**
    * Returns the nearset base to an agent
    * N.B!!!!!!!!!! if there are no bases left there is going to be a BIG problem!
    */
    public Base getNearestBase(Agent arg1) {
        Vector v = bases[arg1.getSide()];
        double dist = -1;
        int index = -1;
 
        if(v.size() > 0) {
            dist = arg1.getCor().distance( ((Base)v.elementAt(0)).getCor().x, ((Base)v.elementAt(0)).getCor().y);
            index = 0;
        }
 
        for(int i=0; i<v.size(); i++) {
            if( (arg1.getCor().distance( ((Base)v.elementAt(i)).getCor().x, ((Base)v.elementAt(i)).getCor().y) < dist) & dist < -1) {
                dist = arg1.getCor().distance( ((Base)v.elementAt(i)).getCor().x, ((Base)v.elementAt(i)).getCor().y);
                index = i;
            }
        }
        return (Base)v.elementAt(index);
    }

    /**
    * Given an Agent, Agents which are visible to it are calculated.
    * Notice that there is no 'same side' condition. this will make friendly fire possible.
    * friendly fire to be disabled to make game run faster for inscpection
    */
    public Agent[] getViewableAgents(Agent arg1, Arc2D arc) {
        Vector allAgents = new Vector();
        Agent currentAgent;
        for(int i=0; i<agents.size(); i++) {
            currentAgent = (Agent)agents.elementAt(i);
            if ( arg1.getSide() != currentAgent.getSide() && !arg1.getName().equals(currentAgent.getName())) {  //make sure he's not shooting himself
                //System.out.println("having a look at : "+currentAgent.getName());
                if (arc.contains(currentAgent.getCor().x, currentAgent.getCor().y)) { //within view-cone
                    //if (currentAgentVisible.............
                    //if (currentAgentGotLineOfSight.......
                    allAgents.addElement(currentAgent);
                }
            }
        }
        Agent[] agents = new Agent[allAgents.size()];
        allAgents.copyInto(agents);
        return agents;
        //return (Agent[])allAgents.toArray();
    }


    public void printInfo() {
        System.out.println("_.,;:'~':;,._");
        arena.printInfo();
        System.out.println("_.,;:'~':;,._");
    }
    public int getWidth() { return arena.getWidth(); }
    public int getHeight() { return arena.getHeight(); }
    public arenaPlan getArenaPlan() { return arena; }
    public Agent getAgent(int arg1, int arg2) { return (Agent)sides[arg1].elementAt(arg2); }
    public int getNumTeams() { return numTeams; }
}