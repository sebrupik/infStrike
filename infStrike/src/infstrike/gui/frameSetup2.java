package infStrike.gui;

import infStrike.objects.varStoreObject;
import infStrike.utils.parser;
import infStrike.utils.arenaXmlFilter;
import infStrike.objects.nationDatabase2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.io.File;
import java.util.Vector;


public class frameSetup2 extends JFrame {
    JButton backBut;
    nationDatabase2 natDatabase;

    JPanel unitPanel;
    JButton createUnitsBut; 
    JLabel numSidesLbl;
    JTextField numSidesTxt;
    JButton clearUnitsBut;
    JButton saveUnitsBut;

    JPanel arenaPanel;
    JButton buildArenaBut;
    JButton loadArenaBut;

    JLabel picLabel; 
    JPanel mainPanel;

    
    varStoreObject varStore;
    Vector unitVec;
    JFileChooser chooser = new JFileChooser(new File("."));

    public frameSetup2(nationDatabase2 arg1) {
        this.natDatabase = arg1;

        varStore = new varStoreObject();
        unitVec = new Vector();

        unitPanel = new JPanel();
        arenaPanel = new JPanel();
        mainPanel = new JPanel();


        createUnitsBut = new JButton("Create Units");
        numSidesLbl = new JLabel("Number of oponents :");
        numSidesTxt = new JTextField("2");
        clearUnitsBut = new JButton("Clear Units");
        saveUnitsBut = new JButton("Save Units");
        buildArenaBut = new JButton("Build Arena");
        loadArenaBut = new JButton("Load Arena");

        picLabel = new JLabel(new ImageIcon("game/infStrike/images/setup.jpg"));


        backBut = new JButton("Back");

        setTitle("Infantry Strike Setup -- V1.2e");
        setSize(320,350);
        setDefaultCloseOperation(EXIT_ON_CLOSE) ;
        Container contentPane = getContentPane();


        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();  
        mainPanel.setLayout(gridbag);     
        unitPanel.setLayout(gridbag);     
        arenaPanel.setLayout(gridbag);  

        unitPanel.setBorder(new TitledBorder("Units"));
        arenaPanel.setBorder(new TitledBorder("Arena"));  
        contentPane.add(mainPanel, "Center");
        contentPane.add(backBut, "South");

        //mainPanel
        buildConstraints(constraints, 0, 0, 1, 2, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(picLabel, constraints);
        mainPanel.add(picLabel);
        buildConstraints(constraints, 1, 0, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(unitPanel, constraints);
        mainPanel.add(unitPanel);
        buildConstraints(constraints, 1, 1, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(arenaPanel, constraints);
        mainPanel.add(arenaPanel);
    
        //unitPanel
        buildConstraints(constraints, 0, 0, 2, 1, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(createUnitsBut, constraints);
        unitPanel.add(createUnitsBut);
        buildConstraints(constraints, 0, 1, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(numSidesLbl, constraints);
        unitPanel.add(numSidesLbl); 
        buildConstraints(constraints, 1, 1, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(numSidesTxt, constraints);
        unitPanel.add(numSidesTxt);
        buildConstraints(constraints, 0, 2, 2, 1, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(clearUnitsBut, constraints);
        unitPanel.add(clearUnitsBut);
        buildConstraints(constraints, 0, 3, 2, 1, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(saveUnitsBut, constraints);
        unitPanel.add(saveUnitsBut);

        //arenaPanel
        buildConstraints(constraints, 0, 0, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(buildArenaBut, constraints);
        arenaPanel.add(buildArenaBut);
        buildConstraints(constraints, 0, 1, 1, 1, 100, 100);
        constraints.fill = GridBagConstraints.HORIZONTAL;                 
        gridbag.setConstraints(loadArenaBut, constraints);
        arenaPanel.add(loadArenaBut); 


        createUnitsBut.addActionListener(new ActionListener() {
            private String info = "Information";
            private String err = "No nation files have been found.";
            public void actionPerformed(ActionEvent e) {
                if (natDatabase.getNumNations() > 0) {
                    dialogForceChoose chooFrame = new dialogForceChoose(Integer.parseInt(numSidesTxt.getText()), unitVec, true, natDatabase);
                }
                else {
                    JOptionPane.showMessageDialog(
                        createUnitsBut,
                        err,
                        info,
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }); 
        clearUnitsBut.addActionListener(new ActionListener() {
            private String clearSuc = "Unit vector cleared";
            private String clearFal = "Unit vector failed";
            private String info = "Information";
            public void actionPerformed(ActionEvent e) {
                System.out.println("Clearing "+varStore.getUnitVecSize()+" items");
                unitVec.clear();
                if (unitVec.size() == 0) {
                    JOptionPane.showMessageDialog(
                        clearUnitsBut,
                        clearSuc,
                        info,
                        JOptionPane.INFORMATION_MESSAGE);
                }
                else
                    JOptionPane.showMessageDialog(
                        clearUnitsBut,
                        clearFal,
                        info,
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        saveUnitsBut.addActionListener(new ActionListener() {
            private String genWarn = "Nothing to save";
            private String warn = "Warning";
            public void actionPerformed(ActionEvent e) {

                if (unitVec.size() == 0) {
                    JOptionPane.showMessageDialog(
                        saveUnitsBut,
                        genWarn,
                        warn,
                        JOptionPane.INFORMATION_MESSAGE);
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }
                dialogUnitSaver saveFrame = new dialogUnitSaver(unitVec);
            }
        });
        buildArenaBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                genArena genAr = new genArena();
            }
        });
        loadArenaBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                varStore.resetObj();
                
                int state = chooser.showOpenDialog(null);
                File file = chooser.getSelectedFile();

                varStore.resetArena();                
                if(file != null && state == JFileChooser.APPROVE_OPTION)  {     
		    JOptionPane.showMessageDialog(null, file.getPath());
                    //pass xml file to parser here.

                    parser.fileRead(file, varStore, natDatabase);
                    varStore.mkArena();
		}
		else if(state == JFileChooser.CANCEL_OPTION)  {
		    JOptionPane.showMessageDialog(null, "Canceled");
		}
		else if(state == JFileChooser.ERROR_OPTION) {
		    JOptionPane.showMessageDialog(null, "Error!");
	        }
                
                if (varStore.getArenaBoo()) {
                    JFrame arEditor = new frameArenaEditor(0,0,0,varStore.getArenaPlan());
                    arEditor.show();
                }
            }
        });


        backBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                JFrame start = new dialogStart(natDatabase);
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