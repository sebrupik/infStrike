package infStrike.eight35Eng;

import java.awt.*;

public class engPoint3D {
    private engVector point  = new engVector();
    private engVector tPoint = new engVector();
    private Color    colour;

    public engPoint3D(double x,double y,double z,Color c) {
       point.setValueAt(0,x);
       point.setValueAt(1,y);
       point.setValueAt(2,z);
       point.setValueAt(3,1);
       tPoint.setValueAt(0,x);
       tPoint.setValueAt(1,y);
       tPoint.setValueAt(2,z);
       tPoint.setValueAt(3,1);
       colour=c;
    }

    public void transform(engMatrix m) {
       tPoint=point.transform(m);
    }
 
    public void draw(Graphics g,int centerX,int centerY) {
       g.setColor(colour);
       g.drawLine(getDrawX()+centerX,getDrawY()+centerY,getDrawX()+centerX,getDrawY()+centerY+1);
    }

    public void fill(Graphics g,int centerX,int centerY) {
       draw(g,centerX,centerY);
    }

    public int getDrawX() {
       return (int)(tPoint.getValueAt(0)/(tPoint.getValueAt(3)+1));
    }

    public int getDrawY() {
       return (int)(tPoint.getValueAt(1)/(tPoint.getValueAt(3)+1));
    }

    public double getAverageZ() {
       return tPoint.getValueAt(2);
    }
}