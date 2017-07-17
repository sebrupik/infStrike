package infStrike.eight35Eng;

import java.util.ArrayList;
import java.awt.Graphics;

public class engWorld {
    private final ArrayList<engTriangle3D> worldAr;

    private engTriangle3D tempTri;

    public engWorld() {
        worldAr = new ArrayList<>();
    }

    public void addTriangle(engTriangle3D arg1) {
        worldAr.add(arg1);
    }

    public void draw(Graphics g, int arg1, int arg2) {
        for (int i=0; i<worldAr.size(); i++) {
            if (worldAr.get(i) instanceof engTriangle3D) {
                tempTri = (engTriangle3D)worldAr.get(i);
                tempTri.draw(g, arg1, arg2);
            }
        }
    }
    
    public engTriangle3D get(int index) { return worldAr.get(index); }
}