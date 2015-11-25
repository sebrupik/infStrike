package infStrike.objects;

import java.awt.geom.*;

/**
* How do you stop the agent firing all the time?
*/

public abstract class weapFile {
    private String name;
    private String weaponUser;
    private String ammoType;
    private double weight;
    private int rof;
    private int effRange;  
    private int maxRange;

    private String[] fire_modes;

    int shit;
    //              name         weaponUser   ammo          weight       rof
    public weapFile(String name, String weaponUser, String ammoType, double weight, int rof, boolean auto, boolean burst, boolean single) {
        this.name = name;
        this.weaponUser = weaponUser;
        this.ammoType = ammoType;
        this.weight = weight;
        this.rof = rof;

        if (auto & burst & single) 
            this.fire_modes = new String[]{"AUTO", "BURST", "SINGLE"};
        if (!auto & burst & single) 
            this.fire_modes = new String[]{"BURST", "BURST", "SINGLE"};
        if (!auto & !burst & single) 
            this.fire_modes = new String[]{"SINGLE", "SINGLE", "SINGLE"};
        if (auto & !burst & single) 
            this.fire_modes = new String[]{"SINGLE", "AUTO", "SINGLE"};
    }

    /**
    * Used during soldier initialisation to ensure that
    * strictly identical weapons aren't being issued
    */
    public weapFile(weapFile arg1) {
        this(arg1.getName(), arg1.getWeaponUser(), arg1.getAmmoType(), arg1.getWeight(), arg1.getRof());
    }
    
    /**
    * The fire mode should be determined by the distance to the target - 
    * Single>far, burst>medium, close>full auto
    * A bullet object should then be sent to the world with a particular trajectory, 
    * length (number of bullets), and force (calibre).
    * 
    * Prehaps just the target agent be passed and a hit probabilty be calcuated.
    * This would save having possibly hundereds of bullets objects flying about and
    * having to calculate what they are hitting every cycle!
    */
    public void fire(Agent firer, Agent target) {
        System.out.println("firing....");
        shit++;
    }

    public abstract weapFile newInstance();

    public String getName() { return name; }
    public String getWeaponUser() { return weaponUser; }
    public String getAmmoType() { return ammoType; }
    public double getWeight() { return weight; }
    public int getRof() { return rof; }
    public int getShit() { return shit; }
}

class emptyWeap extends weapFile {
    public emptyWeap() {
        super("Unarmed", "Unarmed", "None", 1.0, 1);
    }

    public weapFile newInstance() {
        return new emptyWeap();
    }
}