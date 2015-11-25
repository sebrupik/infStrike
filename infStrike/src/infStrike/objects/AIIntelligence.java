package infStrike.objects;

import java.awt.geom.Point2D;

public class AIIntelligence {
    private Object who;
    private Object what;
    private Point2D.Double where;
    private double value;
    private double time;

    public AIIntelligence(Object who, Object what, Point2D.Double where, double value, double time) {
        this.who = who;
        this.what = what;
        this.where  = where;
        this.value = value;  //use for anything. mainly for enemy numbers.
        this.time = time;
    }

    public Object getWho() { return who; }
    public Object getWhat() { return what; }
    public Point2D.Double getWhere() {  return where; }
    public double getValue() { return value; }
    public double getTime() { return time; }
}