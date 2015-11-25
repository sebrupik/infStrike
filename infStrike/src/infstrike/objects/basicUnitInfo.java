package infStrike.objects;

/**
* this is purely a class to store very basic soldier info (enough to create a Soldier object)
* The class was created as it will use less memory than a Soldier object.
*/

public class basicUnitInfo {
    private int side;
    private String nation;
    private String name;
    private double rank;
    private double cammo;
    private double armour;
    private weapMagObj weapLoadout;

    public basicUnitInfo(int side, String nation, String name, double rank, double cammo, double armour, weapMagObj weapLoadout) {
        this.side = side;
        this.nation = nation;
        this.name = name;
        this.rank = rank;
        this.cammo = cammo;
        this.armour = armour;
        this.weapLoadout = weapLoadout;
    }

//******************
    public int getSide() { return side; }
    public String getNation() { return nation; }
    public String getName() { return name; }
    public double getRank() { return rank; }
    public double getArmour() { return armour; }
    public double getCammo() { return cammo; }
    public weapMagObj getWeapLoadout() { return weapLoadout; }
    public String getWeaponPri() { return weapLoadout.getWeapPriName(); }
    public String getWeaponSec() { return weapLoadout.getWeapSecName(); }
    public String[] getMagType() { return weapLoadout.getMagType(); }
    public String getWeaponUser() { return weapLoadout.getWeaponUser(); }
}