package infStrike.objects;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

public abstract class nationFile2 {
    private String nation;
    private String[] weapPri;
    private String[] weapSec;
    private String[] weaponUser;
    private String[] ammoPri;
    private String[] magPri;
    private int[] magPriCap;

    private String[] firstNames;
    private String[] lastNames;

    private Vector weapVec;
    private Vector weapClassVec;

    private weapFile tmpWeapFile;

    private static final String[] cammo = {"Fatigues", "DPM", "Guile Suit"}; //DPM: Disruptive Pattern Material
    private static final double[] cammoValues = {0.3, 0.55, 0.9};  //0.0 (highly visible) - 1.0 (invisible)
    private static final String[] armour = {"None", "Light", "Flak"};
    private static final double[] armourValues = {0.0, 0.2, 0.8};  //is this like quake armour?
    private static final String[] ranks = {"Private", "Corporal", "Sergeant", "Lieutenant", "Captain", "Major", "Lieutenant Colnel", "Colonel"};
    private static final double[] rankValues = {0.125, 0.25, 0.375, 0.5, 0.625, 0.75, 0.875, 1.0};

    public nationFile2(String arg1, String[] arg2, String[] arg3) {
        this.nation = arg1;
        this.firstNames = arg2;
        this.lastNames = arg3;
    }
    public void loadWeaponInfo(Vector arg1) {
        System.out.println("nationFile2/loadWeaponInfo - Initialising");
        this.weapVec = arg1;
        sortWeapVec(weapVec);
        System.out.println("nationFile2/loadWeaponInfo - Finished");
    }
    public void loadWeaponClasses(Vector arg1) {
        System.out.println("nationFile2/loadWeaponClasses - Initialising");
        this.weapClassVec = arg1;
        System.out.println("nationFile2/loadWeaponClasses - Finished");
    }


    /**
    * Given the weapVec the weapons are ordered, then the arrays regarding ammo and magazines are populated with data.
    * Method to split the vector into primary and secondary , and split the primary into weapon user groups.
    * The weapVec will contain string arrays with the following elements:
    * weapon name, primary/secondary, weapon type/user, weapon mag, mag capacity, calibre
    */
    private void sortWeapVec(Vector arg1) {
        Vector tmpPri = new Vector();
        Vector tmpSec = new Vector();
        Vector tmpVec;
        String[] tmpStrAr;
        String tmpStr;
        Hashtable tmpHash = new Hashtable();
        
        // split the vector into primary and secondary , and split the primary into weapon user groups
        if(arg1.size() > 0) {
            for(int i=0; i < arg1.size(); i++) {
                tmpStrAr = (String[])arg1.elementAt(i);
                if(tmpStrAr[1].equals("Primary")) {
                    if(tmpHash.containsKey(tmpStrAr[2].trim())) {             //weapon user
                        tmpVec = (Vector)tmpHash.get(tmpStrAr[2].trim());
                        tmpVec.addElement(tmpStrAr);
                    }
                    else {
                        tmpVec = new Vector();
                        tmpVec.addElement(tmpStrAr);
                        tmpHash.put(tmpStrAr[2].trim(), tmpVec);
                    } 
                }
                if(tmpStrAr[1].equals("Secondary"))
                    tmpSec.addElement(tmpStrAr[0]);
            }
            //weapPri = new String[tmpHash.size()];
            
        }

        // the primary weapons are now grouped into weapon user groups
        Enumeration e1 = tmpHash.keys();
        while (e1.hasMoreElements()) {
            tmpStr = (String)e1.nextElement();
            tmpVec = (Vector)tmpHash.get(tmpStr);
            for (int i=0; i < tmpVec.size(); i++) {
                //tmpStrAr = (String[])tmpVec.elementAt(i);
                //tmpPri.addElement(tmpStrAr[0]);
                Object o = tmpVec.elementAt(i);
                tmpPri.addElement((String[])tmpVec.elementAt(i));
            }
        }
        //weapPri = new String[tmpPri.size()];
        //tmpPri.copyInto(weapPri);
        //weapPri = (String[])tmpPri.toArray();
        tmpSec.add(0, "Empty");
        weapSec = new String[tmpSec.size()];
        tmpSec.copyInto(weapSec);

        weapPri = new String[tmpPri.size()+1];
        weaponUser = new String[tmpPri.size()+1];
        magPri = new String[tmpPri.size()+1];
        magPriCap = new int[tmpPri.size()+1]; 
        ammoPri = new String[tmpPri.size()+1];

        weapPri[0] = "Unarmed";
        weaponUser[0] = "Unarmed";
        magPri[0] = "Empty";
        magPriCap[0] = 0;
        ammoPri[0] = "Empty";

        for(int i=0; i<tmpPri.size(); i++) {
            tmpStrAr = (String[])tmpPri.elementAt(i);
            weapPri[i+1] = tmpStrAr[0];
            weaponUser[i+1] = tmpStrAr[2];
            magPri[i+1] = tmpStrAr[3];
            magPriCap[i+1] = Integer.parseInt(tmpStrAr[4]);
            ammoPri[i+1] = tmpStrAr[5];
        }
    }
//*********************

    /** 
    * This method will return a 'weapMagObj' (read "weapon & magazine") specific to the arguments passed.
    */
    public weapMagObj weapLoadout(String arg1, String arg2, String arg3, String arg4, String arg5, String arg6,
                                                            String arg7, String arg8, String arg9, String arg10) {
        return new weapMagObj(weapLoader(arg1), weapLoader(arg2), new magObj[] {magLoader(arg3), magLoader(arg4),
                                                                                magLoader(arg5), magLoader(arg6),
                                                                                magLoader(arg7), magLoader(arg8),
                                                                                magLoader(arg9), magLoader(arg10)});
    } 

    /**
    * Given the name of a magazine this method will return a 'magObj' containg a set number of bullets of 
    * that type.
    */
    public magObj magLoader(String arg1) {
        for (int i=0; i<magPri.length; i++) {
            if(magPri[i].equals(arg1)) {
                return new magObj(magPri[i], ammoPri[i], magPriCap[i]);
            }
        }
        return new magObj(magPri[0], ammoPri[0], 0);
    }

    /**
    * WeapLoader - pass it a string , and if it is recognised it will return a weapon object
    */
    public weapFile weapLoader(String arg1) {
        for (int i=0; i<weapClassVec.size(); i++) {
            tmpWeapFile = (weapFile)weapClassVec.elementAt(i);
            if (tmpWeapFile.getName().equals(arg1)) {
                return tmpWeapFile.newInstance();
            }
        }
        return new emptyWeap();
    }

    /**
    * Given a weaponuser string this returns an int array representing:
    * cammo, armour, weapPri, weapSec,
    */
    public int[] getUnitSetup(String arg1) {
        System.out.println("nationFile2/getUnitSetup - starting");
        return new int[]{0,0,0,0};   //******************** FOR NOW
    }

    /**
    * Given a weaponUser will return a int array representing a suitable
    * magazine setup
    */
    public int[] getMagSetup(String arg1) {
        return new int[]{0,0,0,0,0,0,0,0};
    }

//*********************

    public String[] getAllWeaponNames() {
        String[] tmpStr = new String[(weapPri.length+weapSec.length)];
        for(int i = 0; i < weapPri.length; i++) {
            tmpStr[i] = weapPri[i];
        }
        for(int i = 0; i < weapSec.length; i++) {
            tmpStr[(i+weapPri.length)] = weapSec[i];
        }
        return tmpStr;
    }

    /**
    * Makes a unique name. 
    */
    public String makeName(Vector arg1) {
        String tmp = null;
        do {
            tmp = (firstNames[(int)(Math.random()*firstNames.length)]+" "+
                   lastNames[(int)(Math.random()*lastNames.length)]);
        }
        while(nameExists(tmp, arg1) == true);
        return tmp;
    }
  
    /**
    * checks that the name (arg1) is not present in the vector arg2.
    */
    private boolean nameExists(String arg1, Vector arg2) {
        basicUnitInfo tmp;
        String tmpStr;
        for(int i = 0; i < arg2.size(); i++) {
            tmp = (basicUnitInfo)arg2.elementAt(i);
            if(tmp.getName().equals(arg1))
                return true;
        } 
        tmp = null;
        return false;
    }

//***************************

    /**
    * Takes an bascUnitInfo(arg1) and checks that all its equipment matches that which is allowed
    * for the nation it belongs to.
    */
    public boolean checkItems(basicUnitInfo arg1) {
        System.out.println("nationFile2/checkItems - starting");
        basicUnitInfo tmpInf = arg1;
        System.out.println("nationFile2/checkItems - "+tmpInf.getName());
        System.out.println(tmpInf.getArmour());
        System.out.println(tmpInf.getCammo());
        System.out.println(tmpInf.getWeaponPri());
        System.out.println(tmpInf.getWeaponSec());
        System.out.println(tmpInf.getMagType());
        
        if (isMember(armourValues, tmpInf.getArmour()) & isMember(cammoValues, tmpInf.getCammo()) &  weapExists(tmpInf.getWeaponPri()) & weapExists(tmpInf.getWeaponSec()) & magExists(tmpInf.getMagType())) {
               return true;
        }
        System.out.println("There was a Unit spec error encountered when checking "+tmpInf.getName());
        return false;
    }

    /**
    * Used during file loading to check if weapon being loaded exists.
    * Also used in the class gui/weaponInfo for geting pictures
    */
    public boolean weapExists(String arg1) {
        if(isMember(weapPri, arg1) | isMember(weapSec, arg1)) {
            return true;
        }
        return false;
    }

    /**
    * Used during file loading to check if magazines being loaded do exist.
    */
    private boolean magExists(String[] arg1) {
        for(int i = 0; i < arg1.length; i++) {
            if(!isMember(magPri, arg1[i])) {
                System.out.println("The magazine "+arg1[i]+" is not recognised by the nation "+nation); 
                return false;
            }
        }  
        return true;
    }

    /**
    * Checks if arg2 is a member of the string array(arg1).
    * Thank god for Prolog
    */
    private boolean isMember(String[] arg1, String arg2) {
        for (int i=0; i < arg1.length; i++) {
            if(arg1[i].equals(arg2)) 
                return true;
        }
        return false;
    }
    private boolean isMember(double[] arg1, double arg2) {
        for (int i=0; i < arg1.length; i++) {
            if(arg1[i]==arg2) 
                return true;
        }
        return false;
    }

//*************** basic return methods;

    public boolean weaponsAvailable() {  
        System.out.println("nationFile2/weaponsAvailable - Checking");
        if (weapVec != null)
            return true;
        return false;
    }
    
    /**
    * Adds a number to the end of each element to diferentiate between them, so
    * that errors don't occur in dialogForceBuild, when selecting a weapon
    * user type.
    */ 
    public String[] getWeaponUser() { 
        String[] tSAr = (String[])weaponUser.clone();
        tSAr[0] = tSAr[0]+" 1";
        for (int i=1; i<tSAr.length; i++) {
            System.out.println("tSAr element : "+tSAr[i]+" :"+i);
            if (tSAr[i-1].startsWith(tSAr[i])) {  //is preceeding entry the same weapon name?
                int xy = Integer.parseInt(tSAr[i-1].split(" ")[tSAr[i-1].split(" ").length-1])+1;  // to get the number at the end
                tSAr[i] = tSAr[i]+" "+xy;
            }
            else {
                tSAr[i] += " 1";
            }
        }
        return tSAr;
    }

    public String[] getMagPri() { return magPri; }
    public String[] getWeapPri() { return weapPri; }
    public String[] getWeapSec() { return weapSec; }

    public String[] getRanks() { return ranks; }
    public double[] getRankValues() { return rankValues; }
    public String[] getCammo() { return cammo; }
    public double[] getCammoValues() { return cammoValues; }
    public String[] getArmour() { return armour; }
    public double[] getArmourValues() { return armourValues; }

    public String getNation() { return nation; }
}