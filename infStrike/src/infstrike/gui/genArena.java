package infStrike.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.Vector;

public class genArena extends JDialog {
    private JLabel widthLbl;
    private JLabel heightLbl;
    private JLabel gridLbl;
    
    private JTextField widthTXT;
    private JTextField heightTXT;
    private JTextField gridTXT;

    private JButton setBut;

    public genArena() {
        JPanel panel = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();  
        panel.setLayout(gridbag); 

        widthLbl = new JLabel("Width (m) : ");
        heightLbl = new JLabel("Height (m) : ");
        gridLbl = new JLabel("Gridsize (m) : ");

        widthTXT = new JTextField("2000");
        heightTXT = new JTextField("2000");
        gridTXT = new JTextField("100");

        setBut = new JButton("Generate");

        buildConstraints(constraints, 0, 0, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(widthLbl, constraints);
        panel.add(widthLbl);
        buildConstraints(constraints, 1, 0, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(widthTXT, constraints);
        panel.add(widthTXT);

        buildConstraints(constraints, 0, 1, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(heightLbl, constraints);
        panel.add(heightLbl);
        buildConstraints(constraints, 1, 1, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(heightTXT, constraints);
        panel.add(heightTXT);

        buildConstraints(constraints, 0, 2, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(gridLbl, constraints);
        panel.add(gridLbl);
        buildConstraints(constraints, 1, 2, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(gridTXT, constraints);
        panel.add(gridTXT);

        buildConstraints(constraints, 0, 3, 2, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(setBut, constraints);
        panel.add(setBut);

        setBut.addActionListener(new ActionListener() {
            private String numFal = " is not a number";
            private String gridErr = "The grid height and width must be a multiple of the gridsize";
            private String info = "Information";
            public void actionPerformed(ActionEvent e)  {
                try {
                    if ((Integer.parseInt(widthTXT.getText()) % Integer.parseInt(gridTXT.getText())) == 0 & (Integer.parseInt(heightTXT.getText()) % Integer.parseInt(gridTXT.getText())) == 0) {
                        JFrame arEditor = new frameArenaEditor(Integer.parseInt(widthTXT.getText()), 
                                                               Integer.parseInt(heightTXT.getText()), 
                                                               Integer.parseInt(gridTXT.getText()), 
                                                               null);
                        arEditor.show();
                        
                        dispose();
                    }
                    else {
                        JOptionPane.showMessageDialog(
                        setBut,
                        gridErr,
                        info,
                        JOptionPane.INFORMATION_MESSAGE);
                   }
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
        setTitle("Ar-Gen V1.0");
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