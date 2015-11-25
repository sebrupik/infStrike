package infStrike.objects;

import java.awt.geom.*;
import java.awt.Graphics2D;

/*
The base class for all drawable objects
*/

public abstract class Entity {
     
    private Point2D.Double Cor;

    public Entity(Point2D.Double arg1) {
        this.Cor = arg1;
    }

    public abstract void updateGraphics(double time, Graphics2D g2, boolean draw);

    public Point2D.Double getCor() { return Cor; }
    public void setCor(Point2D.Double Cor) { this.Cor = Cor; }
}
