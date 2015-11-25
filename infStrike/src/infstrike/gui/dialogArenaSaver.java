package infStrike.gui;

import infStrike.objects.featLake;
import infStrike.objects.featBuilding;
import infStrike.objects.featBush;
import infStrike.objects.featForest;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.Vector;

public class dialogArenaSaver extends JDialog {
    private JLabel mapLbl;
    private JTextField mapTxt;
    private JLabel fileLbl;
    private JTextField fileTxt;
    private JButton createBut;
    private int width, height;
    private Vector objVec;
    private int topoGrid[][];
    private int gridsize;
    

    public dialogArenaSaver(int arg1, int arg2, Vector arg3, int[][] arg4) {
        width = arg1;
        height  = arg2;
        objVec = arg3;
        topoGrid = arg4;
        gridsize = (width/topoGrid.length);

        JPanel panel = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();  
        panel.setLayout(gridbag); 

        mapLbl = new JLabel("Map Name :");
        mapTxt = new JTextField("<MAP NAME>");
        fileLbl = new JLabel("File Name :");
        fileTxt = new JTextField("<ENTER FILE NAME>.arena");
        createBut = new JButton("Create File");

        //**************************
        buildConstraints(constraints, 0, 0, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(mapLbl, constraints);
        panel.add(mapLbl);
 
        buildConstraints(constraints, 1, 0, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(mapTxt, constraints);
        panel.add(mapTxt);


        buildConstraints(constraints, 0, 1, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(fileLbl, constraints);
        panel.add(fileLbl);
 
        buildConstraints(constraints, 1, 1, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(fileTxt, constraints);
        panel.add(fileTxt);

        buildConstraints(constraints, 0, 2, 2, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(createBut, constraints);
        panel.add(createBut);

        //***************************

        createBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if(fileTxt.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(
                      createBut,
                      "No file Name",
                      "Warning",
                      JOptionPane.INFORMATION_MESSAGE);

                    Toolkit.getDefaultToolkit().beep();
                    return;
                }
                else {
                    xmlGen(); 
                }
                dispose();
            }
        });
        getContentPane().add(panel);
        setModal(true);
        setTitle("Unit Saver V1.0");
        setSize(260, 140);
        setResizable(false);
        pack();
        show();   
    }

    void buildConstraints(GridBagConstraints gbc, int gx, int gy, int gw, int gh, int wx, int wy) {
        gbc.gridx = gx;
        gbc.gridy = gy;
        gbc.gridwidth = gw;
        gbc.gridheight = gh;
        gbc.weightx = wx;
        gbc.weighty = wy;
    } 

    private void xmlGen() {
        featForest tmpForest;
        featBuilding tmpBuilding;
        featLake tmpLake;

        try {         
            FileWriter stuff = new FileWriter(fileTxt.getText().trim());
        
            stuff.write("<?xml version='1.0'?>\n");
            stuff.write("<!-- Infantry Strike Arena Information File -->\n");
            stuff.write("<ArenaInfo>\n");

            stuff.write("    <Info>\n");
            stuff.write("        <Attribute Name=\"Name\">\n");
            stuff.write("            "+mapTxt.getText().trim()+"\n");
            stuff.write("        </Attribute>\n");
            stuff.write("        <Attribute Name=\"Width\">\n");
            stuff.write("            "+width+"\n");
            stuff.write("        </Attribute>\n");
            stuff.write("        <Attribute Name=\"Height\">\n");
            stuff.write("            "+height+"\n");
            stuff.write("        </Attribute>\n");
            stuff.write("    </Info>\n");



            
            for (int i=0; i<objVec.size(); i++) { 
                if(objVec.elementAt(i) instanceof featForest) {
                    tmpForest = (featForest)objVec.elementAt(i);
                    stuff.write("    <Forest>\n");
                    stuff.write("        <Attribute Name=\"NumPoints\">\n");
                    stuff.write("            "+tmpForest.getNumPoints()+"\n");
                    stuff.write("        </Attribute>\n");

                    stuff.write("        <Attribute Name=\"XCors\">\n");
                    stuff.write("            "+tmpForest.getXPoints()+"\n");
                    stuff.write("        </Attribute>\n");

                    stuff.write("        <Attribute Name=\"YCors\">\n");
                    stuff.write("            "+tmpForest.getYPoints()+"\n");
                    stuff.write("        </Attribute>\n");

                    stuff.write("        <Attribute Name=\"Name\">\n");
                    stuff.write("            "+tmpForest.getName()+"\n");
                    stuff.write("        </Attribute>\n");
                    stuff.write("    </Forest>\n");
                }
                if(objVec.elementAt(i) instanceof featLake) {
                    tmpLake = (featLake)objVec.elementAt(i);
                    stuff.write("    <Lake>\n");
                    stuff.write("        <Attribute Name=\"NumPoints\">\n");
                    stuff.write("            "+tmpLake.getNumPoints()+"\n");
                    stuff.write("        </Attribute>\n");

                    stuff.write("        <Attribute Name=\"XCors\">\n");
                    stuff.write("            "+tmpLake.getXPoints()+"\n");
                    stuff.write("        </Attribute>\n");

                    stuff.write("        <Attribute Name=\"YCors\">\n");
                    stuff.write("            "+tmpLake.getYPoints()+"\n");
                    stuff.write("        </Attribute>\n");

                    stuff.write("        <Attribute Name=\"Name\">\n");
                    stuff.write("            "+tmpLake.getName()+"\n");
                    stuff.write("        </Attribute>\n");
                    stuff.write("    </Lake>\n");
                }
                if(objVec.elementAt(i) instanceof featBuilding) {
                    tmpBuilding = (featBuilding)objVec.elementAt(i);
                    stuff.write("    <Building>\n");
                    stuff.write("        <Attribute Name=\"NumPoints\">\n");
                    stuff.write("            "+tmpBuilding.getNumPoints()+"\n");
                    stuff.write("        </Attribute>\n");

                    stuff.write("        <Attribute Name=\"XCors\">\n");
                    stuff.write("            "+tmpBuilding.getXPoints()+"\n");
                    stuff.write("        </Attribute>\n");

                    stuff.write("        <Attribute Name=\"YCors\">\n");
                    stuff.write("            "+tmpBuilding.getYPoints()+"\n");
                    stuff.write("        </Attribute>\n");

                    stuff.write("        <Attribute Name=\"Name\">\n");
                    stuff.write("            "+tmpBuilding.getName()+"\n");
                    stuff.write("        </Attribute>\n");
      
                    stuff.write("        <Attribute Name=\"Floors\">\n");
                    stuff.write("            "+tmpBuilding.getNumFloors()+"\n");
                    stuff.write("        </Attribute>\n");

                    stuff.write("    </Building>\n");
                }
            }

            stuff.write("    <Topography>\n");
            stuff.write("        <Attribute Name=\"NumPoints\">\n");
            stuff.write("            "+(topoGrid.length*topoGrid[1].length)+"\n");
            stuff.write("        </Attribute>\n");

            stuff.write("        <Attribute Name=\"NumPointsX\">\n");
            stuff.write("            "+topoGrid.length+"\n");
            stuff.write("        </Attribute>\n");
            
            stuff.write("        <Attribute Name=\"NumPointsY\">\n");
            stuff.write("            "+topoGrid[1].length+"\n");
            stuff.write("        </Attribute>\n");

            stuff.write("        <Attribute Name=\"Grid\">\n");
            stuff.write("            "+(width/topoGrid.length)+"\n");
            stuff.write("        </Attribute>\n");

            stuff.write("        <Attribute Name=\"MaxHeight\">\n");
            stuff.write("            "+calcMaxHeight()+"\n");
            stuff.write("        </Attribute>\n");

            stuff.write("        <Attribute Name=\"GridValues\">\n");
            for(int i = 0; i< (height/gridsize); i++) {  
                stuff.write("           ");
                for (int j = 0; j < (width/gridsize); j++) {
                    //stuff.write(topoGrid[j][i]+", ");
                    stuff.write(""+topoGrid[j][i]);
                    //if(!((i+1) == (height/gridsize) & (j+1) == (width/gridsize)))
                    if(! (((i+1) == topoGrid[0].length) & ((j+1) == topoGrid.length)))
                        stuff.write(", ");
                }
                stuff.write("\n");
            } 
            stuff.write("        </Attribute>\n");   
            stuff.write("    </Topography>\n");

            stuff.write("</ArenaInfo>");
            stuff.close();
        }
        catch (IOException e) {
            System.out.println("Error -- "+ e.toString());
        }

        
    }
    private int calcMaxHeight() {
        int maxTopoHeight = 0;
        for(int i = 0; i< (height/gridsize); i++) {   
            for (int j = 0; j < (width/gridsize); j++) {
                if (topoGrid[j][i] > maxTopoHeight)
                    maxTopoHeight = topoGrid[j][i];
            }
        } 
        return maxTopoHeight;
    }
}