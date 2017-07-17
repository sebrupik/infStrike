package infStrike.utils;

import java.util.*;

public class schema {
    private ArrayList<String> attrName;
    private ArrayList<String> type;

    public schema() {
        attrName = new ArrayList<>();
        type = new ArrayList<>();
    }

    public void addAttrName(String arg1) {
        attrName.add(arg1);
    }

    public void addType(String arg1) {
        type.add(arg1);
    }

    public int getAttrSize() {
        return attrName.size();
    }
 
    public boolean compare(String[][] arg1) {
        for (int i=0;i<attrName.size();i++) {
            if (!(attrName.get(i).equals(arg1[0][i]))) {
                System.out.println("Problem with "+attrName.get(i)+" and "+arg1[0][i]+" as the "+i+" entry");
                return false;
            }
        }
        return true;
    }

    @Override public String toString() {
        String temp = new String();
        for (int i = 0; i < attrName.size(); i++) { 
            temp += attrName.get(i);
            if (i < attrName.size())
                temp += ", ";
        }
        return temp;
    }
}