package infStrike.utils;

import infStrike.objects.weapFile;
import infStrike.objects.nationFile2;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;
import java.util.ArrayList;
import java.net.*;
import javax.swing.*;

/**
* Class that performs run-time class loading from JAR files.
* used to be part of nationDatabase2, but was created so that
* I could use a JProgressBar, and to stop the classes being re-loaded
* every time nationDatabase was created.
*/


public class jarFinder {
    private final String _CLASS;
    JProgressBar curPro;
    HashMap<String, ArrayList> weaponTable;
   
    // Vectors containing specific classes.
    ArrayList<weapFile> allWeapons;
    ArrayList<nationFile2> allNations;        
 
    // Vectors containg JAR URLs
    ArrayList<URL> jarsWeapons;
    ArrayList<URL> jarsNations;

    jarCustomExtractor jEx;

    ArrayList<String[]> tmpVec;
    nationFile2 tmpNatFile;
    weapFile tmpWeapFile;
    String[] tmpStrAr;
    double proInc = 100/6; // 6 is the number of main methods in the constructor
    double tmpInt, value;

    public jarFinder(JProgressBar curPro, ArrayList<URL> jarsWeapons, ArrayList<URL> jarsNations) {
        this.curPro = curPro;
        this.jarsWeapons = jarsWeapons;
        this.jarsNations = jarsNations;
        weaponTable = new HashMap<>();
        
        this._CLASS = this.getClass().getName();

        this.setText("Begining JAR loading", 0.0);
        //loadNationJars();
        loadJars(new File("weapons"), "gun", jarsWeapons);
        //loadWeaponJars();
        loadJars(new File("nations"), "nat", jarsNations);
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
        this.setText(_CLASS+"/loadNationWeapons - Loading weapon classes into nation files", 80.0);
        ArrayList<String[]> v;
        String tmpStr;
        String[] str = new String[5];
        for(String s : weaponTable.keySet()) {
            v = weaponTable.get(s);
            tmpInt = proInc/allNations.size();
            value = curPro.getValue();
            for (int i=0; i < allNations.size(); i++) {
                tmpNatFile = allNations.get(i);
                value = curPro.getValue()+tmpInt;
                if (tmpNatFile.getNation().equals(s)) {
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
    private ArrayList findWeaponClasses(ArrayList<String[]> v) {
        System.out.println(_CLASS+"/findWeaponClasses - Initialising");
        String str;
        ArrayList<weapFile> tmp = new ArrayList<>();
        for (int i=0; i<v.size(); i++) {
            str = v.get(i)[0]; // get the weapon name
            //System.out.println(str);
            for (int j=0; j<allWeapons.size(); j++) {
                tmpWeapFile = allWeapons.get(j);
                //System.out.println("Weapon in the vector is "+tmpWeapFile.getName());
                if (tmpWeapFile.getName().equals(str)) {
                    tmp.add(tmpWeapFile);
                }
            }
        }
        System.out.println(_CLASS+"/findWeaponClasses - Finished");
        return tmp;
    }

//***********************
//** find the JAR URLs **
//***********************

    /*
    private void loadWeaponJars() {
        System.out.println(_CLASS+"/loadWeaponJars - initialising");
        this.setText("Begining Weapon JAR loading", 16.0);
        File root = new File("weapons");
        jarsWeapons = new ArrayList<>();
        loadJars(root, jarsWeapons);
    }
    private void loadNationJars() {
        System.out.println(_CLASS+"/loadNationJars - initialising");
        this.setText("Begining Nation JAR loading", 0.0);
        File root = new File("nations");
        jarsNations = new ArrayList<>();
        loadJars(root, jarsNations);
    }
*/
    /**
    * Doesn't really load the JARs merely finds and stores their URLs
    */
    private void loadJars(File arg1, String file_prefix, ArrayList<URL> arg3) {
        System.out.println(_CLASS+"/loadJars - initialising: "+file_prefix);
        if (arg1.exists()) {
            String[] files = arg1.list();
            if (files != null) {
                tmpInt = proInc/files.length;
                value = curPro.getValue();
                for (int i = 0; i < files.length; i++) {
                    //System.out.println(files[i]);
                    /*try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }*/
                    File file = new File(arg1,files[i]);
                    // System.out.println(!file.isDirectory()+" "+file.getName().startsWith(file_prefix)+" "+file.getName().toLowerCase().endsWith(".jar"));
                    if (!file.isDirectory() && file.getName().startsWith(file_prefix) && file.getName().toLowerCase().endsWith(".jar")) {
                        this.setText("JAR file "+file+" found", value);
                        try {
                            value += tmpInt;
                            System.out.println("tmpInt value "+tmpInt);
                            if (checkJars(file.toURI().toURL())) {
                                this.setText("JAR file "+file+" is valid", value);
                                arg3.add(file.toURI().toURL());
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
        System.out.println(_CLASS+"/loadWeapons - initialising");
        this.setText("Preparing to load weapon classes", 48.0);
        URL[] urls = new URL[jarsWeapons.size()];
        //jarsWeapons.copyInto(urls);
        urls = (URL[])jarsWeapons.toArray(urls);
        for (int i=0; i<urls.length; i++) {
            System.out.println("in the urls variable : "+urls[i]);
        }
        allWeapons = new ArrayList<>();
        new WeaponClassLoader(urls, allWeapons);
        System.out.println(_CLASS+"/loadWeapons - there are "+allWeapons.size()+" loaded");
    }

    private void loadNations() {
        System.out.println(_CLASS+"/loadNations - initialising");
        this.setText("Preparing to load nation classes", 32.0);
        URL[] urls = new URL[jarsNations.size()];
        //jarsNations.copyInto(urls);
        urls = jarsNations.toArray(urls);
        
        for (int i=0; i<urls.length; i++) {
            System.out.println("in the urls variable : "+urls[i]);
        }
        allNations = new ArrayList<>();
        new NationClassLoader(urls, allNations);
        System.out.println(_CLASS+"/loadNations - there are "+allNations.size()+" loaded");
    }

    /**
    * Given an array of weapon JAR URLs this method extracts the setup file from each one.
    * Using the information in this file, nations who use this weapon can be found, 
    * and the weapon info put in the relevant hashtable bucket.
    * str1 elements 0-5 : weapon name, primary/secondary, weapon type, weapon mag, mag capacity, calibre
    * Each seperate hashtable bucket contains all weapons for one nation
    */
    private void sortWeapons() {
        System.out.println(_CLASS+"/sortWeapons - initialising xxxxxxxxx");
        this.setText("Sorting weapons.", 64.0);
        URL[] urls = new URL[jarsWeapons.size()];
        //jarsWeapons.copyInto(urls);
        
        urls = (URL[])jarsWeapons.toArray(urls);
        String[] str1, str2;
        for (int i=0; i<urls.length; i++) {
            jEx = new jarCustomExtractor(urls[i]);
            str1 = jEx.extractSetup();
            str2 = str1[6].split(",");  //put all user nations into an array   
 
            for (int j=0; j<str2.length; j++) {
                if(weaponTable.containsKey(str2[j].trim())) {
                    tmpVec = weaponTable.get(str2[j].trim());
                    tmpVec.add(new String[]{str1[0], str1[1], str1[2], str1[3], str1[4], str1[5]});
                }
                else {
                    tmpVec = new ArrayList();
                    tmpVec.add(new String[]{str1[0], str1[1], str1[2], str1[3], str1[4], str1[5]});
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
        System.out.println(_CLASS+"/checkJars - running");
        return new jarCustomExtractor(u).checkJar();
    }

    public void printWeaponTable() {
        ArrayList<String[]> v;
        String[] str;
        for(String k : weaponTable.keySet()) {    
             v = weaponTable.get(k);
             System.out.println("<"+k+">");
             for (int i=0; i < v.size(); i++) {
                 str = v.get(i);
                 System.out.println(str[0]+", "+str[1]+", "+str[2]+", "+str[3]+", "+str[4]);
             }
        }
    }

    public ArrayList[] getVectors() {
        return new ArrayList[]{allWeapons, allNations, jarsWeapons, jarsNations};
    }

    private void setText(String arg1, double arg2) {
        curPro.setString(arg1);
        curPro.setValue((int)arg2);
    }
}