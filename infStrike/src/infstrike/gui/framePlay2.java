package infStrike.gui;

import infStrike.objects.worldObject;
import infStrike.objects.mapInfo;
import infStrike.objects.nationDatabase2;
import infStrike.objects.topoObj;
import infStrike.objects.varStoreObject;
import infStrike.utils.parser;
import infStrike.utils.unitXmlFilter;
import infStrike.utils.arenaXmlFilter;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.io.File;
import java.util.Vector;

public class framePlay2 extends JFrame {
    JButton loadUnitsBut;
    JButton setVarsBut;
    JButton loadArenaBut;

    JLabel unitsGoLbl;
    JLabel varsGoLbl;
    JLabel arenaGoLbl;
    JLabel unitsGoVal;
    JLabel varsGoVal;
    JLabel arenaGoVal;
    JButton startBut;

    boolean unitsGo;
    boolean varsGo;
    boolean arenaGo;

    JFileChooser chooser = new JFileChooser(new File("."));
 
    varStoreObject varStore;
    worldObject test;
    JFrame renderer;
    Frame renderer2;

    JButton backBut;
    JPanel mainPanel;

    nationDatabase2 natDatabase;

    public framePlay2(nationDatabase2 arg1) {
        this.natDatabase = arg1;
        mainPanel = new JPanel();
        loadUnitsBut = new JButton("Load Units");
        setVarsBut = new JButton("Set Vars");
        loadArenaBut = new JButton("Load Arena");

        varStore = new varStoreObject();

        unitsGoLbl = new JLabel("Units status");
        varsGoLbl = new JLabel("Vars status");
        arenaGoLbl = new JLabel("Arena status");
        unitsGoVal = new JLabel("Incomplete");
        varsGoVal = new JLabel("Incomplete");
        arenaGoVal = new JLabel("Incomplete");
        startBut = new JButton("Start");
        backBut = new JButton("Back");

        unitsGoVal.setForeground(Color.red);
        varsGoVal.setForeground(Color.red);
        arenaGoVal.setForeground(Color.red);
        startBut.setEnabled(false);

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();  
        mainPanel.setLayout(gridbag);

        setTitle("Infantry Strike Control -- V1.5e");
        setSize(350, 160);  //460
        setDefaultCloseOperation(EXIT_ON_CLOSE) ;
        Container contentPane = getContentPane();


        //**************************
        buildConstraints(constraints, 0, 0, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(loadUnitsBut, constraints);
        mainPanel.add(loadUnitsBut);
        buildConstraints(constraints, 1, 0, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(setVarsBut, constraints);
        mainPanel.add(setVarsBut);
        buildConstraints(constraints, 2, 0, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(loadArenaBut, constraints);
        mainPanel.add(loadArenaBut);
        //**************************
        buildConstraints(constraints, 0, 1, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(unitsGoLbl, constraints);
        mainPanel.add(unitsGoLbl);
        buildConstraints(constraints, 1, 1, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(varsGoLbl, constraints);
        mainPanel.add(varsGoLbl);
        buildConstraints(constraints, 2, 1, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(arenaGoLbl, constraints);
        mainPanel.add(arenaGoLbl);
        //**************************
        buildConstraints(constraints, 0, 2, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(unitsGoVal, constraints);
        mainPanel.add(unitsGoVal);
        buildConstraints(constraints, 1, 2, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(varsGoVal, constraints);
        mainPanel.add(varsGoVal);
        buildConstraints(constraints, 2, 2, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(arenaGoVal, constraints);
        mainPanel.add(arenaGoVal);
        //**************************
        buildConstraints(constraints, 1, 3, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.NONE;                 
        gridbag.setConstraints(startBut, constraints);
        mainPanel.add(startBut);
        //**************************


        //contentPane.add(new mainPlayPanel(natDatabase), "Center");
        contentPane.add(mainPanel, "Center");
        contentPane.add(backBut, "South");


        loadUnitsBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chooser.resetChoosableFileFilters();
                chooser.addChoosableFileFilter(new unitXmlFilter());
                int state = chooser.showOpenDialog(null);
                File file = chooser.getSelectedFile();
                
                varStore.resetVar();
                if(file != null && state == JFileChooser.APPROVE_OPTION)  {     
		    JOptionPane.showMessageDialog(null, file.getPath());
                    //pass xml file to parser here.

                    parser.fileRead(file, varStore, natDatabase);
		}
		else if(state == JFileChooser.CANCEL_OPTION)  {
		    JOptionPane.showMessageDialog(null, "Canceled");
		}
		else if(state == JFileChooser.ERROR_OPTION) {
		    JOptionPane.showMessageDialog(null, "Error!");
	        }

                if (varStore.getUnitVecSize() != 0) {
                    unitsGoVal.setText("COMPLETE");
                    unitsGoVal.setForeground(Color.green);
                    unitsGo = true;  
                    startReady();
                }
            }
        });
        setVarsBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialogSetVars varFrame = new dialogSetVars(varStore);

                if (varStore.getVarBoo()) {
                    varsGoVal.setText("COMPLETE");
                    varsGoVal.setForeground(Color.green);
                    varsGo = true;  
                }
                startReady();
            }
        });
        loadArenaBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                varStore.resetObj();
                chooser.resetChoosableFileFilters();
                chooser.addChoosableFileFilter(new arenaXmlFilter());
                int state = chooser.showOpenDialog(null);
                File file = chooser.getSelectedFile();

                varStore.resetArena();                
                if(file != null && state == JFileChooser.APPROVE_OPTION)  {     
		    JOptionPane.showMessageDialog(null, file.getPath());
                    //pass xml file to parser here.

                    parser.fileRead(file, varStore, natDatabase);
		}
		else if(state == JFileChooser.CANCEL_OPTION)  {
		    JOptionPane.showMessageDialog(null, "Canceled");
		}
		else if(state == JFileChooser.ERROR_OPTION) {
		    JOptionPane.showMessageDialog(null, "Error!");
	        }
                varStore.mkArena();

                if (varStore.getArenaBoo()) {
                    arenaGoVal.setText("COMPLETE");
                    arenaGoVal.setForeground(Color.green);
                    arenaGo = true;  
                }
                startReady();
            }
        });
        startBut.addActionListener(new ActionListener() {
            private String genWarn = "Error encountered creating world object!\nPlease start setup process again.\n ";
            private String warn = "Warning";
            public void actionPerformed(ActionEvent e) {
                //try {
                    test = varStore.mkWorld();
                    test.printInfo(); 

                    //the old Swing based interface
                    //renderer = new frameArenaRenderer(test);                    
                    //renderer.show();

                    //the new suposedly faster AWT interface
                    renderer2 = new frameArenaRenderer2(test);
                    renderer2.show();
                    
                    //JFrame wireframe = new frameWireframeRenderer(test);
                    //wireframe.show();

                    test = null;
                    dispose();
                /*} 
                catch (Exception err) { 
                    JOptionPane.showMessageDialog(
                        startBut,
                        genWarn+err,
                        warn,
                        JOptionPane.INFORMATION_MESSAGE);
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }*/
            }
        });

        backBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                JFrame start = new dialogStart(natDatabase);
            }
        });
    }
    
    private void startReady() {
        if (unitsGo & varsGo & arenaGo) 
            startBut.setEnabled(true);
        else
            startBut.setEnabled(false);
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