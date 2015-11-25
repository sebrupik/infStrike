package infStrike.eight35Eng;

public class engVector {
    private double vector[]=new double[4];

   public engVector() {
      vector[0]=0.0;
      vector[1]=0.0;
      vector[2]=0.0;
      vector[3]=0.0;
   }

   public engVector(double[] values) {
      vector[0]=values[0];
      vector[1]=values[1];
      vector[2]=values[2];
      vector[3]=values[3];
   }

   public engVector(double x,double y) {
      vector[0]=x;
      vector[1]=y;
      vector[2]=0.0;
      vector[3]=0.0;
   }

   public engVector(double x,double y,double z) {
      vector[0]=x;
      vector[1]=y;
      vector[2]=z;
      vector[3]=0.0;
   }

   public engVector(double x,double y,double z,double w) {
      vector[0]=x;
      vector[1]=y;
      vector[2]=z;
      vector[3]=w;
   }

   public engVector transform(engMatrix m) {
      double m1[][]=m.getMatrix();
      double m2[]=new double[4];

      m2[0]=((vector[0]*m1[0][0])+(vector[1]*m1[0][1])+(vector[2]*m1[0][2])+(vector[3]*m1[0][3]));
      m2[1]=((vector[0]*m1[1][0])+(vector[1]*m1[1][1])+(vector[2]*m1[1][2])+(vector[3]*m1[1][3]));
      m2[2]=((vector[0]*m1[2][0])+(vector[1]*m1[2][1])+(vector[2]*m1[2][2])+(vector[3]*m1[2][3]));
      m2[3]=((vector[0]*m1[3][0])+(vector[1]*m1[3][1])+(vector[2]*m1[3][2])+(vector[3]*m1[3][3]));

      return new engVector(m2);
   }

   public double dotProduct(engVector v) {
      double v2[]=v.getVector();
      return (vector[0]*v2[0])+(vector[1]*v2[1])+(vector[2]*v2[2])+(vector[3]*v2[3]);
   }

   public engVector crossProduct(engVector v) {
      return new engVector();
   }

   public double[] getVector() {
      return vector;
   }

   public double getValueAt(int i) {
      return vector[i];
   }

   public void setValueAt(int i,double val) {
      vector[i]=val;
   }

   public String toString() {
      return "|\t"+vector[0]+"\t"+vector[1]+"\t"+vector[2]+"\t"+vector[3]+"\t"+"|\n\n";
   }
}