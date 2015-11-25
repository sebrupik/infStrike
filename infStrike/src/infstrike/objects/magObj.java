package infStrike.objects;

public class magObj {
    private String magType;
    private String ammoType;
    private int roundsLeft;
    private int capacity;

    public magObj(String arg1, String arg2, int arg3) {
        magType = arg1;
        ammoType = arg2;
        roundsLeft = arg3;
        capacity = arg3;
    }

    public void addRounds(int arg1) {
        roundsLeft += arg1;
    }
    public void remRounds(int arg1) {
        roundsLeft -= arg1;
    }

    public String status() {
        return ammoType+", "+roundsLeft;
    }
    public String getMagType() {
        return magType;
    }
    public String getAmmoType() {
        return ammoType;
    }
    public int getRoundsLeft() {
        return roundsLeft;
    }
}