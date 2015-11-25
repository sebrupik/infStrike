package infStrike.gui;

import infStrike.objects.nationDatabase2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

public class weaponInfo extends JPanel {
    private JComboBox weaponSelect;
    private JLabel weapPic = new JLabel();
    private JTextArea textArea = new JTextArea();
    private JScrollPane scrollPane;

    private nationDatabase2 natDatabase;

    public weaponInfo(nationDatabase2 arg1) {
        this.natDatabase = arg1;
        BorderLayout b = new BorderLayout();
        setLayout(b);

        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        System.out.println("adding weapon names");
        weaponSelect = new JComboBox(natDatabase.getAllWeaponNames());
        System.out.println("added weapon names");
        scrollPane = new JScrollPane(textArea);
        weapPic.setBorder(BorderFactory.createLoweredBevelBorder());

        add("North", weaponSelect);
        add("South", weapPic);
        add("Center", scrollPane);

        weaponSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)  {            
                weapPic.setIcon(natDatabase.getPic("WEAPON", (String)weaponSelect.getItemAt(weaponSelect.getSelectedIndex())));  
                textArea.setText("");
              textArea.insert(natDatabase.getWeapInfo((String)weaponSelect.getItemAt(weaponSelect.getSelectedIndex())),0);
            }
        });
        weaponSelect.setSelectedIndex(0);
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