package infStrike.objects;

import java.awt.Graphics2D;
import java.awt.Color;

public class featForest extends featObj {
    /**
    * npoints, x[], y[], name
    */
    public featForest(int arg1, int[] arg2, int[] arg3, String arg4) {
        super(arg1, arg2, arg3, new Color(1, 68, 3), arg4);
    }

    public void updateGraphics(double time, Graphics2D g2) {
        super.updateGraphics(time, g2);
    }
}