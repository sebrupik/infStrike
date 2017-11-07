package infStrike.gui;

import infStrike.objects.worldObject;
import infStrike.utils.Vec2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.image.VolatileImage;

public class frameArenaRenderer2 extends Frame {
    Canvas canvas;

    private boolean isFullScreen = false;    
    GraphicsDevice device;
    private DisplayMode originalDM;
    private DisplayMode[] modes;
    private String[] strModes;
    private DisplayMode[] userModes;

    private int AISideDraw = -1;

    //**** graphic adjustment buttons ****
    //Button changeDMButY = new Button("Set Display");
    //Button changeDMButN = new Button("Restore");
    //JComboBox resBox = new JComboBox();

    //************* left panel buttons ***********
    Button lBut1 = new Button("Platoon Info");
    Button lBut2 = new Button("Base Info");
    Button lBut3 = new Button("AI Info");
    Button lBut4 = new Button("Unit Info");
    //************* right panel buttons ***********
    Button rBut1 = new Button("PAUSE!!");
    Button rBut2 = new Button("r Two");
    Button rBut3 = new Button("r Three");
    Button rBut4 = new Button("Exit");

    //*********extra panels
    Panel cfgPanel = new Panel();
    //JPanel dmPanel = new JPanel();
    Panel dmPanel = new Panel(new GridLayout(3, 1, 5, 5));
    //JPanel testPanel = new JPanel();
  
    Panel leftPanel = new Panel(new GridLayout(5, 1, 5, 5));
    Panel rightPanel = new Panel(new GridLayout(4, 1, 5, 5));

    public frameArenaRenderer2(worldObject arg1) {
        super("Infantry Strike Renderer V0.5 - FS, VIBB");
        setSize(400, 400);
        setBackground(Color.black);
        addWindowListener(new WindowAdapter() {
	   @Override public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        canvas = new Canvas(arg1);

        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = env.getScreenDevices();
        device = devices[0];
 
        begin();
        scanRes();
 
        leftPanel.setBackground(Color.black); rightPanel.setBackground(Color.black); 
        // ***************** left panel
        dmPanel.setBackground(Color.black);

        //changeDMButY.setBackground(Color.black); //changeDMButY.setForeground(Color.white); //changeDMButY.setBorderPainted(false);
        //changeDMButN.setBackground(Color.black); //changeDMButN.setForeground(Color.white); //changeDMButN.setBorderPainted(false);
        //resBox.setBackground(Color.black); //resBox.setForeground(Color.white); //resBox.setBorderPainted(false);
        leftPanel.add(buttonColourAdjust(lBut1, Color.white, Color.black)); 
        leftPanel.add(buttonColourAdjust(lBut2, Color.white, Color.black));
        leftPanel.add(buttonColourAdjust(lBut3, Color.white, Color.black));
        leftPanel.add(buttonColourAdjust(lBut4, Color.white, Color.black));

        rightPanel.add(buttonColourAdjust(rBut1, Color.white, Color.black));
        rightPanel.add(buttonColourAdjust(rBut2, Color.white, Color.black));
        rightPanel.add(buttonColourAdjust(rBut3, Color.white, Color.black));
        rightPanel.add(buttonColourAdjust(rBut4, Color.white, Color.black));

        //dmPanel.add(changeDMButY);
        //dmPanel.add(changeDMButN);
        //dmPanel.add(resBox);

        //leftPanel.add(dmPanel);

        //getContentPane().add(new Canvas(arg1));  

        
        //getContentPane().add("Center", new Canvas(arg1));
        /*getContentPane().setLayout(new BorderLayout());
        getContentPane().add("Center", canvas);
        getContentPane().add("West", leftPanel);
        getContentPane().add("East", rightPanel);*/
        setLayout(new BorderLayout());
        add("Center", canvas);
        add("West", leftPanel);
        add("East", rightPanel);
        

        //getContentPane().add(tp);
        //tp.add("Canvas", new Canvas(arg1));
        //tp.add("Config", cfgPanel);

        //**************listeners
        //platoon info
        lBut1.addActionListener((ActionEvent e) -> {
            canvas.setPlatoonDraw();
        }); 
        //base info
        lBut2.addActionListener((ActionEvent e) -> {
            canvas.setBaseDraw();
        }); 
        //AI info
        lBut3.addActionListener((ActionEvent e) -> {
            AISideDraw = canvas.setAIDraw(AISideDraw);
            if (AISideDraw == -1)
                lBut3.setLabel("AI Info : None");
            else
                lBut3.setLabel("AI Info : "+AISideDraw);
        }); 
        //unit info
        lBut4.addActionListener((ActionEvent e) -> {
            canvas.setAgentDraw();
        }); 
        rBut1.addActionListener((ActionEvent e) -> {
            //pause the game
            if(canvas.isRunningMove()) {
                rBut1.setLabel("Start");
                canvas.stopMove();
            } else {
                rBut1.setLabel("Stop");
                canvas.startMove();
            }
        });


        rBut4.addActionListener((ActionEvent e) -> {
            dispose();
        });

        //changeDMButY.setEnabled(device.isDisplayChangeSupported());

        /*changeDMButY.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DisplayMode dm = modes[resBox.getSelectedIndex()];     
                //userModes[1] = dm;        
                device.setDisplayMode(dm);
                setSize(new Dimension(dm.getWidth(), dm.getHeight()));
                validate();
                resBoxListener();
                getContentPane().remove(cfgPanel);
            }
        });
        changeDMButN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                device.setDisplayMode(originalDM);
                setSize(new Dimension(originalDM.getWidth(), originalDM.getHeight()));
                validate();
                resBoxListener();
                getContentPane().remove(cfgPanel);
            }
        });*/
    }

    private Button buttonColourAdjust(Button but, Color fore, Color back) {
        but.setForeground(fore); rBut4.setBackground(back); //but.setBorderPainted(false)
        
        return but;
    }
    
    public void begin() {
        isFullScreen = device.isFullScreenSupported();
        setUndecorated(isFullScreen);
        setResizable(!isFullScreen);
        if (isFullScreen) {
            // Full-screen mode
            device.setFullScreenWindow(this);
            validate();
            //winFull();
        } else {
            // Windowed mode
            pack();
            setVisible(true);
        }
    }
    private void winFull() {
        System.out.println("fS");
        if (device.getFullScreenWindow() == null) {
            //setUndecorated(true);
            device.setFullScreenWindow(this);
            
            setResizable(false);
        }
        else {
            setUndecorated(true);
            device.setFullScreenWindow(null);
            setResizable(true);
        }

        //isUndecorated() ? setUndecorated(false) : setUndecorated(true);
        //isResizable() ? setResizable(false) :  setResizable(true);

        validate();
    }
    /**
    * Querys the system to find out what possible resolutions there are.
    */
    private void scanRes() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = env.getScreenDevices();
        //super(device[0].getDefaultConfiguration());
        this.device = devices[0];
        originalDM = device.getDisplayMode();
        modes = device.getDisplayModes();
        /*for (int i=0; i<modes.length; i++) {
            resBox.addItem(Integer.toString(modes[i].getWidth())+"x"+Integer.toString(modes[i].getHeight())+" - "+Integer.toString(modes[i].getBitDepth()));
        }
        resBoxListener();*/
    }
    /*private void resBoxListener() {
        for (int i=0; i<modes.length; i++) {
            if(originalDM == modes[i]) 
                resBox.setSelectedIndex(i);
        }
    }*/
}
//**********************************************
class Canvas extends Panel implements ActionListener {
    private int moveInt;
    private Timer moveTimer; // = new Timer(100, this);
    private Timer drawTimer = new Timer(100, this);
    private Graphics2D g2;	
    private worldObject world;
    private double time;  //not real world time, but a number to passed through the game for "timelining" purposes

    private Image backBuffer;
    private boolean bbVolatile = true;
    private int width, height;

    private boolean platoonDraw, baseDraw, agentDraw;
    private int AIDraw = -1;
    private boolean done;

//************************
    class MouseManager extends MouseAdapter implements MouseMotionListener {
        double lastx = 0.0;
        double lasty = 0.0;
        
        @Override public void mousePressed(MouseEvent e) {
            lastx = e.getX();
            lasty = e.getY();
        }

        @Override public void mouseMoved(MouseEvent e) { 
            //do nothing 
        }
        
        @Override public void mouseDragged(MouseEvent e) {
	    double dy = e.getY() - lasty;
            
            if ((e.getModifiers() & e.BUTTON1_MASK)!=0) {
            	double dx = e.getX() - lastx;
                translation.x(translation.x()+dx/scale);
                translation.y(translation.y()+dy/scale);
            }
            else if ((e.getModifiers() & e.BUTTON3_MASK)!=0) {
                if (dy >= 0) {
                    scale *= (1.0+(double)dy/50.0);
                }
                else {
                    scale /= (1.0+(-dy/50.0));
                }
            }
            
            lastx = e.getX();
            lasty = e.getY();
        }
    }
    int xres;
    int yres;

    Vec2 translation = new Vec2(0.0,0.0);
    double scale = 1.0;
//*****************

    public Canvas(worldObject arg1) {
        moveTimer = new Timer(moveInt, this);
        time = 0.0;

        world = arg1;
        MouseManager mm = new MouseManager();
        addMouseListener(mm);
        addMouseMotionListener(mm);

        done = false;

        start();
    }

    @Override synchronized public void paint(Graphics g) {
        super.paintComponents(g); 
        g2 = (Graphics2D)g;
        initOffscreen();

        try {
            do {
                resetRestoreVolatileImages();
                Graphics2D gBB = (Graphics2D)backBuffer.getGraphics();
                gBB.clearRect(0,0,getWidth(), getHeight());
                gBB.translate(getWidth()/2, getHeight()/2);
                gBB.scale(scale, scale);
                gBB.translate(translation.x(), translation.y());
                world.updateGraphics(time, gBB, platoonDraw, AIDraw, baseDraw, agentDraw);
                gBB.dispose();
                g2.drawImage(backBuffer, 0, 0, this);
            } while (bbVolatile &&  ((VolatileImage)backBuffer).contentsLost());
        } catch (Exception e) {
            System.out.println("Exception during paint: " + e);
        }
    }

    public synchronized void initOffscreen() {
        if (backBuffer == null || width != getWidth() || height != getHeight()) {
            width = getWidth();
            height = getHeight();
            if (bbVolatile) {
                backBuffer = createVolatileImage(width, height);
                System.out.println("created a volatile back buffer");
            }
            else {
                backBuffer = createImage(width, height);
            }
        }
    }

    /**
    * For any of our images that are volatile, if the contents of
    * the image have been lost since the last reset, reset the image
    * and restore the contents.
    */
    public void resetRestoreVolatileImages() {
        //System.out.println("a volatile image has been lost!");
        GraphicsConfiguration gc = this.getGraphicsConfiguration();
        if (bbVolatile) {
            int valCode = ((VolatileImage)backBuffer).validate(gc);
            if (valCode == VolatileImage.IMAGE_INCOMPATIBLE) {
                backBuffer = createVolatileImage(width, height);
            }      
        }
    }

    @Override public void actionPerformed(ActionEvent event) {
        Timer t = (Timer)event.getSource();
        time += 0.25;
        if(t == moveTimer) { world.update(time); }
        if(t == drawTimer) { repaint(); }
    }

    public void stop() {
        moveTimer.stop();
        drawTimer.stop();
    }

    public void start() {
        moveTimer.start();
        drawTimer.start();
    }

    public void stopMove() { moveTimer.stop(); }
    public void startMove() { moveTimer.start(); }
    public boolean isRunningMove() { return moveTimer.isRunning(); }

    public boolean isRunning() { return moveTimer.isRunning() && drawTimer.isRunning(); }


    @Override public void update(Graphics g) { paint(g); }
    public void setPlatoonDraw() { platoonDraw = !platoonDraw; }
    public void setBaseDraw() { baseDraw = !baseDraw; }
    public void setAgentDraw() { agentDraw = !agentDraw; }
    public int setAIDraw(int sideDraw) { 
        sideDraw++;
        if (sideDraw >= world.getNumTeams())
            sideDraw = -1;
        AIDraw = sideDraw; 
        return sideDraw;
    }
}