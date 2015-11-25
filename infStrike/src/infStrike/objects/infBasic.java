package infStrike.objects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.*;

public class infBasic {
    Graphics2D g2D;

    int scale = 1;
    double runSpd = 1;
    double walkSpd = 0.5;
    double moveSpd;
    int fldVision = 100;

    Point2D.Double Cor;
    Point2D.Double Tar;
    Point2D.Double Des;

    private int side;         //1 = red, 2 = blue
    private String sideNat;
    private String name;
    private String rank;
    private String armour;
    private String cammo;
    private weapMagObj weapLoadout;
    private String ammo1;
    private String ammo2;
    private String ammo3;
    private String ammo4;
    //private String weaponUser; //rifle, gm, heavy mg, sniper, grenadier
    
    
    // platoon and aiCommand variables
    private boolean canMove;               //if false, wait for permission either from platoon leader or command to move
    private boolean waiting;
    private boolean inPlatoon;
    private boolean onPatrol;
    
    private int kills;
    private int rndsFired;
    //boolean userSelect;
    //String[] statsStr = new String[8]; //name, rank, cammo, weapon, damage, stress, kills, rndsfired
    private int skill;
    private int health;
    private int stress;          // 0 none, 5 pumped, 10 hyper(irrational)

    // movement and vision variables
    private boolean walk;
    private boolean run;
    private boolean canMoveHori = true;
    private boolean canMoveVert = true;
    private boolean selectedUnit = false;
    private Color grayView = new Color(207, 207, 207, 40);
    private Arc2D.Double view;
    private int ideg;
    private double rad;
    private double a,o,h;
    private double deg = 0;


    public infBasic(int arg1, String arg2, String arg3, String arg4, String arg5, String arg6, weapMagObj arg7) {
        Cor = new Point2D.Double(50, 100);      
        Des = new Point2D.Double(320, 230);

        side  = arg1; 
        sideNat = arg2;
        name = arg3;
        rank = arg4;
        armour = arg5;
        cammo = arg6;
        weapLoadout = arg7;

        health = 100;
        stress = 0;

        run = false;
        walk = true;
    }

//*************************************************************************
    /**
    * Paint method, fairly basic, and very little improvements to be made (I can't see any)
    */
    public void draw(Graphics g) {
        g2D = (Graphics2D)g;

        g.drawRect((int)Cor.x-2, (int)Cor.y-2, 4, 4);
        g.drawLine((int)Cor.x, (int)Cor.y, (int)Des.x, (int)Des.y);

        g.setColor(grayView);
        view = new Arc2D.Double(Cor.x-(fldVision/2), Cor.y-(fldVision/2), fldVision, fldVision, (viewDegree(Cor, Des)-45), 90, Arc2D.PIE);
        g2D.fill(view);

        if(Tar != null) {
            g2D.setColor(Color.black);
            g2D.drawLine((int)Cor.x, (int)Cor.y, (int)Tar.x, (int)Tar.y);
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
                h = Math.sqrt(Math.pow(a, 2)+Math.pow(o, 2));
    
                deg = Math.toDegrees(Math.acos((a/h)));
                deg = deg+180;
            }
            if(arg1.y > arg2.y) {    //des below
                a = arg1.y - arg2.y;
                o = arg1.x - arg2.x;
                h = Math.sqrt(Math.pow(a, 2)+Math.pow(o, 2));
                
                deg = Math.toDegrees(Math.acos((a/h)));
                deg = deg+90;
            }
        }
        if (arg1.x < arg2.x) {       //des right
            if(arg1.y < arg2.y) {    //des below
                a = arg2.y - arg1.y;
                o = arg2.x - arg1.x;
                h = Math.sqrt(Math.pow(a, 2)+Math.pow(o, 2));
                
                deg = Math.toDegrees(Math.acos((a/h)));
                deg = deg+270;
            }
            if(arg1.y > arg2.y) {    //des below
                o = arg1.y - arg2.y;
                a = arg2.x - arg1.x;
                h = Math.sqrt(Math.pow(a, 2)+Math.pow(o, 2));
 
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
    * This move method uses a new recipe allowing movement in 360 degrees.
    */
    public void move() {
        Point2D.Double tmp;

        if (walk == true)
            moveSpd = walkSpd;
        if (run == true)
            moveSpd = runSpd;
        
        if(waiting==false) {
            //ideg = adjustDeg(viewDegree(Cor, Des, 2));
            ideg = adjustDeg(viewDegree(Cor, Des));
            rad = Math.toRadians(ideg);

            if(h < moveSpd) { // this should stop the unit moving over destination repeatedly
                Cor = Des;
            }
            else {
                if (ideg > 0 & ideg < 90) { 
                    Cor.x += Math.sin(rad)*moveSpd;  //cos
                    Cor.y -= Math.cos(rad)*moveSpd;  //sin
                }
                if (ideg > 90 & ideg < 180) {
                    Cor.x += Math.sin(rad)*moveSpd;
                    Cor.y -= Math.cos(rad)*moveSpd;
                }
                if (ideg > 180 & ideg < 270) {
                    Cor.x += Math.sin(rad)*moveSpd;
                    Cor.y -= Math.cos(rad)*moveSpd;
                }
                if (ideg > 270 & ideg < 360) {
                    Cor.x += Math.sin(rad)*moveSpd;
                    Cor.y -= Math.cos(rad)*moveSpd;
                }
                if (ideg==0 | ideg ==360)
                    Cor.y -= moveSpd;
                if (ideg==90)
                    Cor.x += moveSpd;
                if (ideg==180)
                    Cor.y += moveSpd;
                if (ideg==270)
                    Cor.x -= moveSpd;
            }
        }
        if (Cor.x==Des.x & Cor.y==Des.y) {
            waiting = true;
        }
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

    public double getMoveSpd() {
        return moveSpd;
    }

//*************** set methods *********************************************
    public void setPos(Point2D.Double arg1) {
        Cor = arg1;
    }
    
    public void setDes(Point2D.Double arg1) {
        Des = arg1;
    }

    public void setInPlatoon(boolean arg1) {
        inPlatoon = arg1;
    }

    public void setOnPatrol(boolean arg1) {
        onPatrol = arg1;
    }

    public void setWaiting(boolean arg1) {
        waiting = arg1;
    }

    public void setScale(int arg1) {
        //how many pixels per metre?
        
    }

    /**
    * arg1 is the update interval in milliseconds
    */
    public void setUpdate(int arg1) {
        //assuming 1 pixel is 4 metres......
        walkSpd = 0.25/(1000/arg1);
        runSpd = 0.75/(1000/arg1);
        System.out.println("new unit update "+walkSpd+", "+runSpd);
    }
//*************** retrun methods- not very interesting ********************
    public String toString() {
        return name;
    }

    public int getSide() {
        return side;
    }
    public String getSideNat() {
        return sideNat;
    }
    public String getName() {
        return name;
    }
    public String getRank() {
        return rank;
    }
    public String getArmour() {
        return armour;
    }
    public String getCammo() {
        return cammo;
    }
    public String getWeaponPri() {
        return weapLoadout.getWeapPriName();
    }
    public String getWeaponsec() {
        return weapLoadout.getWeapSecName();
    }
    public String[] getMagType() {
        return weapLoadout.getMagType();
    }
    public String getWeaponUser() {
        return weapLoadout.getWeaponUser();
    }

    public int getKills() {
        return kills;
    }
    public int getRndsFired() {
        return rndsFired;
    }
    public boolean isInPlatoon() {
        return inPlatoon;
    }
    public int getHealth() {
        return health;
    }

    // patrol related methods
    public boolean getOnPatrol() {
        return onPatrol;
    }
    public boolean getWaiting() {
        return waiting;
    }
    public Point2D.Double getCor() {
        return Cor;
    }
    
}
