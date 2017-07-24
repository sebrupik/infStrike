package infStrike.objects;

import java.util.*;
import java.awt.geom.Point2D;

/**
* Esentialy a class to manage entries to a hashtable
* The entries will contain Vectors of particular types (defence, attack)
* and is open ended, so anything can be added, it is just a matter of 
* understanding it.
*
* It should in theory keep a log of every mission issued (interesting?!)
*/

public class Blackboard {
    private Hashtable openMissions;  //new mission not taken
    private Vector activeMissions;   //mission currently being executed
    private int side;

    private Hashtable ht;  //temp hashtable

    public Blackboard(int arg1) {
        this.side = arg1;
        openMissions =  new Hashtable();
    }

    /**
    * returns an int representing the number of missions of type 'missionType' 
    * contained in base 'baseName' hashtable.
    */
 
    public int OMContains(String missionType, String baseName) {
        //System.out.println("Blackboard/OMContains - does "+baseName+" contain missions of type : "+missionType);
        int num;
        ht = (Hashtable)openMissions.get(baseName);
        if (ht != null) {
            //System.out.println(ht.containsKey(missionType));
            if(((ArrayList)ht.get(missionType)) == null)
                num=0;
            else
                num = ((ArrayList)ht.get(missionType)).size();
 
            return num;
        }
        System.out.println("Blackboard/OMContains - "+baseName+" does not even have a hashtable");
        return 0;
    }

    public ArrayList OMGet(String arg1) { return (ArrayList)openMissions.get(arg1); }

    public AIMission OMGetMission(String missionType, String baseName) {
        ht = (Hashtable)openMissions.get(baseName);
        if (ht != null) {
            ArrayList a = (ArrayList)ht.get(missionType);
            if (a != null && a.size() > 0) {
                System.out.println(a.size()+" "+missionType+" missions available");
                return (AIMission)a.remove(0);
            }
        }
        return null;
    }


    /**
    * V2 - 26/03/03
    * hashtable now contains base specific hashtables which contain the missions
    */
    public void OMPut(AIMission mission) {
        ArrayList aL;
        Hashtable h;
        
        if (openMissions.containsKey(mission.getBase().getName())) {  //has the base got its own hashtable?
            System.out.println("Blackboard/OMPut - hashtable available for "+mission.getBase().getName());
            h = (Hashtable)openMissions.get(mission.getBase().getName());
            HTPut(h, mission);
        }
        else {  // no it has not
            System.out.println("Blackboard/OMPut - no slot available for mission of type "+mission.getType());
            h = new Hashtable();
            aL = new ArrayList();
            aL.add(mission);
            h.put(mission.getType(), aL);
            openMissions.put(mission.getBase().getName(), h);
        }

        System.out.println("********************* print all keys for h");
        for (Enumeration e = h.keys() ; e.hasMoreElements() ;) {
             System.out.println((String)e.nextElement());  
        }

        System.out.println("********************* print all keys for openMissions");
        for (Enumeration e = openMissions.keys() ; e.hasMoreElements() ;) {
             System.out.println((String)e.nextElement());  
        }
    }

    private void HTPut(Hashtable ht, AIMission mission) {
        ArrayList aL;

        if (ht.containsKey(mission.getType())) {
            aL = (ArrayList) ht.get(mission.getType());
            aL.add(mission);
        } else {
            aL = new ArrayList();
            aL.add(mission);
            ht.put(mission.getType(), aL);
        }
    }

    /**
    * Adds BlackboardPlatoonSpecificMission objects to the 'platoonSpecific' bucket
    * in the hashtable
    */
    public void addPlatoonSpecificMission(BlackboardPlatoonSpecificMission BPSMission) {
        System.out.println("Blackboard/addPlatoonSpecificMission - specific mission being added for "+BPSMission.ID);
        ArrayList aL;

        if (openMissions.containsKey("platoonSpecific")) {
            aL = (ArrayList) openMissions.get("platoonSpecific");
            aL.add(BPSMission);
        }
        else {   //create new mission slot
            aL = new ArrayList();
            aL.add(BPSMission);
            openMissions.put("platoonSpecific", aL);
        }
    }

    public void addPlatoonSpecificMission(ArrayList<BlackboardPlatoonSpecificMission> v) {
        for (int i=0; i<v.size(); i++) {
            this.addPlatoonSpecificMission(v.get(i));
        }
    }

    public AIMission getPlatoonSpecificMission(String ID) {
        //System.out.println("Blackboard/getPlatoonSpecificMission - processing request for "+ID);
        ArrayList aL = this.OMGet("platoonSpecific");
        int index = -1;
        if (aL != null) {
            for (int i=0; i<aL.size(); i++) {
                if ( ((BlackboardPlatoonSpecificMission)aL.get(i)).ID.equals(ID) ) {
                    index = i;
                }
            }
            if (index != -1)
                return ((BlackboardPlatoonSpecificMission)aL.remove(index)).mission;
        }
        return null;
    }

    /**
    * Remove all missions of type 'missionType' which end at 'location';
    */
    public void removePlatoonSpecificMission(String missionType, Point2D.Double location) {
        BlackboardPlatoonSpecificMission BPSM;
        ArrayList aL = this.OMGet("platoonSpecific");
        
        for (Iterator e = aL.iterator() ; e.hasNext() ;) {
            BPSM = (BlackboardPlatoonSpecificMission)e.next();
            if (BPSM.mission.getType().equals(missionType) & BPSM.mission.getFinalWaypoint().equals(location))
                e.remove();
        }
    }

    /**
    * takes an arraylist of platoons requesting missions
    * 
    */
    public void getMissions(ArrayList a, AIMissionPriority AIMP, String baseName) {
        AIPlatoon AIP;
        AIMission mission;
        int index = 0;

        ArrayList aL = AIMP.calcNumOfMissions(a.size());

        for (Iterator e = a.iterator() ; e.hasNext() ;) {
            AIP = (AIPlatoon)e.next();
            mission = null;
            mission = this.getPlatoonSpecificMission(AIP.getID());

            if(mission == null & aL.size() > 0) {   // no specific mission waiting
                mission = tryToGetMission(aL, baseName);
            }

            if (mission != null) {
                AIP.setMission(mission);
                e.remove();          //misison given so remove mission request
            }
        }
    }
 
    /**
    * Goes through a list of misison types (aL) asking the base (baseName)
    * for that type of mission. To maintain the priority system, the element
    * used for the request is removed.
    */
    public AIMission tryToGetMission(ArrayList aL, String baseName) {
        AIMission mission;
        for (Iterator e = aL.iterator() ; e.hasNext() ;) {
            mission = (AIMission)OMGetMission((String)e.next(), baseName);
            e.remove();
            if (mission != null)
                return mission;
        }
        return null;
    }


    public boolean AMContains(String arg1) {
        for (int i=0; i<activeMissions.size(); i++) {
            //((aiMission)activeMissions.elementAt(i)).getType()
        }
        return true;   //for now
    }

    public int getSide() { return side; }
}