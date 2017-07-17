package infStrike.gui;

//import infStrike.data.varStore;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.Vector;

public class dialogNatSelect extends JDialog {
    int side;
    Vector natVec;
    String boxItem;
    private JLabel natSelLbl;
    
    private JComboBox nationBox;

    private JButton setBut;

    public dialogNatSelect(Vector arg1, int arg2) {
        natVec = arg1;
        side = arg2;
        JPanel panel = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();  
        panel.setLayout(gridbag); 

        natSelLbl = new JLabel("Side "+arg2+" to keep?");
        nationBox = new JComboBox();
        setBut = new JButton("Select");

        for (int i=0; i<natVec.size(); i++) {
            boxItem = (String)natVec.elementAt(i) 
                      //+"("+
                      //arStore.getNatNumber((String)natVec.elementAt(i), side)
                      +")";
            nationBox.addItem(boxItem);
        }

        buildConstraints(constraints, 0, 0, 3, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(natSelLbl, constraints);
        panel.add(natSelLbl); 
        buildConstraints(constraints, 1, 1, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(nationBox, constraints);
        panel.add(nationBox);

        buildConstraints(constraints, 1, 3, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(setBut, constraints);
        panel.add(setBut);

        setBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)  {
                //varStore.removeNations((String)nationBox.getItemAt(nationBox.getSelectedIndex()), side);
                //varStore.removeNations((String)natVec.elementAt(nationBox.getSelectedIndex()), side);
                dispose();
            }
        });

        getContentPane().add(panel);
        setModal(true);
        setTitle("Nat-Select V1.0");
        setSize(300, 150);
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
}