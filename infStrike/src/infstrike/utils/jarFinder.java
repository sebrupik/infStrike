package infStrike.utils;

import infStrike.objects.weapFile;
import infStrike.objects.nationFile2;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.net.*;
import javax.swing.*;

/**
* Class that performs run-time class loading from JAR files.
* used to be part of nationDatabase2, but was created so that
* I could use a JProgressBar, and to stop the classes being re-loaded
* every time nationDatabase was created.
*/


public class jarFinder {
    JProgressBar curPro;
    Hashtable weaponTable;
   
    // Vectors containing specific classes.
    Vector allWeapons;
    Vector allNations;        
 
    // Vectors containg JAR URLs
    Vector jarsWeapons;
    Vector jarsNations;

    jarCustomExtractor jEx;

    Vector tmpVec;
    nationFile2 tmpNatFile;
    weapFile tmpWeapFile;
    String[] tmpStrAr;
    double proInc = 100/6; // 6 is the number of main methods in the constructor
    double tmpInt, value;

    public jarFinder(JProgressBar curPro, Vector jarsWeapons, Vector jarsNations) {
        this.curPro = curPro;
        this.jarsWeapons = jarsWeapons;
        this.jarsNations = jarsNations;
        weaponTable = new Hashtable();

        this.setText("Begining JAR loading", 0.0);
        loadNationJars();
        loadWeaponJars();
        loadNations();
        loadWeapons();
        sortWeapons();
        loadNationWeapons(); //now load the weapons into the nations
        this.setText("Runtime class loading complete.", 100.0);
    }

//**************************************************************
//** hand all relevant weapon info and classes to the nations **
//**************************************************************

    /**
    * Goes through the nation hashtable and extracts each nations weapon vector.
    * The nation vector is then searched for an owner, and if in existance is 
    * passed the vector and weapon classes.
    */
    private void loadNationWeapons() {
        this.setText("Loading weapon classes into nation files", 80.0);
        Vector v;
        String tmpStr;
        String[] str = new String[5];
        Enumeration e1 = weaponTable.keys();
        while (e1.hasMoreElements()) {
            tmpStr = (String)e1.nextElement();
            v = (Vector)weaponTable.get(tmpStr);
            tmpInt = proInc/allNations.size();
            value = curPro.getValue();
            for (int i=0; i < allNations.size(); i++) {
                tmpNatFile = (nationFile2)allNations.elementAt(i);
                value = curPro.getValue()+tmpInt;
                if (tmpNatFile.getNation().equals(tmpStr)) {
                    this.setText("Loading weapons for "+tmpNatFile.getNation(), value);
                    tmpNatFile.loadWeaponInfo(v); 
                    tmpNatFile.loadWeaponClasses(findWeaponClasses(v));
                }
            }
        }
    }

    /**
    * Given a vector of weapon info, a vector containing the 
    * corresponding weapon classes will be returned.
    */
    private Vector findWeaponClasses(Vector v) {
        System.out.println("nationDatabase/findWeaponClasses - Initialising");
        String str;
        Vector tmp = new Vector();
        for (int i=0; i<v.size(); i++) {
            str = ((String[])v.elementAt(i))[0]; // get the weapon name
            //System.out.println(str);
            for (int j=0; j<allWeapons.size(); j++) {
                tmpWeapFile = (weapFile)allWeapons.elementAt(j);
                //System.out.println("Weapon in the vector is "+tmpWeapFile.getName());
                if (tmpWeapFile.getName().equals(str)) {
                    tmp.addElement(tmpWeapFile);
                }
            }
        }
        System.out.println("nationDatabase/findWeaponClasses - Finished");
        return tmp;
    }

//***********************
//** find the JAR URLs **
//***********************

    private void loadWeaponJars() {
        System.out.println("nationDatabase2/loadWeaponJars - initialising");
        this.setText("Begining Weapon JAR loading", 16.0);
        File root = new File("weapons");
        jarsWeapons = new Vector();
        loadJars(root, jarsWeapons);
    }
    private void loadNationJars() {
        System.out.println("nationDatabase2/loadNationJars - initialising");
        this.setText("Begining Nation JAR loading", 0.0);
        File root = new File("nations");
        jarsNations = new Vector();
        loadJars(root, jarsNations);
    }

    /**
    * Doesn't really load the JARs merely finds and stores their URLs
    */
    private void loadJars(File arg1, Vector arg2) {
        System.out.println("nationDatabase2/loadJars - initialising");
        if (arg1.exists()) {
            String[] files = arg1.list();
            if (files != null) {
                tmpInt = proInc/files.length;
                value = curPro.getValue();
                for (int i = 0; i < files.length; i++) {
                    /*try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }*/
                    File file = new File(arg1,files[i]);
                    if (!file.isDirectory() && file.getName().toLowerCase().endsWith(".jar")) {
                        this.setText("JAR file "+file+" found", value);
                        try {
                            value += tmpInt;
                            System.out.println("tmpInt value "+tmpInt);
                            if (checkJars(file.toURL())) {
                                this.setText("JAR file "+file+" is valid", value);
                                arg2.addElement(file.toURL());
                            }
                            else {
                                this.setText("JAR file "+file+" is NOT valid!", value);
                            }
                        } 
                        catch (MalformedURLException e) {
                            System.out.println(e);
                        }
                    }
                }
            }
        }
    }

//*****************************************
//** load the classes using the JAR URLs **
//*****************************************

    private void loadWeapons() {
        System.out.println("nationDatabase2/loadWeapons - initialising");
        this.setText("Preparing to load weapon classes", 48.0);
        URL[] urls = new URL[jarsWeapons.size()];
        jarsWeapons.copyInto(urls);
        for (int i=0; i<urls.length; i++) {
            System.out.println("in the urls variable : "+urls[i]);
        }
        allWeapons = new Vector();
        new WeaponClassLoader(urls, allWeapons);
        System.out.println("nationDatabase2/loadWeapons - there are "+allWeapons.size()+" loaded");
    }

    private void loadNations() {
        System.out.println("nationDatabase2/loadNations - initialising");
        this.setText("Preparing to load nation classes", 32.0);
        URL[] urls = new URL[jarsNations.size()];
        jarsNations.copyInto(urls);
        for (int i=0; i<urls.length; i++) {
            System.out.println("in the urls variable : "+urls[i]);
        }
        allNations = new Vector();
        new NationClassLoader(urls, allNations);
        System.out.println("nationDatabase2/loadNations - there are "+allNations.size()+" loaded");
    }

    /**
    * Given an array of weapon JAR URLs this method extracts the setup file from each one.
    * Using the information in this file, nations who use this weapon can be found, 
    * and the weapon info put in the relevant hashtable bucket.
    * str1 elements 0-5 : weapon name, primary/secondary, weapon type, weapon mag, mag capacity, calibre
    * Each seperate hashtable bucket contains all weapons for one nation
    */
    private void sortWeapons() {
        System.out.println("nationDatabase2/sortWeapons - initialising xxxxxxxxx");
        this.setText("Sorting weapons.", 64.0);
        URL[] urls = new URL[jarsWeapons.size()];
        jarsWeapons.copyInto(urls);
        String[] str1, str2;
        for (int i=0; i<urls.length; i++) {
            jEx = new jarCustomExtractor(urls[i]);
            str1 = jEx.extractSetup();
            str2 = str1[6].split(",");  //put all user nations into an array   
 
            for (int j=0; j<str2.length; j++) {
                if(weaponTable.containsKey(str2[j].trim())) {
                    tmpVec = (Vector)weaponTable.get(str2[j].trim());
                    tmpVec.addElement(new String[]{str1[0], str1[1], str1[2], str1[3], str1[4], str1[5]});
                }
                else {
                    tmpVec = new Vector();
                    tmpVec.addElement(new String[]{str1[0], str1[1], str1[2], str1[3], str1[4], str1[5]});
                    weaponTable.put(str2[j].trim(), tmpVec);
                }
            }
        }
        printWeaponTable();
    }

//************************
    /**
    * Given an URL for a JAR (weapon or nation) its contents are remotely checked
    */
    private boolean checkJars(URL u) {
        System.out.println("nationDatabase2/checkJars - running");
        return new jarCustomExtractor(u).checkJar();
    }

    public void printWeaponTable() {
        Vector v;
        String tmpStr;
        String[] str = new String[5];
        Enumeration e1 = weaponTable.keys();
        while (e1.hasMoreElements()) {
             tmpStr = (String)e1.nextElement();
             v = (Vector)weaponTable.get(tmpStr);
             System.out.println("<"+tmpStr+">");
             for (int i=0; i < v.size(); i++) {
                 str = (String[])v.elementAt(i);
                 System.out.println(str[0]+", "+str[1]+", "+str[2]+", "+str[3]+", "+str[4]);
             }
        }
    }

    public Vector[] getVectors() {
        return new Vector[]{allWeapons, allNations, jarsWeapons, jarsNations};
    }

    private void setText(String arg1, double arg2) {
        curPro.setString(arg1);
        curPro.setValue((int)arg2);
    }
}