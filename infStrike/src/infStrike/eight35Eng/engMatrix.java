package infStrike.eight35Eng;

public class engMatrix {
    private double matrix[][]={{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};

    public engMatrix() {

    }

    public engMatrix(double values[][]) {
       matrix=values;
    }

    public void produceIdentityMatrix() {
       matrix[0][0]=1; matrix[0][1]=0; matrix[0][2]=0; matrix[0][3]=0;
       matrix[1][0]=0; matrix[1][1]=1; matrix[1][2]=0; matrix[1][3]=0;
       matrix[2][0]=0; matrix[2][1]=0; matrix[2][2]=1; matrix[2][3]=0;
       matrix[3][0]=0; matrix[3][1]=0; matrix[3][2]=0; matrix[3][3]=1;
    }

    public void produceProjectionMatrix(int distance) {
       matrix[3][2]=1.0/distance;
       matrix[3][3]=0;
    }

    public void produceTranslationMatrix(double x,double y,double z) {
       matrix[0][3]=x;
       matrix[1][3]=y;
       matrix[2][3]=z;
       matrix[3][3]=1;
    }
 
    public void produceRotationMatrix(double x,double y,double z) {
       engMatrix mx=new engMatrix(),
                my=new engMatrix(),
                mz=new engMatrix(),
                t;

       mx.produceXRotationMatrix(x);
       my.produceYRotationMatrix(y);
       mz.produceZRotationMatrix(z);

       t=mx.multiply(my.multiply(mz));
       matrix=t.getMatrix();
    }

    public void produceXRotationMatrix(double x) {
       matrix[1][1]= Math.cos(x);
       matrix[2][1]= Math.sin(x);
       matrix[1][2]=-Math.sin(x);
       matrix[2][2]= Math.cos(x);
    }

    public void produceYRotationMatrix(double y) {
       matrix[0][0]= Math.cos(y);
       matrix[2][0]=-Math.sin(y);
       matrix[0][2]= Math.sin(y);
       matrix[2][2]= Math.cos(y);
    }

    public void produceZRotationMatrix(double z) {
       matrix[0][0]= Math.cos(z);
       matrix[1][0]= Math.sin(z);
       matrix[0][1]=-Math.sin(z);
       matrix[1][1]= Math.cos(z);
    }

    public void produceScalingMatrix(double x,double y,double z) {
       matrix[0][0]=x;
       matrix[1][1]=y;
       matrix[2][2]=z;
       matrix[3][3]=1;
    }

    public engMatrix multiply(engMatrix m) {
       double m1[][]=m.getMatrix();
       double m2[][]={{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};

       m2[0][0]=((matrix[0][0]*m1[0][0])+(matrix[1][0]*m1[0][1])+(matrix[2][0]*m1[0][2])+(matrix[3][0]*m1[0][3]));
       m2[1][0]=((matrix[0][0]*m1[1][0])+(matrix[1][0]*m1[1][1])+(matrix[2][0]*m1[1][2])+(matrix[3][0]*m1[1][3]));
       m2[2][0]=((matrix[0][0]*m1[2][0])+(matrix[1][0]*m1[2][1])+(matrix[2][0]*m1[2][2])+(matrix[3][0]*m1[2][3]));
       m2[3][0]=((matrix[0][0]*m1[3][0])+(matrix[1][0]*m1[3][1])+(matrix[2][0]*m1[3][2])+(matrix[3][0]*m1[3][3]));
       m2[0][1]=((matrix[0][1]*m1[0][0])+(matrix[1][1]*m1[0][1])+(matrix[2][1]*m1[0][2])+(matrix[3][1]*m1[0][3]));
       m2[1][1]=((matrix[0][1]*m1[1][0])+(matrix[1][1]*m1[1][1])+(matrix[2][1]*m1[1][2])+(matrix[3][1]*m1[1][3]));
       m2[2][1]=((matrix[0][1]*m1[2][0])+(matrix[1][1]*m1[2][1])+(matrix[2][1]*m1[2][2])+(matrix[3][1]*m1[2][3]));
       m2[3][1]=((matrix[0][1]*m1[3][0])+(matrix[1][1]*m1[3][1])+(matrix[2][1]*m1[3][2])+(matrix[3][1]*m1[3][3]));
       m2[0][2]=((matrix[0][2]*m1[0][0])+(matrix[1][2]*m1[0][1])+(matrix[2][2]*m1[0][2])+(matrix[3][2]*m1[0][3]));
       m2[1][2]=((matrix[0][2]*m1[1][0])+(matrix[1][2]*m1[1][1])+(matrix[2][2]*m1[1][2])+(matrix[3][2]*m1[1][3]));
       m2[2][2]=((matrix[0][2]*m1[2][0])+(matrix[1][2]*m1[2][1])+(matrix[2][2]*m1[2][2])+(matrix[3][2]*m1[2][3]));
       m2[3][2]=((matrix[0][2]*m1[3][0])+(matrix[1][2]*m1[3][1])+(matrix[2][2]*m1[3][2])+(matrix[3][2]*m1[3][3]));
       m2[0][3]=((matrix[0][3]*m1[0][0])+(matrix[1][3]*m1[0][1])+(matrix[2][3]*m1[0][2])+(matrix[3][3]*m1[0][3]));
       m2[1][3]=((matrix[0][3]*m1[1][0])+(matrix[1][3]*m1[1][1])+(matrix[2][3]*m1[1][2])+(matrix[3][3]*m1[1][3]));
       m2[2][3]=((matrix[0][3]*m1[2][0])+(matrix[1][3]*m1[2][1])+(matrix[2][3]*m1[2][2])+(matrix[3][3]*m1[2][3]));
       m2[3][3]=((matrix[0][3]*m1[3][0])+(matrix[1][3]*m1[3][1])+(matrix[2][3]*m1[3][2])+(matrix[3][3]*m1[3][3]));

       return new engMatrix(m2);
    }

    public double[][] getMatrix() {
       return matrix;
    }

    public String toString() {
       return "|\t"+matrix[0][0]+"\t"+matrix[1][0]+"\t"+matrix[2][0]+"\t"+matrix[3][0]+"\t"+"|\n"+
              "|\t"+matrix[0][1]+"\t"+matrix[1][1]+"\t"+matrix[2][1]+"\t"+matrix[3][1]+"\t"+"|\n"+
              "|\t"+matrix[0][2]+"\t"+matrix[1][2]+"\t"+matrix[2][2]+"\t"+matrix[3][2]+"\t"+"|\n"+
              "|\t"+matrix[0][3]+"\t"+matrix[1][3]+"\t"+matrix[2][3]+"\t"+matrix[3][3]+"\t"+"|\n\n";
    }
}