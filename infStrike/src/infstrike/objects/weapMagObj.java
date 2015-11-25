package infStrike.objects;

public class weapMagObj {
    private weapFile weapPri;
    private weapFile weapSec;    
    private magObj[] mags;
    private int magIndex;
    private boolean canFire;

    public weapMagObj(weapFile weapPri, weapFile weapSec, magObj[] mags) {
        this.weapPri = weapPri;
        this.weapSec = weapSec;
        this.mags = mags;
        //status();
    }

    /**
    * Argument 1 distance - distance from target
    * Argument 2 weaponSkill - shooters accuracy
    * Argument 3 Agent (target) - the agent being shoot at
    */
    public void fireWeap(double distance, double weaponSkill, Agent target) { 

        if (mags[magIndex].getAmmoType().equals(weapPri.getAmmoType())) {
            //return weapFire.fire(arg1, mags[magIndex]);
        }
        if (mags[magIndex].getRoundsLeft() == 0)
            reload();
    }

    /**
    * Cool method to be used when a unit is waiting about, resting, ambushing.....etc
    */
    public void combineMags() {
        canFire = false;
        
    }

    public void reload() {
        canFire = false;
        while (magIndex == mags.length | mags[magIndex].getRoundsLeft() == 0) {
            magIndex++; 
        }
        //Thread.sleep(4000);

        canFire = true;
    }

//*********************
// Methods for body looting
    /**
    * The current weapon is being swapped
    */ 
    public weapFile swapWeapon(weapFile arg1) { 
        weapFile tmpWeap = weapPri;
        weapPri = arg1;
        return tmpWeap;
    }
    public magObj swapMag(int arg1, magObj arg2) {
        magObj tmpMag = mags[arg1];
        mags[arg1] = arg2;
        return tmpMag;
    }
 
    /**
    * Pass it a string stating the bullet type you want, and it will return
    * the magIndex of the magazine with the largest amount if any.
    */
    public int findMagOfType(String arg1) {
        int biggest = 0;
        int tmpIndex = -1;
        for(int i = 0; i < mags.length; i++) {
            if(mags[i].getAmmoType().equals(arg1) & (mags[i].getRoundsLeft() > biggest)) 
                tmpIndex = i;
        }
        return tmpIndex;
    }

//*********************
    public String[] getMagType() {
        return new String[]{mags[0].getMagType(), mags[1].getMagType(), mags[2].getMagType(), mags[3].getMagType(),
                            mags[4].getMagType(), mags[5].getMagType(), mags[6].getMagType(), mags[7].getMagType()};
    }

    public String getWeapPriName() {
        return weapPri.getName();
    }
    public String getWeapSecName() {
        return weapSec.getName();
    }
    public String getWeaponUser() {
        return weapPri.getWeaponUser();
    }

    public void status() {
        System.out.println(weapPri.toString()+", "+mags[0].status()+", "+
                                                    mags[1].status()+", "+
                                                    mags[2].status()+", "+
                                                    mags[3].status()+", "+
                                                    mags[4].status()+", "+
                                                    mags[5].status()+", "+
                                                    mags[6].status()+", "+
                                                    mags[7].status());
    }
}