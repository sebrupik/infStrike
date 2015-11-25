package infStrike.objects;

import java.awt.geom.Point2D;
import java.awt.geom.*;
import java.awt.Color;
import java.awt.Graphics2D;

/**
* With movement being handled by the Agent class, this class is purely designed
* for sensing the environment and shooting at things. 
*/
public class Soldier extends Agent {
    private int side;
    private String nation;
    private double rank;   //0.0 (lowest) - 1.0 (highest)
    private double cammo;  //0.0 (highly visible) - 1.0 (invisible)
    private double armour;  //0.0 (none) - 1.0 (loads)
    private weapMagObj weapLoadout;   
    private double weaponSkill;  // 0.0 terrible - 1.0 sharp shooter
   
    private Rectangle2D.Double solRect;
    private int visionDist = 1000;
    private int visionAngle = 90;
    private Arc2D.Double view;
    Color fadedColour;

    private double runSpd = 1;
    private double walkSpd = 0.5;

    private Agent[] targets = null;

    private boolean walk;
    private boolean run;

    public Soldier(Point2D.Double Cor, int side, String nation, String name, double rank, double armour, double cammo, weapMagObj weapLoadout, worldObject world, AIController AIC) {
        super(Cor, side, world, AIC, 10.0, name);
        this.nation = nation;
        this.rank = rank;
        this.armour = armour;
        this.cammo = cammo;
        this.weapLoadout = weapLoadout;
        this.weaponSkill = 0.5;  

        solRect = new Rectangle2D.Double((getCor().x-0.5), (getCor().y-0.5), 1.0, 1.0);  //soldiers bounding box is a square meter
        run = true;
        fadedColour = new Color(world.colours[side].getRed(), world.colours[side].getGreen(), world.colours[side].getBlue(), 40);
    }
  
    /**
    * Constructor used by objects/Base
    */
    public Soldier(Point2D.Double Cor, basicUnitInfo bui, worldObject world, AIController AIC) {
        this(Cor, bui.getSide(), bui.getNation(), bui.getName(), bui.getRank(), bui.getArmour(), bui.getCammo(), bui.getWeapLoadout(), world, AIC);
    }

    public void updateGraphics(double time, Graphics2D g2, boolean agentDraw) {
        super.updateGraphics(time, g2, agentDraw);
        //draw the Soldier
        g2.draw(new Rectangle2D.Double((this.getCor().x-0.5), (this.getCor().y-0.5), 1.0, 1.0));
        //draw the line of movement
        g2.drawLine((int)this.getCor().x, (int)this.getCor().y, (int)this.getDes().x, (int)getDes().y);

        if (agentDraw) {
            g2.setColor(fadedColour);
            //draw the view
            view = new Arc2D.Double(this.getCor().x-(visionDist/2), this.getCor().y-(visionDist/2), visionDist, visionDist, (viewDegree(this.getCor(), this.getDes())-(visionAngle/2)), visionAngle, Arc2D.PIE);
            g2.fill(view);
            //draw the weapon cone

            //draw the current target
            if (targets != null) {
                if (targets.length > 0) {
                    g2.setColor(Color.white);
                    g2.drawLine((int)this.getCor().x, (int)this.getCor().y, (int)targets[0].getCor().x, (int)targets[0].getCor().y);
                }
            }
        }
    } 
    /*public void action(double time) {
    }*/
  
    public void move() {
        if (walk == true)
            moveSpd = walkSpd;
        if (run == true)
            moveSpd = runSpd;

        super.move();
    }

    /**
    * Pass its view cone to the world to find out what it can see. 
    * The world then passes it back an array of tantilising targets.
    */
    public void look() {
        if (view != null)
            targets = world.getViewableAgents(this, this.view);
    }
    
    /**
    * This method chooses which target (if any) to attack, and then uses  
    * its weapon to do some damage to it.
    */
    public void attack() {
    }

    /**
    * By combing cammoflage, surrounding terrain, movment, stance(?!) and weapon firing
    * a visibility index is calaculted. 0.0 (visible) - 1.0 (invisible)
    */
    public double calcVisibility() { return cammo; }
}