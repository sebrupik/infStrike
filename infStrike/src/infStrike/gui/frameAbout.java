package infStrike.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class frameAbout extends JFrame {
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    JWindow window = new JWindow();
    JLabel label1;
    JLabel label2;
    JLabel label3;

    public frameAbout() {   
        int picChoose = (int)(Math.random() * 2);

        
        label1 = new JLabel(new ImageIcon("game/infStrike/images/RCS.jpg"));
        label2 = new JLabel(new ImageIcon("game/infStrike/images/eight35-engine.jpg"));
        label3 = new JLabel("  by Seb Rupik (27/03/03).");

        label1.setBorder(BorderFactory.createRaisedBevelBorder());
        label2.setBorder(BorderFactory.createRaisedBevelBorder());
        window.getContentPane().add(label1, BorderLayout.NORTH);
        window.getContentPane().add(label2, BorderLayout.CENTER);
        window.getContentPane().add(label3, BorderLayout.SOUTH);
        centerWindow();
        window.setVisible(true);
        window.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                window.dispose();
            }
        });
    }
     
    private void centerWindow() {
        Dimension scrnSize = toolkit.getScreenSize();
        Dimension labelSize1 = label1.getPreferredSize();
        int labelWidth1 = labelSize1.width;
        int labelHeight1 = labelSize1.height;
        Dimension labelSize2 = label2.getPreferredSize();
        int labelWidth2 = labelSize2.width;
        int labelHeight2 = labelSize2.height;

        window.setLocation(scrnSize.width/2 - (labelWidth1/2),
                           scrnSize.height/2 - ((labelHeight1+labelHeight2)/2));
        window.pack();        
    }
}