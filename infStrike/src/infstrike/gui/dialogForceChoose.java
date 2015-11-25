package infStrike.gui;

import infStrike.objects.nationDatabase2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.Vector;

public class dialogForceChoose extends JDialog {
    JComboBox[] combos;
    String[] choices;
    JTextField[] numUnitsTxt;
    int[] numUnitsChoices;
    JButton createBut;
    Vector unitVec;
    boolean tmpBoo;

    nationDatabase2 natDatabase;

    /**
    * Arg1 : number of opposing sides
    * Arg2 : Vector that all units are to be added to
    * Arg3 : True if user is making own units. 
    *        False if program to generate its own units. User must specify how many units it wants.
    *
    */
    public dialogForceChoose(int arg1, Vector arg2, boolean arg3, nationDatabase2 arg4) {
        combos = new JComboBox[arg1];
        choices = new String[arg1];
        unitVec = arg2;
        numUnitsTxt = new JTextField[arg1];
        numUnitsChoices = new int[arg1];
        tmpBoo = arg3;
        natDatabase = arg4;
        createBut = new JButton("Create");

        JPanel panel = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();  
        panel.setLayout(gridbag); 


        for (int i=0; i<arg1; i++) {
            combos[i] = new JComboBox(natDatabase.getAllNations());

            buildConstraints(constraints, 0, i, 1, 1, 100, 100);
            constraints.fill = GridBagConstraints.NONE;                 
            gridbag.setConstraints(combos[i], constraints);
            panel.add(combos[i]);
            
            if (tmpBoo==false) {
                numUnitsTxt[i] = new JTextField("64");

                buildConstraints(constraints, 1, i, 1, 1, 100, 100);
                constraints.fill = GridBagConstraints.NONE;                 
                gridbag.setConstraints(numUnitsTxt[i], constraints);
                panel.add(numUnitsTxt[i]);
            }
        }
        
        buildConstraints(constraints, 0, arg1, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(createBut, constraints);
        panel.add(createBut);
        

        createBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                for (int i=0; i<choices.length; i++) {
                    choices[i] = (String)combos[i].getItemAt(combos[i].getSelectedIndex());
                    if(tmpBoo==false)
                        numUnitsChoices[i] = Integer.parseInt(numUnitsTxt[i].getText().trim());
                }
                if(tmpBoo==true) {
                    if(checkNationsWeapons()) {
                        dialogForceBuild genFrame = new dialogForceBuild(choices, unitVec, natDatabase);
                    }
                }
                else {
                    //some auto-gen method
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

    /**
    * Given a nations name, checks its nationFile held in nationDatabase
    * to see if there are any weapons loaded.
    */
    private boolean checkNationsWeapons() {
        String err1 = "The nation ";
        String err2 = " has no weapons available for use.";
        String info = "Warning";
        for(int i=0; i<choices.length; i++) {
            System.out.println("checking "+choices[i]+" / "+natDatabase.getNationFile(choices[i]).weaponsAvailable());
            if(!natDatabase.getNationFile(choices[i]).weaponsAvailable()) {
                JOptionPane.showMessageDialog(
                        createBut,
                        err1+natDatabase.getNationFile(choices[i]).getNation()+err2,
                        info,
                        JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
        }
        return true;
    }

    void buildConstraints(GridBagConstraints gbc, int gx, int gy, int gw, int gh, int wx, int wy) {
        gbc.gridx = gx;
        gbc.gridy = gy;
        gbc.gridwidth = gw;
        gbc.gridheight = gh;
        gbc.weightx = wx;
        gbc.weighty = wy;
    } 
}