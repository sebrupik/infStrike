package infStrike.gui;

import infStrike.objects.infBasic;
import infStrike.objects.basicUnitInfo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.Vector;
import java.util.Hashtable;

public class dialogUnitSaver extends JDialog {
    private JLabel fileLbl;
    private JTextField fileTxt;
    private JButton createBut;
    private Vector unitVec;

    public dialogUnitSaver(Vector arg1) {
        unitVec = arg1;
        JPanel panel = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();  
        panel.setLayout(gridbag); 

        

        fileLbl = new JLabel("File Name :");
        fileTxt = new JTextField("ENTER FILE NAME.unit");
        createBut = new JButton("Create File");
        countNumbers();

        //**************************
        buildConstraints(constraints, 0, 0, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(fileLbl, constraints);
        panel.add(fileLbl);
 
        buildConstraints(constraints, 1, 0, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(fileTxt, constraints);
        panel.add(fileTxt);

        buildConstraints(constraints, 0, 1, 2, 1, 100, 100);
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

    private void countNumbers() {
        String tStr;    
        int num = 1; 

        for (int i=0; i<unitVec.size(); i++) {
            if ( ((basicUnitInfo)unitVec.elementAt(i)).getSide() >= num)
                num++;
        }
        int[] all = new int[num];
        java.util.Arrays.fill(all, 0);

        for (int i=0; i<unitVec.size(); i++) {
            num = ((basicUnitInfo)unitVec.elementAt(i)).getSide();
            all[num] = all[num]+1;
        }


        String s = all.length+"Sides_";
        for (int i=0; i<all.length; i++) {
            s += "_"+all[i];
        }        
        fileTxt.setText(s+".unit");
    }


    private void xmlGen() {
        basicUnitInfo tmpInf;
        String[] tmpStr;

        try {         
            FileWriter stuff = new FileWriter(fileTxt.getText().trim());
        
            stuff.write("<?xml version='1.0'?>\n");
            stuff.write("<!-- Infantry Strike Unit Information File -->\n");
            stuff.write("<UnitInfo>\n");
            stuff.write("    <Schema>\n");
            stuff.write("        <AttrName Name=\"Side\" Type=\"String\"/>\n");
            stuff.write("        <AttrName Name=\"Nation\" Type=\"String\"/>\n");
            stuff.write("        <AttrName Name=\"Name\" Type=\"String\"/>\n");
            stuff.write("        <AttrName Name=\"Rank\" Type=\"Double\"/>\n");
            stuff.write("        <AttrName Name=\"Cammo\" Type=\"Double\"/>\n");
            stuff.write("        <AttrName Name=\"Armour\" Type=\"Double\"/>\n");
            stuff.write("        <AttrName Name=\"weapPri\" Type=\"String\"/>\n");
            stuff.write("        <AttrName Name=\"weapSec\" Type=\"String\"/>\n");             
            stuff.write("        <AttrName Name=\"Ammo1\" Type=\"String\"/>\n");
            stuff.write("        <AttrName Name=\"Ammo2\" Type=\"String\"/>\n");
            stuff.write("        <AttrName Name=\"Ammo3\" Type=\"String\"/>\n");
            stuff.write("        <AttrName Name=\"Ammo4\" Type=\"String\"/>\n");
            stuff.write("        <AttrName Name=\"Ammo5\" Type=\"String\"/>\n");
            stuff.write("        <AttrName Name=\"Ammo6\" Type=\"String\"/>\n");
            stuff.write("        <AttrName Name=\"Ammo7\" Type=\"String\"/>\n");
            stuff.write("        <AttrName Name=\"Ammo8\" Type=\"String\"/>\n");            
            stuff.write("    </Schema>\n");

            
            for (int i=0; i < unitVec.size(); i++) { 
                tmpInf = (basicUnitInfo)unitVec.elementAt(i);
                tmpStr = tmpInf.getMagType();
                stuff.write("    <Unit>\n");
                stuff.write("        <Attribute Name=\"Side\">\n");
                stuff.write("        "+tmpInf.getSide()+"\n");
                stuff.write("        </Attribute>\n");

                stuff.write("        <Attribute Name=\"Nation\">\n");
                stuff.write(tmpInf.getNation()+"\n");
                stuff.write("        </Attribute>\n");

                stuff.write("        <Attribute Name=\"Name\">\n");
                stuff.write(tmpInf.getName()+"\n");
                stuff.write("        </Attribute>\n");

                stuff.write("        <Attribute Name=\"Rank\">\n");
                stuff.write(tmpInf.getRank()+"\n");
                stuff.write("        </Attribute>\n");

                stuff.write("        <Attribute Name=\"Cammo\">\n");
                stuff.write(tmpInf.getCammo()+"\n");
                stuff.write("        </Attribute>\n");

                stuff.write("        <Attribute Name=\"Armour\">\n");
                stuff.write(tmpInf.getArmour()+"\n");
                stuff.write("        </Attribute>\n");

                stuff.write("        <Attribute Name=\"weapPri\">\n");
                stuff.write(tmpInf.getWeaponPri()+"\n");
                stuff.write("        </Attribute>\n");

                stuff.write("        <Attribute Name=\"weapSec\">\n");
                stuff.write(tmpInf.getWeaponSec()+"\n");
                stuff.write("        </Attribute>\n");

                stuff.write("        <Attribute Name=\"Ammo1\">\n");
                stuff.write(tmpStr[0]+"\n");
                stuff.write("        </Attribute>\n");

                stuff.write("        <Attribute Name=\"Ammo2\">\n");
                stuff.write(tmpStr[1]+"\n");
                stuff.write("        </Attribute>\n");

                stuff.write("        <Attribute Name=\"Ammo3\">\n");
                stuff.write(tmpStr[2]+"\n");
                stuff.write("        </Attribute>\n");

                stuff.write("        <Attribute Name=\"Ammo4\">\n");
                stuff.write(tmpStr[3]+"\n");
                stuff.write("        </Attribute>\n"); 

                stuff.write("        <Attribute Name=\"Ammo5\">\n");
                stuff.write(tmpStr[4]+"\n");
                stuff.write("        </Attribute>\n");

                stuff.write("        <Attribute Name=\"Ammo6\">\n");
                stuff.write(tmpStr[5]+"\n");
                stuff.write("        </Attribute>\n");

                stuff.write("        <Attribute Name=\"Ammo7\">\n");
                stuff.write(tmpStr[6]+"\n");
                stuff.write("        </Attribute>\n");

                stuff.write("        <Attribute Name=\"Ammo8\">\n");
                stuff.write(tmpStr[7]+"\n");
                stuff.write("        </Attribute>\n"); 
     
                stuff.write("    </Unit>\n");
            }

            stuff.write("</UnitInfo>");
            stuff.close();
        }
        catch (IOException e) {
            System.out.println("Error -- "+ e.toString());
        }
    }
}