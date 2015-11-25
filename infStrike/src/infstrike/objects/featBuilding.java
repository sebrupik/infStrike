package infStrike.objects;

import java.awt.Graphics2D;
import java.awt.Color;

public class featBuilding extends featObj {
    int floors;

    public featBuilding(int arg1, int[] arg2, int[] arg3, String arg4, int arg5) {
        super(arg1, arg2, arg3, new Color(103, 103, 103), arg4);
        this.floors = arg5;
    }

    public void updateGraphics(double time, Graphics2D g2) {
        super.updateGraphics(time, g2);
        g2.setColor(new Color(0, 0, 0));
        g2.drawPolygon(super.getPoly());
    }

    public int getNumFloors() { return floors; }
}