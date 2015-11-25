package infStrike.objects;

import java.awt.Graphics2D;
import java.awt.Color;

public class featBush extends featObj{
    int size;

    public featBush(int arg1, int arg2, int size) {
        super(1, new int[]{arg1}, new int[]{arg2}, Color.green, "Bush");
        this.size = size;
    }


    public void updateGraphics(double time, Graphics2D g2) {
        g2.setColor(Color.green);
        g2.fillOval( getX()[0], getY()[0], size, size);
    }
}