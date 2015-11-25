package infStrike.eight35Eng;

import java.awt.Graphics;
import java.awt.*;
import java.awt.Color;

public class Eight35Eng {
    private engMatrix rotationX   = new engMatrix();
    private engMatrix rotationY   = new engMatrix();
    private engMatrix rotationZ   = new engMatrix();
    private engMatrix translation = new engMatrix();
    private engMatrix scaling     = new engMatrix();
    private engMatrix projection  = new engMatrix();

    private engMatrix rotationMatrix = new engMatrix();
    private engMatrix worldMatrix    = new engMatrix();

    private int[][] pointArray;
    private int gridSize;
    private engWorld[] world;

    

    public Eight35Eng () {
        
    }

    public void loadMapArray(int[][] arg1, int arg2) {
        pointArray = arg1;
        gridSize = arg2;
        world = new engWorld();

        int tmpX = 0;
        int tmpY = 0;
        int eleScale = 0;

        for (int i=0; i<pointArray.length; i++) { // is x
             for (int j=0; j<pointArray[0].length; j++) { // is y
                 tmpX = gridSize*j;
                 if (!((j+1 >= pointArray[0].length) | (i+1 >= pointArray.length))) {
                     world.addTriangle(new engTriangle3D((tmpX+(gridSize/2)), (pointArray[j][i]*eleScale), (tmpY+(gridSize/2)),
                                                        ((tmpX+(gridSize/2))+gridSize), (pointArray[j+1][i+1]*eleScale), ((tmpY+(gridSize/2))+gridSize),
                                                        (tmpX+(gridSize/2)), (pointArray[j+1][i]*eleScale), ((tmpY+(gridSize/2))+gridSize),
                                                        Color.green)); // triangle 1

                     world.addTriangle(new engTriangle3D((tmpX+(gridSize/2)), (pointArray[j][i]*eleScale), (tmpY+(gridSize/2)),
                                                        ((tmpX+(gridSize/2))+gridSize), (pointArray[j][i+1]*eleScale), (tmpY+(gridSize/2)),
                                                        ((tmpX+(gridSize/2))+gridSize), (pointArray[j+1][i+1]*eleScale), ((tmpY+(gridSize/2))+gridSize),
                                                        Color.green)); // triangle 2

                     numberOfObjects += 2;
                 }
             }
             tmpX = 0;
             tmpY += gridSize; 
        }
        System.out.println("Engine fueled and running!");
    }

    /*
     * How have we ended up with two draw methods with the same parameters. How did this compile?!
     * Removed 2015-11-25
     */
    /*public void draw(Graphics g, int arg1, int arg2) { // center x, center y
        world.draw(g, 0, 0);
    }*/

    public void dumpWireframe() {
    }

    //****************************
    private int numberOfObjects;

    private double zValues[];
    private int order[];
   
    public void swap(int i,int j) {
        double T = zValues[i];
        zValues[i] = zValues[j];
        zValues[j] = T;

        int T2 = order[i];
        order[i] = order[j];
        order[j] = T2;
    }

    public void quickSort(int lo0,int hi0)
    {
      int lo=lo0;
      int hi=hi0;
      double mid;

      if (hi0>lo0)
      {
         mid=zValues[(lo0+hi0)>>1];
         while(lo<=hi)
         {
            while ((lo<hi0)&&(zValues[lo]<mid)) ++lo;
            while ((hi>lo0)&&(zValues[hi]>mid)) --hi;

            if (lo<=hi)
            {
               swap(lo,hi);
               ++lo;
               --hi;
            }
         }
         if (lo0<hi) quickSort(lo0,hi);
         if (lo<hi0) quickSort(lo,hi0);
      }
   }

   public void transform(MyMatrix m)
   {
      for (int i=0; i<numberOfObjects; i++)
      {
         world[i].transform(m);
         zValues[i]=-world[i].getAverageZ();
         order[i]=i;
      }
      quickSort(0,numberOfObjects-1);
   }

   public void draw(Graphics g,int centerX,int centerY)
   {
      for (int i=0; i<numberOfObjects; i++)
         world[order[i]].draw(g,centerX,centerY);
   }

   public void fill(Graphics g,int centerX,int centerY)
   {
      //for (int i=0; i<numberOfObjects; i++)
         //world[order[i]].fill(g,centerX,centerY);
   }

   public void transformAndFill(MyMatrix m,Graphics g,int centerX,int centerY)
   {
      for (int i=0; i<numberOfObjects; i++)
      {
         world[order[i]].fill(g,centerX,centerY);
         world[order[i]].transform(m);
         zValues[i]=-world[i].getAverageZ();
         order[i]=i;
      }
      quickSort(0,numberOfObjects-1);
   }
}
