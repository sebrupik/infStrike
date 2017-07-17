package infStrike.eight35Eng;

import java.awt.*;

public class engTriangle3D {
    private engPoint3D[] points;
    //private engPoint3D point2;
    //private engPoint3D point3;
    private Color colour;

    public engTriangle3D(double x,double y,double z,
                        double xx,double yy,double zz,
                        double xxx,double yyy,double zzz,
                        Color c) {
        points = new engPoint3D[3];
        
        points[0] = new engPoint3D(x,y,z,c);
        points[1] = new engPoint3D(xx,yy,zz,c);
        points[2] = new engPoint3D(xxx,yyy,zzz,c);
        colour = c;
    }

    public engTriangle3D(int x, int y, int z,
                        int xx, int yy, int zz,
                        int xxx, int yyy, int zzz,
                        Color c) {

        points[0] = new engPoint3D((double)x, (double)y, (double)z,c);
        points[1] = new engPoint3D((double)xx, (double)yy, (double)zz,c);
        points[2] = new engPoint3D((double)xxx, (double)yyy,(double)zzz,c);
        colour = c;
    }

    public void draw(Graphics g,int centerX,int centerY) {
        g.setColor(colour);
        
        g.drawLine(points[0].getDrawX()+centerX,points[0].getDrawY()+centerY,points[1].getDrawX()+centerX,points[1].getDrawY()+centerY);
        g.drawLine(points[1].getDrawX()+centerX,points[1].getDrawY()+centerY,points[2].getDrawX()+centerX,points[2].getDrawY()+centerY);
        g.drawLine(points[2].getDrawX()+centerX,points[2].getDrawY()+centerY,points[0].getDrawX()+centerX,points[0].getDrawY()+centerY);
    }
    
    public void fill(Graphics g,int centerX,int centerY) {
       for (int i=0; i<3; i++)
           points[i].draw(g,centerX,centerY);
    }
    
    public void transform(engMatrix m) {
        for (int i=0; i<3; i++)
            points[i].transform(m);
    }
    
    public double getAverageZ() {
        return (points[0].getAverageZ() + points[1].getAverageZ() + points[2].getAverageZ()) / 3;
    }
}