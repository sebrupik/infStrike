package infStrike.objects;

import java.util.ArrayList;

public class AIMissionPriority {
    AIMPE[] entries;  

    ArrayList missionTypeList;
 
    public AIMissionPriority(String[] mission_types) {
        entries = new AIMPE[mission_types.length];

        for (int i=0; i<entries.length; i++) {
            entries[i] = new AIMPE(1.0/entries.length, mission_types[i]);
        }
    }    
    
    public void reorder() { java.util.Arrays.sort(entries); }
    public int getSize() { return entries.length; }
    public AIMPE getElementAt(int index) { return entries[index]; }

    public ArrayList calcNumOfMissions(int num) {
        int numMissions;
        missionTypeList = new ArrayList();

        for (int i=0; i<entries.length; i++) {
            numMissions = (int)((((double)num/100)) * ((entries[i].value/1.0)*100));
            if (numMissions < 1)
                numMissions = 1;

            for (int j=0; j<numMissions; j++) {
                missionTypeList.add(entries[i].mission);
            }
        }
        return missionTypeList;
    }
}

class AIMPE implements Comparable {
    double value;
    String mission;

    public AIMPE(double value, String mission) {
        this.value = value;
        this.mission = mission;
    }

    public int compareTo(Object o) {
         //try {
             if ( ((AIMPE)o).value < this.value) 
                 return -1; 
             else if ( ((AIMPE)o).value == this.value) 
                 return 0; 
             else 
                 return 1; 

         //} catch (ClassCastException e) { }
    }
}