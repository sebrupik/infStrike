package infStrike.gui;

import infStrike.objects.featLake;
import infStrike.objects.featBuilding;
import infStrike.objects.featBush;
import infStrike.objects.featForest;
import infStrike.objects.featObj;
import infStrike.objects.arenaPlan;
import infStrike.utils.Vec2;

import java.awt.*;
import java.awt.event.*;
import java.awt.Polygon;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.Graphics;
import java.util.Vector;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;

public class frameArenaEditor extends JFrame {
    editorCanvas eCanvas;

    //menubar buttons
    JMenuBar mb;
    JMenu fileMenu;
    JMenu editMenu;
    JMenu viewMenu;
    JMenu helpMenu;
  
    JMenuItem saveI;
    JMenuItem exitI;

    JMenuItem resetMapScaleI;
    JMenuItem resetMapTranslationI;
    JMenuItem resizeMapI;
    JMenuItem changeMapGridsizeI;

    JCheckBox topoViewI;
    JCheckBox gridLinesI;
    JCheckBox showNamesI;

    JMenuItem aboutI;

    //toolbar buttons
    private JToolBar tb = new JToolBar();
    JRadioButton topoBut;
    JRadioButton forestBut;
    JRadioButton lakeBut;
    JRadioButton buildBut;
    JRadioButton editBut;
    JButton addBut;
    JTextField nameBox;

    int lakes, buildings, forests;

    //dialog stuff
    private String widthDia = "Enter new width";
    private String heightDia = "Enter new height";
    private String gridsizeDia = "Enter new gridsize";
    private String numFal = " is not a number";
    private String info = "Information";

    public frameArenaEditor(int arg1, int arg2, int arg3, arenaPlan arg4) {
        super("Infantry Strike Arena Editor V1.4c");
        setSize(500, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE) ;

        mb = new JMenuBar();
        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit");
        viewMenu = new JMenu("View");
        helpMenu = new JMenu("Help");

        saveI = new JMenuItem("Save As...");
        exitI = new JMenuItem("Exit");

        resetMapScaleI = new JMenuItem("Reset Map Scale");
        resetMapTranslationI = new JMenuItem("Reset Map Translation");
        resizeMapI = new JMenuItem("Resize the Map");
        changeMapGridsizeI = new JMenuItem("Change map gridsize");

        topoViewI = new JCheckBox("View Topography");
        gridLinesI = new JCheckBox("GridLines");
        showNamesI = new JCheckBox("Show Names");

        aboutI = new JMenuItem("About..");

        fileMenu.add(saveI);
        fileMenu.addSeparator();
        fileMenu.add(exitI);

        editMenu.add(resetMapScaleI);
        editMenu.add(resetMapTranslationI);
        editMenu.add(resizeMapI);
        editMenu.add(changeMapGridsizeI);

        viewMenu.add(topoViewI);
        viewMenu.add(gridLinesI);
        viewMenu.add(showNamesI);

        helpMenu.add(aboutI);
        
        mb.add(fileMenu);
        mb.add(editMenu);
        mb.add(viewMenu);
        mb.add(helpMenu);

        setJMenuBar(mb);  

        tb = new JToolBar();
        topoBut = new JRadioButton("Topology");
        forestBut = new JRadioButton("Forest");
        lakeBut = new JRadioButton("Lake");
        buildBut = new JRadioButton("Buildings");
        editBut = new JRadioButton("Edit Mode");
        addBut = new JButton("Add");
        nameBox = new JTextField("");

        tb.add(topoBut);
        tb.add(forestBut);
        tb.add(lakeBut);
        tb.add(buildBut);
        tb.add(editBut);
        tb.add(addBut);
        tb.addSeparator();
        tb.add(nameBox);
        tb.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
        tb.setFloatable(false); 
        topoBut.setSelected(true);
        
        getContentPane().add(tb, "North");

        if (arg4 == null) 
            eCanvas = new editorCanvas(arg1, arg2, arg3, null, null);
        else 
            eCanvas = new editorCanvas(arg4.getWidth(), arg4.getHeight(), arg4.getTopoObj().getGridsize(), arg4.getFeatVec(), arg4.getTopoObj().getTopoValues());
        
        getContentPane().add(eCanvas);

        topoBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setRadioButtons(true, false, false, false, false);
                eCanvas.setAllBooleans(true, false, false, false, false);
                eCanvas.resetTmpPoly();
            }
        });
        forestBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setRadioButtons(false, true, false, false, false);
                eCanvas.setAllBooleans(false, true, false, false, false);
                eCanvas.resetTmpPoly();
                setNameText();
            }
        });
        lakeBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setRadioButtons(false, false, true, false, false);
                eCanvas.setAllBooleans(false, false, true, false, false);
                eCanvas.resetTmpPoly();
                setNameText();
            }
        });
        buildBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setRadioButtons(false, false, false, true, false);
                eCanvas.setAllBooleans(false, false, false, true, false);
                eCanvas.resetTmpPoly();
                setNameText();
            }
        });
        editBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setRadioButtons(false, false, false, false, true);
                eCanvas.setAllBooleans(false, false, false, false, true);
                eCanvas.resetTmpPoly();
                setNameText();
            }
        });
        addBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eCanvas.addObj(nameBox.getText().trim());

                if(lakeBut.isSelected()) 
                    lakes++;
                if(forestBut.isSelected())
                    forests++;
                if(buildBut.isSelected()) 
                    buildings++;
         
                setNameText();
            }
        });

        saveI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eCanvas.writeToFile();
            }
        });
        topoViewI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eCanvas.setTopoView(topoViewI.isSelected());
            }
        });
        gridLinesI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eCanvas.setGridLines(gridLinesI.isSelected());
            }
        });
        showNamesI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eCanvas.setShowNames(showNamesI.isSelected());
            }
        });
        resetMapScaleI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eCanvas.resetMapScale();
            }
        });
        resetMapTranslationI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eCanvas.resetMapTranslation();
            }
        });
        resizeMapI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String s1 = JOptionPane.showInputDialog(widthDia);
                try {
                    if (s1 != null)
                        System.out.println(Integer.parseInt(s1));
                }
                catch (NumberFormatException exp) {
                    JOptionPane.showMessageDialog(
                        resizeMapI,
                        exp+numFal,
                        info,
                        JOptionPane.INFORMATION_MESSAGE);
                }

                String s2 = JOptionPane.showInputDialog(heightDia);
                try {
                    if (s2 != null)
                        System.out.println(Integer.parseInt(s2));
                }
                catch (NumberFormatException exp) {
                    JOptionPane.showMessageDialog(
                        resizeMapI,
                        exp+numFal,
                        info,
                        JOptionPane.INFORMATION_MESSAGE);
                }
                if (s1 != null & s2 != null)
                    eCanvas.resizeMap(Integer.parseInt(s1), Integer.parseInt(s2));
            }
        });
        changeMapGridsizeI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("I'd like to change the map gridsize");
                String s1 = JOptionPane.showInputDialog(gridsizeDia);
                try {
                    if (s1 != null)
                        System.out.println(Integer.parseInt(s1));
                }
                catch (NumberFormatException exp) {
                    JOptionPane.showMessageDialog(
                        changeMapGridsizeI,
                        exp+numFal,
                        info,
                        JOptionPane.INFORMATION_MESSAGE);
                }

                if (s1 != null)
                    eCanvas.changeMapGridsize(Integer.parseInt(s1), false);
            }
        });
        aboutI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("tell me about the map editor");
            }
        });

        
    }

    private void setNameText() {
        if(lakeBut.isSelected()) 
            nameBox.setText("Lake"+lakes);
        if(forestBut.isSelected()) 
            nameBox.setText("Forest"+forests);
        if(buildBut.isSelected())
            nameBox.setText("Building"+buildings);
    }

    private void setRadioButtons(boolean arg1, boolean arg2, boolean arg3, boolean arg4, boolean arg5) {
        topoBut.setSelected(arg1);
        forestBut.setSelected(arg2);
        lakeBut.setSelected(arg3);
        buildBut.setSelected(arg4);
        editBut.setSelected(arg5);
    }
}
//**********************************************

class editorCanvas extends JPanel {
    // inital vars
    private int width;
    private int height;
    private int gridsize;
    private int maxTopoHeight;
    private double topoPercent;
    private Vector objVec = new Vector(); 
    private int[][] topoGrid;
    private int topoX, topoY;

    private boolean topoBoo = true;
    private boolean forestBoo;
    private boolean lakeBoo;
    private boolean buildBoo;
    private boolean editModeBoo;
 
    //tmp polygons
    private featObj tmpFeatObj;
    private Polygon tmpPoly;
    private featForest tmpForest;
    private featBuilding tmpBuilding;
    private featLake tmpLake;

    //mouse motion stuff
    int mCorX, mCorY;

    //paint stuff
    int tmpX, tmpY;
    private boolean topoViewBoo;
    private boolean gridLinesBoo;
    private boolean showNamesBoo;

    //edit Mode variables
    private Point2D.Double tmpPoint;
    private int vecPosition = -1;
    private int[] topLeft;
    private int[] bottomRight;

    //canvas scaling
    int xres;
    int yres;
    Vec2 translation = new Vec2(0.0,0.0);
    double scale = 1.0;
    Graphics2D g2D;

    //Dialog info
    private String topoStr = "Enter the height in meters :";
    private String numFal = " is not a number";
    private String info = "Information";
    private String intersects = "This polygon intersects ";
    private String nameExists1 = "The name ";
    private String nameExists2 = " already exists.";

    public editorCanvas(int arg1, int arg2, int arg3, Vector arg4, int[][] arg5) {
        width = arg1;
        height = arg2;
        gridsize = arg3;

        if (arg4 != null)
            objVec = arg4;

        if (arg5 != null)
            topoGrid = arg5;
        else
            topoGrid = new int[width/gridsize][height/gridsize];

        setPreferredSize(new Dimension(width, height)) ;
        MouseManager mm = new MouseManager();
        addMouseListener(mm);
        addMouseMotionListener(mm);
        
        tmpPoly = new Polygon();

        calcMaxTopoHeight();
    }

    public void paint(Graphics g) {
        super.paintComponent(g);  
        tmpX = 0;
        tmpY = 0;

        g2D = (Graphics2D)g;
        g2D.clearRect(0,0,getWidth(), getHeight());
        g2D.scale(scale, scale);
        g2D.translate(translation.x(), translation.y());
        
        if (topoViewBoo) {
            for(int i = 0; i< (height/gridsize); i++) {   // the height of the array grid
                for (int j = 0; j < (width/gridsize); j++) {
                    g2D.setColor(new Color(255, (int)(255-(topoPercent*topoGrid[j][i])), 255));
                    tmpX = gridsize*j;
                    g2D.fillRect(tmpX, tmpY, gridsize, gridsize);
                    g2D.setColor(Color.black);
                    if (scale > 0.45)
                        g2D.drawString(Integer.toString(topoGrid[j][i]), tmpX+(gridsize/2), tmpY+(gridsize/2));
                } 
                tmpX = 0;
                tmpY += gridsize;   
            }
        }

        if(gridLinesBoo) {
            for(int i = 0; i <= width; i += 100) {
                if ((i % gridsize) == 0) {
                    g2D.setColor(Color.red);
                    g2D.drawLine(i, 0, i, height);
                }
                if (((i % gridsize) != 0) & scale > 0.1) {
                    g2D.setColor(Color.black);
                    g2D.drawLine(i, 0, i, height);
                }

                if (scale > 0.45)
                    g2D.drawString(Integer.toString(i), 10, i+10);
            }

            for(int i = 0; i <= height; i += 100) {
                if ((i % gridsize) == 0) {
                    g2D.setColor(Color.red);
                    g2D.drawLine(0, i, width, i);
                }
                if (((i % gridsize) != 0) & scale > 0.1) {
                    g2D.setColor(Color.black);
                    g2D.drawLine(0, i, width, i);
                }
                if (scale > 0.45)
                    g2D.drawString(Integer.toString(i), i+10, 10);
            }
        }

        if(tmpPoly.npoints > 0) 
            g.drawPolygon(tmpPoly);

        for(int i=0; i<objVec.size(); i++) {
            tmpFeatObj = (featObj)objVec.elementAt(i);
            tmpFeatObj.updateGraphics(0.0, g2D);
            if (showNamesBoo) {
                g2D.setColor(Color.black);
                g2D.drawString(tmpFeatObj.getName(), tmpFeatObj.getX()[0], tmpFeatObj.getY()[0]);
            }
        }
        if (vecPosition != -1) {
            g2D.setColor(Color.black);
            tmpFeatObj = (featObj)objVec.elementAt(vecPosition);
            g2D.drawPolygon(findBounds(tmpFeatObj));
        }
    }

//************ boolean set methods***********************************
    public void setTopoBoo(boolean arg1) {
        topoBoo = arg1;
        repaint();
    }
    public void setForestBoo(boolean arg1) {
        forestBoo = arg1;
        repaint();
    }
    public void setLakeBoo(boolean arg1) {
        lakeBoo = arg1;
        repaint();
    }
    public void setBuildBoo(boolean arg1) {
        buildBoo = arg1;
        repaint();
    }

    public void setAllBooleans(boolean arg1, boolean arg2, boolean arg3, boolean arg4, boolean arg5) {
        topoBoo = arg1;
        forestBoo = arg2;
        lakeBoo = arg3;
        buildBoo = arg4;
        editModeBoo = arg5;
        repaint();
    }

//************************* utility methods **************************

    public void resetTmpPoly() {
        tmpPoly = new Polygon();
    }

    private boolean polyInside(Polygon arg1) {
        Rectangle2D tmpRect = arg1.getBounds2D();
        for(int i=0; i<objVec.size(); i++) {

            tmpFeatObj = (featObj)objVec.elementAt(i);
            if(tmpFeatObj.containsPoly(tmpRect)) {
                JOptionPane.showMessageDialog(
                        editorCanvas.this,
                        intersects+tmpFeatObj.getName(),
                        info,
                        JOptionPane.INFORMATION_MESSAGE);

                return true;
            }
        }
        return false;
    }

    private boolean checkNameExists(String arg1) {
        for(int i=0; i<objVec.size(); i++) {
            tmpFeatObj = (featObj)objVec.elementAt(i);

            if(arg1.equals(tmpFeatObj.getName())) {
                JOptionPane.showMessageDialog(
                        editorCanvas.this,
                        nameExists1+tmpFeatObj.getName()+nameExists2,
                        info,
                        JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
        }
        return false;
    }

    private void setTopoHeight(int arg1, int arg2, int arg3) {
        topoGrid[(int)(arg1/gridsize)][(int)(arg2/gridsize)] = arg3;
        calcMaxTopoHeight();
    }

    private void calcMaxTopoHeight() {
        maxTopoHeight = 0;
        for(int i = 0; i < (height/gridsize); i++) {   // check each time incase tile with highest value to changed to low value
            for (int j = 0; j < (width/gridsize); j++) {
                if (topoGrid[j][i] > maxTopoHeight)
                    maxTopoHeight = topoGrid[j][i];
            }
        } 
        topoPercent = 255.0/maxTopoHeight;
    }

    /**
    * Produces two points which are used to draw a box around the given polygon
    */
    private Polygon findBounds(featObj arg1) {
        topLeft = new int[] {arg1.getX()[0], arg1.getY()[0]};
        bottomRight = new int[] {arg1.getX()[0], arg1.getY()[0]};
        for(int i=0; i<arg1.npoints(); i++) {
            if(arg1.getX()[i] < topLeft[0])
                topLeft[0] = arg1.getX()[i];
            if(arg1.getY()[i] < topLeft[1])
                topLeft[1] = arg1.getY()[i];

            if(arg1.getX()[i] > bottomRight[0])
                bottomRight[0] = arg1.getX()[i];
            if(arg1.getY()[i] > bottomRight[1])
                bottomRight[1] = arg1.getY()[i];
        }
        topLeft[0] -= 10;         
        topLeft[1] -= 10;
        bottomRight[0] += 10;
        bottomRight[1] += 10;

        return new Polygon(new int[]{topLeft[0], bottomRight[0], bottomRight[0], topLeft[0]}, new int[]{topLeft[1], topLeft[1], bottomRight[1], bottomRight[1]}, 4);
    }

//********************* methods used by 'frameArenaEditor' menuItems ******************

    public void writeToFile() {
        dialogArenaSaver arSave = new dialogArenaSaver(width, height, objVec, topoGrid);
    }
    public void setTopoView(boolean arg1) {
        topoViewBoo = arg1;
        repaint();
    }
    public void setGridLines(boolean arg1) {
        gridLinesBoo = arg1;
        repaint();
    }
    public void setShowNames(boolean arg1) {
        showNamesBoo = arg1;
        repaint();
    }
    public void resetMapScale() {
        scale = 1.0;
        repaint();
    }
    public void resetMapTranslation() {
        translation.x(0.0);
        translation.y(0.0);
        repaint();
    }
    public void addObj(String arg1) {
        if (!checkNameExists(arg1) & !arg1.equals("") & !polyInside(tmpPoly) & (tmpPoly.npoints > 0)) {
            if(forestBoo) {
                objVec.addElement(new featForest(tmpPoly.npoints, tmpPoly.xpoints, tmpPoly.ypoints, arg1));
                resetTmpPoly();
            }
            if(lakeBoo) {
                objVec.addElement(new featLake(tmpPoly.npoints, tmpPoly.xpoints, tmpPoly.ypoints, arg1));
                resetTmpPoly();
            }
            if(buildBoo) {
                objVec.addElement(new featBuilding(tmpPoly.npoints, tmpPoly.xpoints, tmpPoly.ypoints, arg1, 2));
                resetTmpPoly();
            }
            repaint();
        }
    }
    public void resizeMap(int arg1, int arg2) {
        int tmpTopoGrid[][] = new int[arg1/gridsize][arg2/gridsize];
        int newWidth = arg1;
        int newHeight = arg2;

        if(width < newWidth)   //find the smallest
            newWidth = width;
        if(height < newHeight)
            newHeight = height;

        for(int i = 0; i< (newHeight/gridsize); i++) {   // the height of the array grid
            for (int j = 0; j < (newWidth/gridsize); j++) {
                tmpTopoGrid[j][i] = topoGrid[j][i];
            }
        }
        topoGrid = tmpTopoGrid;
        width = arg1;
        height = arg2;
        repaint();
    }

    /**
    * arg1 - new gridsize  arg2 - fill in the new gridholes?
    */
    public void changeMapGridsize(int arg1, boolean arg2) {
        int oldGridsize = gridsize;
        gridsize = arg1;
        int tmpTopoGrid[][] = new int[width/gridsize][height/gridsize];
 
        for(int i = 0; i< (height/gridsize); i++) {   // the height of the array grid
            for (int j = 0; j < (width/gridsize); j++) {
                //if ( ((i*gridsize) % (oldGridsize)) == 0)
                    tmpTopoGrid[j][i] = topoGrid[j*(oldGridsize/gridsize)][i*(oldGridsize/gridsize)];
            }
        }
        repaint();
    }
//******************************
    class MouseManager extends MouseAdapter implements MouseMotionListener {
        double lastx = 0.0;
        double lasty = 0.0;
        
        public void mouseClicked(MouseEvent e1) {
            if(topoBoo & (e1.getModifiers() & e1.BUTTON1_MASK)!=0) {
                topoX = (int) ( (e1.getX()*(1/scale))-(int)translation.x() );
                topoY = (int) ( (e1.getY()*(1/scale))-(int)translation.y() );

                String s = JOptionPane.showInputDialog(topoStr);
                try {
                    if (s != null)
                        setTopoHeight(topoX, topoY, Integer.parseInt(s));
                }
                catch (NumberFormatException exp) {
                    JOptionPane.showMessageDialog(
                        editorCanvas.this,
                        exp+numFal,
                        info,
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
            if ((forestBoo | lakeBoo | buildBoo) & (e1.getModifiers() & e1.BUTTON1_MASK)!=0) {
                tmpPoly.addPoint( (int) ( (e1.getX()*(1/scale))-(int)translation.x() ), (int) ( (e1.getY()*(1/scale))-(int)translation.y() ) );
            }
            
            if (editModeBoo & (e1.getModifiers() & e1.BUTTON3_MASK)!=0) {
                System.out.println("Engage special editing powers!");
                for(int i=0; i<objVec.size(); i++) {
                    tmpFeatObj = (featObj)objVec.elementAt(i);
                    tmpPoint = new Point2D.Double((e1.getX()*(1/scale))-(int)translation.x(), (e1.getY()*(1/scale))-(int)translation.y());
                    if(tmpFeatObj.contains(tmpPoint)) {
                        System.out.println("You are trying to fuck with : "+tmpFeatObj.getName());
                        if (vecPosition == -1)
                            vecPosition = i;
                        else 
                            vecPosition = -1;
                    }
                }
            }
            e1.consume();
            repaint();
        }
        public void mouseDragged(MouseEvent e) {
            double dy = e.getY() - lasty;
            
            if ((e.getModifiers() & e.BUTTON1_MASK)!=0) {       //move
            	double dx = e.getX() - lastx;
                translation.x(translation.x()+dx/scale);
                translation.y(translation.y()+dy/scale);
            }
            else if ((e.getModifiers() & e.BUTTON3_MASK)!=0) {  //scale
                if (dy >= 0) {                                  //make larger
                    scale *= (1.0+(double)dy/50.0);
                }
                else {                                          // make smaller
                    scale /= (1.0+(-dy/50.0));
                }
            }
            lastx = e.getX();
            lasty = e.getY();
            e.consume();
            repaint();
        }
        public void mousePressed(MouseEvent e) {
            lastx = e.getX();
            lasty = e.getY();
        }

        public void mouseMoved(MouseEvent e) {
            //do nothing
        }
    }
}