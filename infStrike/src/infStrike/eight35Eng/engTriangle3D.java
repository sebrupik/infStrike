package infStrike.eight35Eng;

import java.awt.*;

public class engTriangle3D {
    private engPoint3D point1;
    private engPoint3D point2;
    private engPoint3D point3;
    private Color colour;

    public engTriangle3D(double x,double y,double z,
                        double xx,double yy,double zz,
                        double xxx,double yyy,double zzz,
                        Color c) {

        point1 =new engPoint3D(x,y,z,c);
        point2=new engPoint3D(xx,yy,zz,c);
        point3=new engPoint3D(xxx,yyy,zzz,c);
        colour = c;
    }

    public engTriangle3D(int x, int y, int z,
                        int xx, int yy, int zz,
                        int xxx, int yyy, int zzz,
                        Color c) {

        point1 =new engPoint3D((double)x, (double)y, (double)z,c);
        point2=new engPoint3D((double)xx, (double)yy, (double)zz,c);
        point3=new engPoint3D((double)xxx, (double)yyy,(double)zzz,c);
        colour = c;
    }

    public void draw(Graphics g,int centerX,int centerY) {
        g.setColor(colour);
        g.drawLine(point1.getDrawX()+centerX,point1.getDrawY()+centerY,point2.getDrawX()+centerX,point2.getDrawY()+centerY);
        g.drawLine(point2.getDrawX()+centerX,point2.getDrawY()+centerY,point3.getDrawX()+centerX,point3.getDrawY()+centerY);
        g.drawLine(point3.getDrawX()+centerX,point3.getDrawY()+centerY,point1.getDrawX()+centerX,point1.getDrawY()+centerY);
   }
}