package infStrike.objects;

public class objectTarget {
    double attack_value;
    RCSInfluenceMapPosition target;   

    public objectTarget(double attack_value, RCSInfluenceMapPosition target) {
        this.attack_value = attack_value;
        this.target = target;
    }   
}