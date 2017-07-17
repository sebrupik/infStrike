package infStrike.gui;

import infStrike.objects.infBasic;
import infStrike.objects.worldObject;
import infStrike.eight35Eng.Eight35Eng;
import infStrike.eight35Eng.engMatrix;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

public class frameWireframeRenderer extends JFrame {

    public frameWireframeRenderer(worldObject arg1) {
        super("Infantry Strike wireframe window V0.1");
        setSize(400, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE) ;
        //getContentPane().add(new JScrollPane(new wireCanvas(arg1))) ;
        getContentPane().add(new wireCanvas(arg1)) ;
    }
}
//*******************************
 
class wireCanvas extends JPanel implements ActionListener {
    Graphics2D g2D;
    worldObject world;
    Eight35Eng tmpEng;

    Timer drawTimer = new Timer(100, this);


    private engMatrix rotationX   = new engMatrix();
    private engMatrix rotationY   = new engMatrix();
    private engMatrix rotationZ   = new engMatrix();
    private engMatrix translation = new engMatrix();
    private engMatrix scaling     = new engMatrix();
    private engMatrix projection  = new engMatrix();

    private engMatrix rotationMatrix = new engMatrix();
    private engMatrix worldMatrix    = new engMatrix();

    static float degreeRads = (float)(Math.PI/180);


    public wireCanvas(worldObject arg1) {
        world = arg1;
     
        tmpEng = new Eight35Eng();

        //***********
        rotationX.produceXRotationMatrix(0);
        rotationY.produceYRotationMatrix(0);
        rotationZ.produceZRotationMatrix(0);
        translation.produceTranslationMatrix(0,0,0);
        scaling.produceScalingMatrix(1,1,1);
        projection.produceProjectionMatrix(512);
        worldMatrix = projection.multiply(translation.multiply(scaling));
        //***********

        createWorld();
        
        start();
    }
    private void createWorld() {
        System.out.println("About to start engine");
        tmpEng.loadMapArray(world.getArenaPlan().getTopoObj().getTopoValues(), 
                            world.getArenaPlan().getTopoObj().getGridsize());
        //***********
        //world.transform(rotationMatrix.multiply(worldMatrix));
        //***********
    }

    // was : synchronized public void paint(Graphics g) {frameWireframeRenderer   <---- huh?!
    @Override synchronized public void paint(Graphics g) {
        super.paintComponent(g);  
        Dimension size = getSize();
        setBackground(Color.white);
        tmpEng.draw(g, (size.width/2), (size.height/2));
    }

    @Override public void actionPerformed(ActionEvent event) {
        repaint();
    }

    public void stop() {
        drawTimer.stop();
    }

    public void start() {
        drawTimer.start();
    }

    public boolean isRunning() {
        return drawTimer.isRunning();
    }

    public void update(Graphics g) {
        paint(g);
    }
}