package infStrike.objects;

import java.awt.geom.Point2D;
import java.awt.geom.AffineTransform;


/**
* given the parameters, this will provide an array of co-ord offsets to produce the formation
*/

public class AIFormation {
    private int num;   //number of people in formation
    private String type;  //type of formation (column, wedge, etc)
    private Point2D.Double Cor;  //leaders co-ord
    private Point2D.Double[] allCors;
    private int Rot;
    int spacing = 5;  //value in metres

    private AffineTransform trans;

    public AIFormation(int num, String type, Point2D.Double Cor, int Rot) {
        this.num = num;
        this.type = type.toUpperCase();   
        this.Cor = Cor;
        this.Rot = Rot;
        this.allCors = new Point2D.Double[num];

        this.trans = new AffineTransform();  
 
        calcPositions();
    }

    private void calcPositions() {
        if (type == "WEDGE")
            wedgeFormation();
        else if (type == "BOX")
            boxFormation();
        else if (type == "LINE")
            lineFormation();
        else if (type == "COLUMN")
            columnFormation();
        else
            wedgeFormation();   //the default formation
    }

    private void wedgeFormation() {
        allCors[0] = Cor;
        double value;
        for(int i=1; i<allCors.length; i++) {
            value = ((double)i/2)+0.2;   // the 0.2 is required for when i=1 as spacing*value =0
            if(i%2 == 0) {    //right
                allCors[i] = new Point2D.Double(Cor.x+(spacing*value), Cor.y+(spacing*value));
            }
            else {            //left
                allCors[i] = new Point2D.Double(Cor.x-(spacing*value), Cor.y+(spacing*value));
            }
        }
        rotatePositions();
    } 
    private void boxFormation() {
        rotatePositions();
    }
    private void lineFormation() {
        allCors[0] = Cor;
        for(int i=1; i<allCors.length; i++) {
            if(i%2 == 0) {    //right
                allCors[i] = new Point2D.Double(Cor.x+(spacing*(i/2)), Cor.y);
            }
            else {            //left
                allCors[i] = new Point2D.Double(Cor.x-(spacing*(i/2)), Cor.y);
            }
        }
        rotatePositions();
    }
    private void columnFormation() {
        allCors[0] = Cor;
        for (int i=1; i<allCors.length; i++) {
            allCors[i] = new Point2D.Double(Cor.x, Cor.y-(spacing*i));
        }
        rotatePositions();
    }

    /**
    * Rotates the formation so that it faces the direction of the platoon leader
    */
    private void rotatePositions() {
        trans.setToIdentity();
        trans.rotate(Math.toRadians(Rot), Cor.x, Cor.y);  //make Rot negative to rotate clockwise
        trans.transform(allCors, 1, allCors, 1, allCors.length-1);
    }

    public void update(Point2D.Double Cor, int Rot) {
        this.Cor = Cor;
        this.Rot = Rot;
        calcPositions();
    }

    public String getType() { return type; }
    public Point2D.Double getSpecificPosition(int arg1) { return allCors[arg1]; }
}