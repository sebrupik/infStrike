package infStrike.gui;

import infStrike.objects.basicUnitInfo;
import infStrike.objects.nationDatabase2;
import infStrike.objects.nationFile2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.ArrayList;

public class dialogForceBuild extends JDialog {
    private static JTabbedPane tp;
    private JLabel GUIPic;
    private ArrayList unitAl;

    public dialogForceBuild(String[] arg1, ArrayList arg2, nationDatabase2 arg3) {
        this.unitAl = arg2;
        tp = new JTabbedPane();
        GUIPic = new JLabel(new ImageIcon("game/infStrike/images/GUI.jpg"));
        Container contentPane = getContentPane();

        System.out.println("dialogForceBuild - adding nation panel(s)");        

        for(int i=0; i<arg1.length; i++) {
            System.out.println(arg3.getNationFile(arg1[i]).getNation());
            tp.add(new unitSetup(arg1[i], i, unitAl, arg3.getNationFile(arg1[i]), arg3.getPic("NATION", arg1[i])), "Side "+i);
            System.out.println("one has been added");
        }
        System.out.println("dialogForceBuild - adding weapInfo panel");        
        tp.add(new weaponInfo(arg3), "Weapons");
        System.out.println("dialogForceBuild - all panels added");                
        contentPane.add(tp, "Center");
        contentPane.add(GUIPic, "South");

        setModal(true);
        setTitle("Unit Creator -- V1.01a");
        setSize(390, 600);
        setResizable(false);
        pack();
        show();  
    }
}

//*************************

class unitSetup extends JPanel {
    private JLabel sideTypeImg;
    private JLabel editTypeLbl;   //combo box to edit created soliders
    private JLabel nameTypeLbl;
    private JLabel rankTypeLbl;
    private JLabel weapTypeLbl;
    private JLabel cammTypeLbl;
    private JLabel armrTypeLbl;
    private JLabel priWTypeLbl;
    private JLabel secWTypeLbl;

    private JLabel[] ammTypeLbl = new JLabel[8];

    private JComboBox editTypeBox;   
    private JTextField nameTypeTXT;
    private JComboBox rankTypeBox;
    private JComboBox weapTypeBox;
    private JComboBox cammTypeBox;
    private JComboBox armrTypeBox;
    private JComboBox priWTypeBox;
    private JComboBox secWTypeBox;

    private JComboBox[] ammTypeBox = new JComboBox[8];

    private JButton addUnit;
    private JTextField howManyTXT;

    private String nation;
    private int side;

    ArrayList unitAl;

    nationFile2 natFile;

    public unitSetup(String arg1, int arg2, ArrayList arg3, nationFile2 arg4, ImageIcon arg5) {  // nation, side, unitVec, specific weapFile
        this.nation = arg1;
        this.side = arg2;
        this.unitAl = arg3;
        this.natFile = arg4;

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();  
        setLayout(gridbag);

        editTypeLbl = new JLabel("Unit No");
        nameTypeLbl = new JLabel("ID");
        rankTypeLbl = new JLabel("Rank");
        weapTypeLbl = new JLabel("Weapon load out");
        cammTypeLbl = new JLabel("Cammo Type");
        armrTypeLbl = new JLabel("Armour");
        priWTypeLbl = new JLabel("Primary Weapon");
        secWTypeLbl = new JLabel("Secondary Weapon");

        for (int i=0; i<ammTypeLbl.length; i++) 
            ammTypeLbl[i] = new JLabel("Ammo "+(i+1)+" type");

        rankTypeBox = new JComboBox(natFile.getRanks());
        weapTypeBox = new JComboBox(natFile.getWeaponUser());
        cammTypeBox = new JComboBox(natFile.getCammo());
        armrTypeBox = new JComboBox(natFile.getArmour());
        editTypeBox = new JComboBox();

        addUnit = new JButton("Add"); 

        sideTypeImg = new JLabel(arg5);      
        nameTypeTXT = new JTextField(natFile.makeName(unitAl));
        priWTypeBox = new JComboBox(natFile.getWeapPri());
        secWTypeBox = new JComboBox(natFile.getWeapSec());
         
        howManyTXT = new JTextField("1");


        for (int i=0; i<ammTypeBox.length; i++)
            ammTypeBox[i] = new JComboBox(natFile.getMagPri());
        
        //***************************

        buildConstraints(constraints, 0, 0, 2, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(sideTypeImg, constraints);
        add(sideTypeImg);

        buildConstraints(constraints, 0, 1, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(editTypeLbl, constraints);
        add(editTypeLbl);
        buildConstraints(constraints, 1, 1, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(editTypeBox, constraints);
        add(editTypeBox);

        buildConstraints(constraints, 0, 2, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(nameTypeLbl, constraints);
        add(nameTypeLbl);
        buildConstraints(constraints, 1, 2, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(nameTypeTXT, constraints);
        add(nameTypeTXT);

        buildConstraints(constraints, 0, 3, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(rankTypeLbl, constraints);
        add(rankTypeLbl);
        buildConstraints(constraints, 1, 3, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(rankTypeBox, constraints);
        add(rankTypeBox);

        buildConstraints(constraints, 0, 4, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(weapTypeLbl, constraints);
        add(weapTypeLbl);
        buildConstraints(constraints, 1, 4, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(weapTypeBox, constraints);
        add(weapTypeBox);

        buildConstraints(constraints, 0, 5, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(cammTypeLbl, constraints);
        add(cammTypeLbl);
        buildConstraints(constraints, 1, 5, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(cammTypeBox, constraints);
        add(cammTypeBox);

        buildConstraints(constraints, 0, 6, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(armrTypeLbl, constraints);
        add(armrTypeLbl);
        buildConstraints(constraints, 1, 6, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(armrTypeBox, constraints);
        add(armrTypeBox);

        buildConstraints(constraints, 0, 7, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(priWTypeLbl, constraints);
        add(priWTypeLbl);
        buildConstraints(constraints, 1, 7, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(priWTypeBox, constraints);
        add(priWTypeBox);
   
        buildConstraints(constraints, 0, 8, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(secWTypeLbl, constraints);
        add(secWTypeLbl);
        buildConstraints(constraints, 1, 8, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(secWTypeBox, constraints);
        add(secWTypeBox);

        /**
        * NB check the gridbag y co-ord below corresponds with the one above!
        */
        for (int i=0; i<ammTypeLbl.length; i++) {
            buildConstraints(constraints, 0, (9+i), 1, 1, 100, 100);
            constraints.fill = GridBagConstraints.NONE;                 
            gridbag.setConstraints(ammTypeLbl[i], constraints);
            add(ammTypeLbl[i]);
            buildConstraints(constraints, 1, (9+i), 1, 1, 100, 100);
            constraints.fill = GridBagConstraints.HORIZONTAL;                 
            gridbag.setConstraints(ammTypeBox[i], constraints);
            add(ammTypeBox[i]);
        }
        buildConstraints(constraints, 0, (9+ammTypeBox.length), 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(addUnit, constraints);
        add(addUnit);
        buildConstraints(constraints, 1, (9+ammTypeBox.length), 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(howManyTXT, constraints);
        add(howManyTXT);

        //*********************************

        priWTypeBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)  {
                int[] tmpI = natFile.getMagSetup((String)priWTypeBox.getItemAt(priWTypeBox.getSelectedIndex()));
                for (int i=0; i<ammTypeBox.length; i++) {
                    //ammTypeBox[i].setSelectedIndex(tmpI[i]);
                    ammTypeBox[i].setSelectedIndex(priWTypeBox.getSelectedIndex());

                    /*if (priWTypeBox.getSelectedIndex()!=0 & tmpI[i]==0)
                        ammTypeBox[i].setEnabled(false);
                    else
                        ammTypeBox[i].setEnabled(true);*/
                }
            }
        });

        weapTypeBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)  {  
                System.out.println("dialogForceBuild/weapTypeBox - starting");
                int[] tmpI = natFile.getUnitSetup((String)weapTypeBox.getItemAt(weapTypeBox.getSelectedIndex()));
                System.out.println("dialogForceBuild/weapTypeBox - "+tmpI[0]+tmpI[1]+tmpI[2]+tmpI[3]);
                //cammTypeBox.setSelectedIndex(tmpI[0]);
                //armrTypeBox.setSelectedIndex(tmpI[1]);
                //priWTypeBox.setSelectedIndex(tmpI[2]); 
                //secWTypeBox.setSelectedIndex(tmpI[3]); 
                priWTypeBox.setSelectedIndex(weapTypeBox.getSelectedIndex()); 
                System.out.println("dialogForceBuild - selected index is : "+weapTypeBox.getSelectedIndex());
            }  
        });

        addUnit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)  {
                int iterations = 0;
                try {
                    iterations = Integer.parseInt(howManyTXT.getText());
                } catch (NumberFormatException err) { System.out.println(err); }

                
                System.out.println("the size of unitVec to start with for side "+side+" is : "+unitVec.size());
                for (int i=0; i<iterations; i++) {
                unitVec.addElement(new basicUnitInfo(side,
                                                     nation,
                                                     nameTypeTXT.getText().trim(),
                                                     natFile.getRankValues()[rankTypeBox.getSelectedIndex()],
                                                     natFile.getCammoValues()[cammTypeBox.getSelectedIndex()],
                                                     natFile.getArmourValues()[armrTypeBox.getSelectedIndex()],
                                                     natFile.weapLoadout(
                                                     (String)priWTypeBox.getItemAt(priWTypeBox.getSelectedIndex()),
                                                     (String)secWTypeBox.getItemAt(secWTypeBox.getSelectedIndex()),
                                                     (String)ammTypeBox[0].getItemAt(ammTypeBox[0].getSelectedIndex()),
                                                     (String)ammTypeBox[1].getItemAt(ammTypeBox[1].getSelectedIndex()),
                                                     (String)ammTypeBox[2].getItemAt(ammTypeBox[2].getSelectedIndex()),
                                                     (String)ammTypeBox[3].getItemAt(ammTypeBox[3].getSelectedIndex()),
                                                     (String)ammTypeBox[4].getItemAt(ammTypeBox[4].getSelectedIndex()),
                                                     (String)ammTypeBox[5].getItemAt(ammTypeBox[5].getSelectedIndex()),
                                                     (String)ammTypeBox[6].getItemAt(ammTypeBox[6].getSelectedIndex()),
                                                     (String)ammTypeBox[7].getItemAt(ammTypeBox[7].getSelectedIndex()) ) ));
                nameTypeTXT.setText(natFile.makeName(unitVec));
                }
            }
        });
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