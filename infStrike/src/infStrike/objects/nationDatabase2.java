package infStrike.objects;

/**
* .....and you though nationDatabase 1 was good eh?
* Well this is version 2 and it blows the previous one out of the water.
* By combining realtime class loading, and JAR package access weapon data is no
* longer hardwired into the nation classes. Instead weapon JARs state which
* country they belong to, these lists are then passed as and when a nation is
* loaded.
* This means that nation classes do NOT need to be recompiled every time a new weapon
* is added.
* Sweet huh?!  (2/10/02)
*/

import infStrike.utils.jarCustomExtractor;
import infStrike.utils.WeaponClassLoader;
import infStrike.utils.NationClassLoader;

import java.util.ArrayList;
import java.net.*;
import javax.swing.ImageIcon;

public class nationDatabase2 {
   
    // Vectors containing specific classes.
    ArrayList<weapFile> allWeapons;
    ArrayList<nationFile2> allNations;        
    
    // Vectors containg JAR URLs
    ArrayList<URL> jarsWeapons;
    ArrayList<URL> jarsNations;

    ArrayList tmpVec;
    nationFile2 tmpNatFile;
    weapFile tmpWeapFile;
    String[] tmpStrAr;

    public nationDatabase2(ArrayList[] arg1) {
        System.out.println("nationDatabase2 - initialising");
        this.allWeapons = arg1[0];
        this.allNations = arg1[1];
        this.jarsWeapons = arg1[2];
        this.jarsNations = arg1[3];
    }


//************************************************************************
//** Mixed bag of methods, mostly used the side setup series of classes **
//************************************************************************
    /**
    * Returns a String array containing names of all participating nations
    */
    public String[] getAllNations() {
        String tmpStr[] = new String[allNations.size()];
        for (int i=0; i<allNations.size(); i++) {
            tmpNatFile = allNations.get(i);
            tmpStr[i] = tmpNatFile.getNation();
        }
        java.util.Arrays.sort(tmpStr);
        return tmpStr;
    }

    /**
    * Returns a nationFile corresponding to the argument
    */
    public nationFile2 getNationFile(String arg1) {
        for (int i = 0; i < allNations.size(); i++) {
            tmpNatFile = allNations.get(i);
            if(arg1.equals(tmpNatFile.getNation()))
                return tmpNatFile;
        }
        return null;
    }

    /**
    * Given a unit it will be checked that its nation is a participant.
    * If its country is found its items will be checked against those held by that
    * country.
    */
    public boolean checkItems(basicUnitInfo arg1) {
        System.out.println("nationDatabase2 - checkItems starting");
        for (int i = 0; i < allNations.size(); i++) {
            tmpNatFile = allNations.get(i);
            if(arg1.getNation().equals(tmpNatFile.getNation()))
                if(tmpNatFile.checkItems(arg1))
                    return true;
            else
                System.out.println("Nation "+arg1.getNation()+" not recognised");
        }
        System.out.println("nationDatabase2/checkItems: FAIL.");
        return false;
    }

    /**
    * arg 1 is nation name. arg 2&3 are weapons and args 4-11 are magazines.
    */
    public weapMagObj weapLoadout(String arg1, String arg2, String arg3, String arg4, String arg5, String arg6,
                                           String arg7, String arg8, String arg9, String arg10, String arg11) {
        for (int i = 0; i < allNations.size(); i++) {
            tmpNatFile = allNations.get(i);
            if(arg1.equals(tmpNatFile.getNation()))
                return tmpNatFile.weapLoadout(arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
        }
        System.out.println("nationDatabase2/weapLoadout: FAIL.");
        return null;
    }

    /** 
    * Used in class gui/weaponInfo
    */
    public String[] getAllWeaponNames() {
        tmpVec = new ArrayList<weapFile>();
        for (int i=0; i<allWeapons.size(); i++) {
            tmpWeapFile = allWeapons.get(i);
            tmpVec.add(tmpWeapFile.getName());
        }
        //return (String[])tmpVec.toArray();
        //tmpStrAr = new String[tmpVec.size()];
        //tmpVec.copyInto(tmpStrAr);
        tmpStrAr = (String[])tmpVec.toArray();
        java.util.Arrays.sort(tmpStrAr);  //sorts the array alphabeticaly
        return tmpStrAr;
    }

    /**
    * Used in class gui/weaponInfo
    * allWeapons and jarWeapons *should* the same length, and each sequential element
    * refer to the same weapon.
    */
    public String getWeapInfo(String arg1) {
        for (int i=0; i<allWeapons.size(); i++) {
            tmpWeapFile = allWeapons.get(i);
            if(tmpWeapFile.getName().equals(arg1)) {
                return new jarCustomExtractor(jarsWeapons.get(i)).extractInfo();
            }
        }
        return "";
    }
   
    /**
    * Used in class gui/weaponInfo & gui/dialogForceBuild
    * @param arg1 : either 'nation' or 'weapon'
    * @param arg2 : name of picture to be extracted
    */
    public ImageIcon getPic(String arg1, String arg2) {
        System.out.println("nationDatabase2/getPic - initialising");
        if(arg1.equals("WEAPON")) {
            System.out.println("nationDatabase2/getPic - WEAPON : "+arg2);
            for (int i=0; i<allWeapons.size(); i++) {
                tmpWeapFile = allWeapons.get(i);
                if(tmpWeapFile.getName().equals(arg2)) {
                    return new jarCustomExtractor(jarsWeapons.get(i)).extractImage();
                }
            }
        }
        if(arg1.equals("NATION")) {
            System.out.println("nationDatabase2/getPic - NATION : "+arg2);          
            for (int i=0; i<allNations.size(); i++) {
                tmpNatFile = allNations.get(i);
                if(tmpNatFile.getNation().equals(arg2)) {
                    return new jarCustomExtractor(jarsNations.get(i)).extractImage();
                }
            }
        }
        return null;
    }

//**************************

    public int getNumNations() { return allNations.size(); }
    public int getNumWeapons() { return allWeapons.size(); }
}