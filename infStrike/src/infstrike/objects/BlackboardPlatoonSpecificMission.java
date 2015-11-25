package infStrike.objects;

public class BlackboardPlatoonSpecificMission {
    String ID;
    AIMission mission;

    BlackboardPlatoonSpecificMission(String ID, AIMission mission) {
        this.ID = ID;
        this.mission = mission;
    }
}