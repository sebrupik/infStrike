package infStrike.gui;

import infStrike.objects.nationDatabase2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.applet.*; 
import java.net.*; 
import java.io.*; 

public class dialogStart extends JFrame {
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    private JLabel imgLbl;
    private JButton playBut;
    private JButton setupBut;
    private JButton aboutBut;
    private JButton helpBut; 
    private JButton quitBut;
    private JLabel infoLbl;
    JWindow window = new JWindow(); 

    nationDatabase2 natDatabase;

    public dialogStart(nationDatabase2 arg1) {
        int picChoose = (int)(Math.random() * 2);
        if (picChoose == 0)
            imgLbl = new JLabel(new ImageIcon(getClass().getResource("/infStrike/images/infSplash1.jpg")));
        if (picChoose == 1)
            imgLbl = new JLabel(new ImageIcon(getClass().getResource("/infStrike/images/infSplash2.jpg")));

        /*String p1 = getClass().getResource( "infSplash1.jpg" ).getPath();
        String p2 = getClass().getResource( "infSplash2.jpg" ).getPath();
        System.out.println(p1);
        System.out.println(p2);*/

        //natDatabase = new nationDatabase2();
        this.natDatabase = arg1;

        imgLbl.setBorder(BorderFactory.createRaisedBevelBorder());
        playBut = new JButton("Play");
        setupBut = new JButton("Setup");
        helpBut = new JButton("Help");
        aboutBut = new JButton("About");
        quitBut = new JButton("Quit");
        quitBut.setBackground(Color.black);
        quitBut.setForeground(Color.red);
        infoLbl = new JLabel("Weapons loaded : "+natDatabase.getNumWeapons()+" --- Nations loaded: "+natDatabase.getNumNations());

        //JPanel panel = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();  
        window.getContentPane().setLayout(gridbag); 

        //**************************
        buildConstraints(constraints, 0, 0, 5, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(imgLbl, constraints);
        window.getContentPane().add(imgLbl);

        buildConstraints(constraints, 0, 1, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(playBut, constraints);
        window.getContentPane().add(playBut);
        buildConstraints(constraints, 1, 1, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(setupBut, constraints);
        window.getContentPane().add(setupBut);
        buildConstraints(constraints, 2, 1, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(helpBut, constraints);
        window.getContentPane().add(helpBut);
        buildConstraints(constraints, 3, 1, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(aboutBut, constraints);
        window.getContentPane().add(aboutBut);
        buildConstraints(constraints, 4, 1, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(quitBut, constraints);
        window.getContentPane().add(quitBut);

        buildConstraints(constraints, 0, 5, 5, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(infoLbl, constraints);
        window.getContentPane().add(infoLbl);

        playBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame pFrame = new framePlay2(natDatabase);
                pFrame.setResizable(false);
                pFrame.setVisible(true);
                window.dispose();
            }
        });
        setupBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame sFrame = new frameSetup2(natDatabase);
                //sFrame.setResizable(false);
                sFrame.setVisible(true);
                window.dispose();
            }
        });
        aboutBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("hello");
                JFrame about = new frameAbout();
            }
        });
        quitBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                window.dispose();
                System.exit(0);
            }
        });

        centerWindow();
        window.setVisible(true);
    }
    void buildConstraints(GridBagConstraints gbc, int gx, int gy, int gw, int gh, int wx, int wy) {
        gbc.gridx = gx;
        gbc.gridy = gy;
        gbc.gridwidth = gw;
        gbc.gridheight = gh;
        gbc.weightx = wx;
        gbc.weighty = wy;
    }

    private void centerWindow() {
        Dimension scrnSize = toolkit.getScreenSize();
        Dimension labelSize1 = imgLbl.getPreferredSize();
        int labelWidth1 = labelSize1.width;
        int labelHeight1 = labelSize1.height;

        window.setLocation(scrnSize.width/2 - (labelWidth1/2),
                           scrnSize.height/2 - (labelHeight1/2));
        window.pack();        
    }
}