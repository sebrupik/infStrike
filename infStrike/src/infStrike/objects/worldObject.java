package infStrike.objects;

import java.awt.Graphics2D;
import java.awt.Color;
import java.util.ArrayList;
import java.awt.geom.Arc2D;


/**
   Version 2 of worldObject. Changed 4/12/02 to allow different AI systems to be used by introducing
   the AIController super class.
*/

public class worldObject {
    
    AIController[] controllers;  //contains all the diffent AIControllers for all sides
    ArrayList<Agent>[] sides;     //each element contains a vector of agents
    ArrayList<AIPlatoon>[] platoons;  //each element contains a vector platoons for that side
    ArrayList<Base>[] bases;     //each element contains a vector of bases
    ArrayList<Agent> agents;
    Color[] colours;

    private int numTeams;
    private arenaPlan arena; 
    private boolean platoonDraw, AIDraw, baseDraw, agentDraw;

    public worldObject(int numTeams, arenaPlan arena) {
        this.numTeams = numTeams;
        this.arena = arena;
 
        controllers = new AIController[numTeams];
        sides = new ArrayList[numTeams];
        platoons = new ArrayList[numTeams];
        bases = new ArrayList[numTeams];
        agents = new ArrayList<>();

        for (int i=0; i < numTeams; i++) {
            sides[i] = new ArrayList<>();
            platoons[i] = new ArrayList<>();
            bases[i] = new ArrayList<>();
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
    public void add(basicUnitInfo bui) { ((Base)bases[bui.getSide()].get(0)).addSoldier(bui); }
    public void add(Base base) { bases[base.getSide()].add(base); }
    public void add(AIController AICont) { this.controllers[AICont.getSide()] = AICont; }
    public void add(Agent a) { agents.add(a); sides[a.getSide()].add(a); }

    public void updateGraphics(double time, Graphics2D g2, boolean platoonDraw, int AIDraw, boolean baseDraw, boolean agentDraw) {
        arena.updateGraphics(time, g2);
        for (int i=0; i < numTeams; i++) {
            g2.setPaint(colours[i]);
            for (int j=0; j < platoons[i].size(); j++) {
                platoons[i].get(j).updateGraphics(time, g2, platoonDraw);
            }
            for (int j=0; j< bases[i].size(); j++) {
                bases[i].get(j).updateGraphics(time, g2, baseDraw);
            }
            for (int j=0; j < sides[i].size(); j++) {
                sides[i].get(j).updateGraphics(time, g2, agentDraw);
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
        ArrayList v;
        //System.out.println("************** platoons");
        for (int i=0; i< platoons.length; i++) {
            v = (ArrayList)platoons[i];
            for (int j=0; j < v.size(); j++) {
                ((AIPlatoon)v.get(j)).action(time);
            }
        }
        v = null;
        //System.out.println("************** agents");
        for (int i=0; i<agents.size(); i++) {
            ((Agent)agents.get(i)).action(time);
        }
        //System.out.println("************** bases");
        for (int i=0; i<bases.length; i++) {
            v = (ArrayList)bases[i];
            for (int j=0; j<v.size(); j++) { 
                ((Base)v.get(j)).action(time);
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
        ArrayList<Base> v = bases[arg1.getSide()];
        double dist = -1;
        int index = -1;
 
        if(v.size() > 0) {
            dist = arg1.getCor().distance( v.get(0).getCor().x, v.get(0).getCor().y);
            index = 0;
        }
 
        for(int i=0; i<v.size(); i++) {
            if( (arg1.getCor().distance( v.get(i).getCor().x, v.get(i).getCor().y) < dist) & dist < -1) {
                dist = arg1.getCor().distance( v.get(i).getCor().x, v.get(i).getCor().y);
                index = i;
            }
        }
        return v.get(index);
    }

    /**
    * Given an Agent, Agents which are visible to it are calculated.
    * Notice that there is no 'same side' condition. this will make friendly fire possible.
    * friendly fire to be disabled to make game run faster for inspection
    */
    public Agent[] getViewableAgents(Agent arg1, Arc2D arc) {
        ArrayList<Agent> allAgents = new ArrayList<>();
        Agent currentAgent;
        for(int i=0; i<agents.size(); i++) {
            currentAgent = agents.get(i);
            if ( arg1.getSide() != currentAgent.getSide() && !arg1.getName().equals(currentAgent.getName())) {  //make sure he's not shooting himself
                //System.out.println("having a look at : "+currentAgent.getName());
                if (arc.contains(currentAgent.getCor().x, currentAgent.getCor().y)) { //within view-cone
                    //if (currentAgentVisible.............
                    //if (currentAgentGotLineOfSight.......
                    allAgents.add(currentAgent);
                }
            }
        }
        //Agent[] agents = new Agent[allAgents.size()];
        //allAgents.copyInto(agents);
        //return agents;
        
        return allAgents.toArray(new Agent[allAgents.size()]);
    }


    public void printInfo() {
        System.out.println("_.,;:'~':;,._");
        arena.printInfo();
        System.out.println("_.,;:'~':;,._");
    }
    public int getWidth() { return arena.getWidth(); }
    public int getHeight() { return arena.getHeight(); }
    public arenaPlan getArenaPlan() { return arena; }
    public Agent getAgent(int arg1, int arg2) { return sides[arg1].get(arg2); }
    public int getNumTeams() { return numTeams; }
}