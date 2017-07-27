package infStrike.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class dialogRenderOpt extends JDialog {
    JButton changeDMButY = new JButton("Set Display");
    JButton changeDMButN = new JButton("I don't want FS mode");
    JComboBox resBox = new JComboBox();

    private GraphicsDevice device;
    private DisplayMode[] modes;
    private DisplayMode[] userModes;
    private boolean isFullScreen = false;

    public dialogRenderOpt(GraphicsDevice arg1) {
        this.device = arg1;
        JPanel panel = new JPanel();
        panel.add(changeDMButY);
        panel.add(changeDMButN);
        panel.add(resBox);

        System.out.println("dia1");
        scanRes();
        System.out.println("dia2");

        changeDMButY.setEnabled(device.isDisplayChangeSupported());

        changeDMButY.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DisplayMode dm = modes[resBox.getSelectedIndex()];           
                device.setDisplayMode(dm);
                setSize(new Dimension(dm.getWidth(), dm.getHeight()));
                validate();
                dispose();
            }
        });
        changeDMButN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        getContentPane().add(panel);
        setModal(true);
        setTitle("Set Resolution V0.2");
        setSize(300, 150);
        setResizable(false);
        pack();
        setVisible(true);
    }

    /**
    * Querys the system to find out what possible resolutions there are.
    */
    private void scanRes() {
        modes = device.getDisplayModes();
        for (int i=0; i<modes.length; i++) {
            resBox.addItem(Integer.toString(modes[i].getWidth())+"x"+Integer.toString(modes[i].getHeight())+" - "+Integer.toString(modes[i].getBitDepth()));
        }
    }
}