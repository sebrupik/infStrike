package infStrike.eight35Eng;

import java.awt.*;
import java.util.Vector;
import java.awt.Graphics;

public class engWorld {
    private Vector worldVec;

    private engTriangle3D tempTri;

    public engWorld() {
        worldVec = new Vector();
    }

    public void addTriangle(engTriangle3D arg1) {
        worldVec.addElement(arg1);
    }

    public void draw(Graphics g, int arg1, int arg2) {
        for (int i=0; i<worldVec.size(); i++) {
            if (worldVec.elementAt(i) instanceof engTriangle3D) {
                tempTri = (engTriangle3D)worldVec.elementAt(i);
                tempTri.draw(g, arg1, arg2);
            }
        }
    }
}