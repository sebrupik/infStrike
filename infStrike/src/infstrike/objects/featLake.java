package infStrike.objects;

import java.awt.Graphics2D;
import java.awt.Color;

public class featLake extends featObj {

    public featLake(int arg1, int[] arg2, int[] arg3, String arg4) {
        super(arg1, arg2, arg3, new Color(24, 0, 255), arg4);
    }

    public void updateGraphics(double time, Graphics2D g2) {
        super.updateGraphics(time, g2);
    }
}