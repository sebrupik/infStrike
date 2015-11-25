package infStrike.gui;

import infStrike.objects.varStoreObject;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.Vector;

public class dialogSetVars extends JDialog {
    private JLabel maxViewLbl;
    private JLabel maxVArcLbl;
    private JLabel numBaseLbl;
    private JLabel platSizLbl;
    
    
    private JTextField maxViewTXT;
    private JTextField maxVArcTXT;
    private JTextField numBaseTXT;
    private JTextField platSizTXT;

    private JCheckBox picWeapBox;
    private JCheckBox advWeapBox;
    private JCheckBox platoonBox;
    private JCheckBox advPathBox;

    private JButton setBut;

    private varStoreObject varStore;

    public dialogSetVars(varStoreObject arg1) {
        varStore = arg1;
        JPanel panel = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();  
        panel.setLayout(gridbag); 

        maxViewLbl = new JLabel("Max View (No Optics): ");
        maxVArcLbl = new JLabel("View Arc : ");
        numBaseLbl = new JLabel("Number of bases : ");
        platSizLbl = new JLabel("Max size of platoons : ");

        maxViewTXT = new JTextField("80");
        maxVArcTXT = new JTextField("90");
        numBaseTXT = new JTextField("3");
        platSizTXT = new JTextField("8");

        picWeapBox = new JCheckBox("Pick up weapons / ammo");
        advWeapBox = new JCheckBox("Advanced weapon balistics");
        platoonBox = new JCheckBox("Platoons created");
        advPathBox = new JCheckBox("Advanced path finding");

        setBut = new JButton("Set");

        buildConstraints(constraints, 0, 0, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(maxViewLbl, constraints);
        panel.add(maxViewLbl); 
        buildConstraints(constraints, 1, 0, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(maxViewTXT, constraints);
        panel.add(maxViewTXT);

        buildConstraints(constraints, 0, 1, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(maxVArcLbl, constraints);
        panel.add(maxVArcLbl);
        buildConstraints(constraints, 1, 1, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(maxVArcTXT, constraints);
        panel.add(maxVArcTXT);

        buildConstraints(constraints, 0, 2, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(numBaseLbl, constraints);
        panel.add(numBaseLbl);
        buildConstraints(constraints, 1, 2, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(numBaseTXT, constraints);
        panel.add(numBaseTXT);

        buildConstraints(constraints, 0, 3, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(platSizLbl, constraints);
        panel.add(platSizLbl);
        buildConstraints(constraints, 1, 3, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(platSizTXT, constraints);
        panel.add(platSizTXT);

        buildConstraints(constraints, 0, 4, 2, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(picWeapBox, constraints);
        panel.add(picWeapBox);
        buildConstraints(constraints, 0, 5, 2, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(advWeapBox, constraints);
        panel.add(advWeapBox);
        buildConstraints(constraints, 0, 6, 2, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(platoonBox, constraints);
        panel.add(platoonBox);
        buildConstraints(constraints, 0, 7, 2, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(advPathBox, constraints);
        panel.add(advPathBox);

        buildConstraints(constraints, 0, 8, 2, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(setBut, constraints);
        panel.add(setBut);

        setBut.addActionListener(new ActionListener() {
            private String numFal = " is not a number";
            private String info = "Information";
            public void actionPerformed(ActionEvent e)  {
                try {
                    varStore.setVariables(Integer.parseInt(maxViewTXT.getText()), Integer.parseInt(maxVArcTXT.getText()), 
                                          Integer.parseInt(numBaseTXT.getText()), Integer.parseInt(platSizTXT.getText()),
                                          picWeapBox.isSelected(), advWeapBox.isSelected(), 
                                          platoonBox.isSelected(), advPathBox.isSelected());
                    dispose();
                }
                catch (NumberFormatException exp) {
                    JOptionPane.showMessageDialog(
                        setBut,
                        exp+numFal,
                        info,
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        getContentPane().add(panel);
        setModal(true);
        setTitle("Set-Vars V1.0");
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