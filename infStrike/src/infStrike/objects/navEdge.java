package infStrike.objects;

public class navEdge {
    private double[] ori = new double[2]; 
    private double[] des = new double[2];
    private int desID;
    private int cost; // included for further expansion. eg cost=2 for traveling into forest
    private double dist;
    
    /**
    * Currently cost is only 2 IF the destination is in a forest.
    * The edge PASSING through a forest is not noticed. :(
    */

    public navEdge(double arg1, double arg2, double arg3, double arg4, int arg5, int arg6) {
        ori[0] = arg1;
        ori[1] = arg2;
        des[0] = arg3;
        des[1] = arg4;
        desID = arg5;
        cost = arg6;
        calcDist();
    }

    /**
    * This method calculates the distance between two navNodes
    */
    private void calcDist() {
        double a;
        double o;

        if (ori[0] > des[0]) {      //des left
            if(ori[1] < des[1]) {   //des below
                o = des[1] - ori[1];
                a = ori[0] - des[0];
                dist = Math.sqrt(Math.pow(a, 2)+Math.pow(o, 2));
            }
            if(ori[1] > des[1]) {    //des below
                a = ori[1] - des[1];
                o = ori[0] - des[0];
                dist = Math.sqrt(Math.pow(a, 2)+Math.pow(o, 2));
            }
        }
        if (ori[0] < des[0]) {       //des right
            if(ori[1] < des[1]) {    //des below
                a = des[1] - ori[1];
                o = des[0] - ori[0];
                dist = Math.sqrt(Math.pow(a, 2)+Math.pow(o, 2));
            }
            if(ori[1] > des[1]) {    //des below
                o = ori[1] - des[1];
                a = des[0] - ori[0];
                dist = Math.sqrt(Math.pow(a, 2)+Math.pow(o, 2));
            }
        }
    }

    //****************** return methods
    public double getDist() {
        return dist;
    }
    public int getDesID() {
        return desID;
    }
    public int getCost() {
        return cost;
    }
    public double[] getDesCor() {
        return des;
    }
}