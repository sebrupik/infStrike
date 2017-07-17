package infStrike.objects;

import java.util.ArrayList;
import java.awt.geom.Point2D;

public class varStoreObject {
    private final String _CLASS;
    private ArrayList<basicUnitInfo> unitAl;
    private ArrayList objAl;
    private arenaPlan arena;
    private topoObj topo;
    private int view;
    private int vArc;
    private int numBases;
    private int platSize;
    private boolean picWeap;
    private boolean advWeap;
    private boolean platoon;
    private boolean advPath;

    private boolean varBoo;
    private boolean arenaBoo;

    private infBasic tmpUnit;
    private basicUnitInfo tmpBUI;

    public varStoreObject() {
        this._CLASS = this.getClass().getName();
        
        unitAl = new ArrayList<basicUnitInfo>();
        objAl = new ArrayList();
    }

    
    public void addUnit(basicUnitInfo arg1) {
        System.out.println(_CLASS+"/addUnit - adding unit : "+arg1.getName());
        unitAl.add(arg1);
    }
    public void resetArena() {
        arena = null;
        topo = null;
    }

    
 
    public void setVariables(int view, int vArc, int numBases, int platSize, boolean picWeap, boolean advWeap, boolean platoon, boolean advPath) {
        this.view =  view;
        this.vArc = vArc;
        this.numBases = numBases;
        this.platSize = platSize;
        this.picWeap = picWeap;
        this.advWeap = advWeap;
        this.platoon = platoon;
        this.advPath = advPath;
        varBoo = true;
        System.out.println(view+" "+vArc+" "+numBases+" "+platSize+" "+picWeap+" "+advWeap+" "+platoon+" "+advPath);
        System.out.println("varBoo is "+varBoo);
    }
                             
    public void mkArena() {
        if (objAl != null & topo != null) {
            arena = new arenaPlan(objAl, topo);
            arena.printInfo();
        }

        if (objAl.size() != 0)
            arenaBoo = true;
    }

    /**
    * Constructs a worldObject and entities and objects to it.
    * Once fully initialised returns the worldObject it has created.
    */
    public worldObject mkWorld() {
        System.out.println(_CLASS+"/mkWorld - making world");
        worldObject world = new worldObject(getNumSides(), arena);
        

        //int platoonSize = 8;
        int baseCapacity = platSize * 10;

        String[] aiTypes = new String[getNumSides()];
        java.util.Arrays.fill(aiTypes, "RCS");


        AIController AICont = null;
        ArrayList randomPositions = makeRandomBasePositions(arena.getWidth(), arena.getHeight(), numBases*getNumSides());

        for(int i=0; i < getNumSides(); i++) {
            if (aiTypes[i].equals("RCS")) {
                AICont = new RCSController(i, world);
                
            }
            /*if (aiTypes[i].equals("Basic")) {
                AICont = new BAIController(i, world);
            }*/
            world.add(AICont);
 
            for (int j=0; j<numBases; j++) {
                //world.add(new Base(new Point2D.Double((Math.random()*(arena.getWidth()-26)), (Math.random()*(arena.getHeight()-26))), i, "Side "+i+" base "+j, baseCapacity, world, AICont));
                world.add(new Base(pickRandomPosition(randomPositions), i, "Side "+i+" base "+j, baseCapacity, platSize, world, AICont));
            }
        }
        

        //add the user specified Soldiers
        for (int i=0; i<unitAl.size(); i++) {
            world.add((basicUnitInfo)unitAl.get(i));
        }

        System.out.println(_CLASS+"/mkWorld - setting variables");
        //world.setVars(view, vArc, numBase, platSize, picWeap, advWeap, platoon, advPath);
        System.out.println(_CLASS+"/mkWorld - returning world");
        return world;
    }

    /**
    * Creates a Vector of Point2D objects. The Points are randomly choosen from 
    * specific areas in the arena. The number of areas is equal to the number of bases,
    * making a grid over the arena. 
    */
    private ArrayList makeRandomBasePositions(int width, int height, int numOfBases) {
        System.out.println(_CLASS+"/makeRandomPositions- starting "+numOfBases);
        ArrayList<Point2D.Double> positions = new ArrayList<Point2D.Double>();
        int resX, resY;
        int mult = 0;
        int boundary = 100;  //the distance away from the real edge of the arena
       
        while ((mult*mult) < numOfBases) {
            mult++;
        }
    
        resX = width/mult;
        resY = height/mult;

        for (int x=0; x<width/resX; x++) {
            for (int y=0; y<height/resY; y++) {
                double xCor = (x*resX)+(Math.random()*resX);
                double yCor = (y*resY)+(Math.random()*resY);
                System.out.println(xCor+", "+yCor);
                positions.add(new Point2D.Double(xCor, yCor));
            }
        }
        System.out.println(_CLASS+"/makeRandomPositions- finishing "+positions.size());
        return positions;
    }

    private Point2D.Double pickRandomPosition(ArrayList v) {
        int index = (int)(Math.random()*v.size());
        Point2D.Double p2D = (Point2D.Double)v.get(index);
        v.remove(index);
        return p2D;
    }

//*******************************
    public boolean nameExists(String arg1) { 
        infBasic tmp;
        String tmpStr;
        for(int i =0; i < unitAl.size(); i++) {
            tmpBUI = (basicUnitInfo)unitAl.get(i);
            if(tmpBUI.getName().equals(arg1))
                return true;
        } 
        tmpBUI = null;
        return false;
    }

    public int getNumSides() {
        int cnt = -1;
        for(int i=0; i<unitAl.size(); i++) { 
            tmpBUI = (basicUnitInfo)unitAl.get(i);
            if(tmpBUI.getSide() > cnt)
                cnt = tmpBUI.getSide();
        }
        return cnt+1;
    }

    public void setTopo(Object arg1) { topo = (topoObj)arg1; }
    public void resetVar() { unitAl.clear(); }
    public void resetObj() { objAl.clear(); }
    public void addObj(Object arg1) { objAl.add(arg1); }

    public boolean getVarBoo() { return varBoo; }
    public boolean getArenaBoo() { return arenaBoo; }
    public int getUnitVecSize() { return unitAl.size(); }
    public arenaPlan getArenaPlan() { return arena; }   
}