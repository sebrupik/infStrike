package infStrike.objects;

import java.awt.geom.*;
import java.awt.Color;
import java.awt.Graphics2D;

/**
* The Agent class provides methods for moving in any direction
* on a 2D plane.
*
*/
public abstract class Agent extends Entity {
    private int side;                // what side the agent is on
    worldObject world = null;
    AIController AIC = null;
    private double health = 10.0;  // just for the sake of argument
    private String name;
    private boolean inPlatoon;
    
    double moveSpd = 0.0;
    private boolean waiting;

    private int ideg;  
    private double rad;
    private double a,o,h;
    private double deg = 0;
    private int Rot = 0;   //angle of rotation
    private Point2D.Double Des;    

    private Point2D.Double oldDes;    

    public Agent(Point2D.Double Cor, int side, worldObject world, AIController AIC, double health, String name) {
        super(Cor);
        this.side = side;
        this.world = world;
        this.AIC = AIC;
        this.health = health;
        this.name = name;
       
        this.Des = this.getCor();
    }

    /**
    * This move method uses a new recipe allowing movement in 360 degrees.
    */
    public void move() {
        if(waiting==false) {
            Rot = adjustDeg(viewDegree(this.getCor(), Des));
            rad = Math.toRadians(Rot);

            h = this.getCor().distance(Des);
            //System.out.println(name+" ------ "+h+" ---------- "+Rot);
            //System.out.println("Agent Cor : "+this.getCor()+" / Des : "+Des);

            if(h <= moveSpd) { // this should stop the unit moving over the destination repeatedly
                //System.out.println(name+" doing the magic move");
                this.setCor(Des);
            }
            else {
                if (Rot==0 | Rot ==360)
                    this.getCor().y -= moveSpd;
                else if (Rot==90)
                    this.getCor().x += moveSpd;
                else if (Rot==180)
                    this.getCor().y += moveSpd;
                else if (Rot==270)
                    this.getCor().x -= moveSpd;
                else {
                    this.getCor().x += Math.sin(rad)*moveSpd;  //cos
                    this.getCor().y -= Math.cos(rad)*moveSpd;  //sin
                }
            }
        }
        //if (this.getCor().x==Des.x & this.getCor().y==Des.y) {
        if (this.getCor().equals(Des)) {
            //System.out.println(name+" : I have reached my destination "+this.getCor().toString()+" / "+Des.toString());
            waiting = true;
        }
        else { 
            //System.out.println(name+" : I haven't reached my destination "+this.getCor().toString()+" / "+Des.toString());
            waiting = false;
        }
    }
    /**
    * This field of vision method came to me in a dream, and was written in a trace like state. 
    * As a result I'm not sure what's going on. Highly experimental!!!! Works sweet though.
    */    
    public int viewDegree(Point2D.Double arg1, Point2D.Double arg2) {   //Cor point then Des point
        if (arg1.x > arg2.x) {      //des left
            if(arg1.y < arg2.y) {   //des below
                o = arg2.y - arg1.y;
                a = arg1.x - arg2.x;
                //h = Math.sqrt(Math.pow(a, 2)+Math.pow(o, 2));
                h = arg1.distance(arg2);
    
                deg = Math.toDegrees(Math.acos((a/h)));
                deg = deg+180;
            }
            if(arg1.y > arg2.y) {    //des below
                a = arg1.y - arg2.y;
                o = arg1.x - arg2.x;
                //h = Math.sqrt(Math.pow(a, 2)+Math.pow(o, 2));
                h = arg1.distance(arg2);                

                deg = Math.toDegrees(Math.acos((a/h)));
                deg = deg+90;
            }
        }
        if (arg1.x < arg2.x) {       //des right
            if(arg1.y < arg2.y) {    //des below
                a = arg2.y - arg1.y;
                o = arg2.x - arg1.x;
                //h = Math.sqrt(Math.pow(a, 2)+Math.pow(o, 2));
                h = arg1.distance(arg2);                

                deg = Math.toDegrees(Math.acos((a/h)));
                deg = deg+270;
            }
            if(arg1.y > arg2.y) {    //des above
                o = arg1.y - arg2.y;
                a = arg2.x - arg1.x;
                //h = Math.sqrt(Math.pow(a, 2)+Math.pow(o, 2));
                h = arg1.distance(arg2); 

                deg = Math.toDegrees(Math.acos((a/h)));
            }
        }

        if(arg1.y == arg2.y) {
            if(arg1.x > arg2.x)
                deg = 180;
            if(arg1.x < arg2.x)
                deg = 0;
        }
        if(arg1.x == arg2.x) {
            if(arg1.y > arg2.y)
                deg = 90;
            if(arg1.y < arg2.y)
                deg = 270;
        }


        return (int)deg;
    }
    /**
    * Adjust my degree system (12 o'clock clockwise) to trig degree system (3 o'clock anti-clockwise)
    */
    private int adjustDeg(int arg1) {
        if(arg1 <= 90)
            return (90-arg1);

        if(arg1 > 90 & arg1 <= 180)
            return (360 - (arg1-90));

        if(arg1 > 180 & arg1 <= 270)
            return (270 - (arg1-180));

        if(arg1 > 270 & arg1 <= 360)
            return (180 - (arg1-270));

        return 0;
    }

    public void updateGraphics(double time, Graphics2D g2, boolean agentDraw) { }
    public void action(double time) {
        move();
        look();
        attack();
    }

    public abstract void look();
    public abstract void attack();
    public abstract double calcVisibility();

    public void setDes(Point2D.Double Des) { this.Des = Des; /*System.out.println(name+" has had a new destination set "+Des.x+", "+Des.y);*/ }
    public Point2D.Double getDes() { return Des; }
    public int getRot() { return Rot; }
    public void setInPlatoon(boolean inPlatoon) { this.inPlatoon = inPlatoon; }
    public int getSide() { return side; }
    public String getName() { return name; }
    public boolean getInPlatoon() { return inPlatoon; }
    public double getHealth() { return health; }
    public boolean getWaiting() { return waiting; }
    public double getMoveSpeed() { return moveSpd; }
}