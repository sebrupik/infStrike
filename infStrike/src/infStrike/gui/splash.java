package infStrike.gui;

import infStrike.utils.jarFinder;
import infStrike.objects.nationDatabase2;

import java.awt.*;

import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import java.net.URL;

public class splash extends JFrame {
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    JWindow window = new JWindow();
    JLabel label;
    private JProgressBar curPro;

    nationDatabase2 natB;

    public splash() {   
        label = new JLabel(new ImageIcon("infStrike/images/mainSplash.jpg"));
        label.setBorder(BorderFactory.createRaisedBevelBorder());

        curPro = new JProgressBar();
        curPro.setStringPainted(true);
             

        window.getContentPane().add(label, BorderLayout.CENTER);
        window.getContentPane().add(curPro, BorderLayout.SOUTH);
        centerWindow();
        window.setVisible(true);

        
        natB = new nationDatabase2( new jarFinder( curPro, new ArrayList<URL>(), new ArrayList<URL>() ).getVectors() );  
        window.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                window.dispose();
                JFrame start = new dialogStart(natB);
            }
        });
    }
     
    private void centerWindow() {
        Dimension scrnSize = toolkit.getScreenSize();
        Dimension labelSize = label.getPreferredSize();
        int labelWidth = labelSize.width;
        int labelHeight = labelSize.height;

        window.setLocation(scrnSize.width/2 - (labelWidth/2),
                           scrnSize.height/2 - (labelHeight/2));
        window.pack();        
    }
}