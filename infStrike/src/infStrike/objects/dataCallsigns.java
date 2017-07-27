package infStrike.objects;

import java.util.ArrayList;

public class dataCallsigns {
    final String[] callsign = {"Angel", "Appollo", "Archer", "Banshee", "Bloodhound", "Bulldog", "Charger", "Cheetah", "Chieftan", "Cobra", "Cougar", "Darkhorse", "Demon", "Dodger", "Eight-Ball", "Farsight", "Fireball", "Firefox", "Freelancer", "Gator", "Gauntlet", "Gladiator", "Goliath", "Goose", "Hellcat", "Jaguar", "Jester", "Lancer", "Maniac", "Mauler", "Nightmare", "Panther", "Phantom", "Phoenix", "Raptor", "Razorback", "Sabre", "Snowball", "Spectrum", "Stalker", "Stingray", "Striker", "Sundown", "Swamp-Fox", "Tempest", "Tiger-Shark", "Vertigo", "Viking", "Viper", "Voodoo", "Vulcan", "Vulture", "Warlock", "Warlord", "Warrior", "Wildcat", "Wolfpack"};
    int side;
    
    public dataCallsigns(int side) {
        this.side = side;
    }

    public String makeCallsign(ArrayList<AIPlatoon> al) {
        String tmp = null;
        do {
            tmp = callsign[(int)(Math.random()*callsign.length)];
        }
        while(nameExists(tmp, al) == true);
        return tmp;
    }
  
    /**
    * checks that the name (arg1) is not present in the vector arg2.
    */
    private boolean nameExists(String arg1, ArrayList<AIPlatoon> al) {
        AIPlatoon AIP;
        String tmpStr = side+"-"+arg1;
        for(int i = 0; i < al.size(); i++) {
            AIP = al.get(i);
            if(AIP.getID().equals(tmpStr))
                return true;
        } 
        AIP = null;
        return false;
    }
}