package infStrike.utils;

import infStrike.objects.basicUnitInfo;
import infStrike.objects.infBasic;
import infStrike.objects.featBuilding;
import infStrike.objects.topoObj;
import infStrike.objects.featForest;
import infStrike.objects.nationDatabase2;
import infStrike.objects.mapInfo;
import infStrike.objects.featLake;

import java.io.*;
import java.util.*;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParserFactory;  
import javax.xml.parsers.ParserConfigurationException;  
import javax.xml.parsers.SAXParser;  


public class XMLHandler extends DefaultHandler {
    private schema tempSch;
    private infBasic tempUnit;
  
    private nationDatabase2 natDatabase; // = new nationDatabase2();

    private basicUnitInfo tmpBUI;
    private boolean echoing = false ;
    private int indentLevel = 0 ;        // For indenting echoed XML
    private static final String INDENT_STRING = "  "; // what to indent by
    
    // for collecting characters within or between elements
    private static final int LASTCHARS_INITIAL_CAPACITY = 1024 ;
    private StringBuffer lastCharacters = new StringBuffer(LASTCHARS_INITIAL_CAPACITY) ;

    private Collection xmlCol ;
    private String[][] UnitValues = new String[2][16];
    private String[][] MapValues = new String[2][3];
    private String[][] ForestValues = new String[2][4];
    private String[][] LakeValues = new String[2][4];
    private String[][] BuildingValues = new String[2][5];
    private String[][] TopoValues = new String[2][6];
    private int AttrItt = 0;
    private boolean parseInf = false;
    private boolean parseMapinfo = false;
    private boolean parseArena = false;
    private boolean parseForest = false;
    private boolean parseLake = false;
    private boolean parseBuilding = false;
    private boolean parseTopo = false;

    public XMLHandler(Collection arg1, nationDatabase2 natDatabase) {
	xmlCol = arg1 ;
        this.natDatabase = natDatabase;
        echoing = true;
    }
    
    //===========================================================
    // SAX DocumentHandler methods
    //===========================================================

    public void startDocument ()
    throws SAXException {
    }

    public void endDocument ()
    throws SAXException {
        // tidy variables for GC process
        System.out.println("Tidying parser vars");
        tempUnit = null;
        xmlCol = null;
        UnitValues = null;
    }

    public void startElement (String namespaceURI,
			      String lName, // local name
			      String qName, // qualified name
			      Attributes attrs)
    throws SAXException {
	if (echoing) {
	    String elName = lName ;
	    if ("".equals(elName))
		elName = qName ;
            
            if("UNITINFO".equals(elName.toUpperCase())) { parseInf = true; }
            if("ARENAINFO".equals(elName.toUpperCase())) { parseArena = true; }
            if("INFO".equals(elName.toUpperCase())) { parseMapinfo = true; }
            if("FOREST".equals(elName.toUpperCase())) { parseForest = true; }
            if("LAKE".equals(elName.toUpperCase())) { parseLake = true; }
            if("BUILDING".equals(elName.toUpperCase())) { parseBuilding = true; }
            if("TOPOGRAPHY".equals(elName.toUpperCase())) { parseTopo = true; }

            if ("SCHEMA".equals(elName.toUpperCase())) { tempSch = new schema(); }

            // new infantry unit, so reset relevant variables
            if ("UNIT".equals(elName.toUpperCase()) & parseInf) {
                tempUnit = null;
                AttrItt = 0;
            }

            // new forest | lake | building | topography, so reset attr counter
            if (("INFO".equals(elName.toUpperCase()) |
                 "FOREST".equals(elName.toUpperCase()) |
                 "LAKE".equals(elName.toUpperCase()) |
                 "BUILDING".equals(elName.toUpperCase()) |
                 "TOPOGRAPHY".equals(elName.toUpperCase()) )
                 & parseArena) {
                AttrItt = 0;
            }

            if ("ATTRNAME".equals(elName.toUpperCase())) {
                if (attrs != null) {
                    tempSch.addAttrName(attrs.getValue(0));
                    tempSch.addType(attrs.getValue(1));
                }
            }

            // for parsing infantry units
            if ("ATTRIBUTE".equals(elName.toUpperCase()) & parseInf) {
                if (attrs != null) {
                    if ("SIDE".equals(attrs.getValue(0).toUpperCase())) { UnitValues[0][0] = (String)attrs.getValue(0); }
                    if ("NATION".equals(attrs.getValue(0).toUpperCase())) { UnitValues[0][1] = attrs.getValue(0); }
                    if ("NAME".equals(attrs.getValue(0).toUpperCase())) { UnitValues[0][2] = attrs.getValue(0); }
                    if ("RANK".equals(attrs.getValue(0).toUpperCase())) { UnitValues[0][3] = attrs.getValue(0); }
                    if ("CAMMO".equals(attrs.getValue(0).toUpperCase())) { UnitValues[0][4] = attrs.getValue(0); }
                    if ("ARMOUR".equals(attrs.getValue(0).toUpperCase())) { UnitValues[0][5] = attrs.getValue(0); }
                    if ("WEAPPRI".equals(attrs.getValue(0).toUpperCase())) { UnitValues[0][6] = attrs.getValue(0); }
                    if ("WEAPSEC".equals(attrs.getValue(0).toUpperCase())) { UnitValues[0][7] = attrs.getValue(0); }
                    if ("AMMO1".equals(attrs.getValue(0).toUpperCase())) { UnitValues[0][8] = attrs.getValue(0); }
                    if ("AMMO2".equals(attrs.getValue(0).toUpperCase())) { UnitValues[0][9] = attrs.getValue(0); }
                    if ("AMMO3".equals(attrs.getValue(0).toUpperCase())) {  UnitValues[0][10] = attrs.getValue(0); }
                    if ("AMMO4".equals(attrs.getValue(0).toUpperCase())) { UnitValues[0][11] = attrs.getValue(0); }
                    if ("AMMO5".equals(attrs.getValue(0).toUpperCase())) { UnitValues[0][12] = attrs.getValue(0); }
                    if ("AMMO6".equals(attrs.getValue(0).toUpperCase())) { UnitValues[0][13] = attrs.getValue(0); }
                    if ("AMMO7".equals(attrs.getValue(0).toUpperCase())) { UnitValues[0][14] = attrs.getValue(0); }
                    if ("AMMO8".equals(attrs.getValue(0).toUpperCase())) { UnitValues[0][15] = attrs.getValue(0); }
                }
            }

             // for parsing arena & info
            if ("ATTRIBUTE".equals(elName.toUpperCase()) & parseArena & parseMapinfo) {
                if (attrs != null) {
                    if ("NAME".equals(attrs.getValue(0).toUpperCase())) { MapValues[0][0] = attrs.getValue(0); }
                    if ("WIDTH".equals(attrs.getValue(0).toUpperCase())) { MapValues[0][1] = attrs.getValue(0); }
                    if ("HEIGHT".equals(attrs.getValue(0).toUpperCase())) { MapValues[0][2] = attrs.getValue(0); }
                }
            }

            // for parsing arena & forest
            if ("ATTRIBUTE".equals(elName.toUpperCase()) & parseArena & parseForest) {
                if (attrs != null) {
                    if ("NUMPOINTS".equals(attrs.getValue(0).toUpperCase())) { ForestValues[0][0] = attrs.getValue(0); }
                    if ("XCORS".equals(attrs.getValue(0).toUpperCase())) { ForestValues[0][1] = attrs.getValue(0); }
                    if ("YCORS".equals(attrs.getValue(0).toUpperCase())) { ForestValues[0][2] = attrs.getValue(0); }
                    if ("NAME".equals(attrs.getValue(0).toUpperCase())) { ForestValues[0][3] = attrs.getValue(0); }
                }
            }
            // for parsing arena & Lake
            if ("ATTRIBUTE".equals(elName.toUpperCase()) & parseArena & parseLake) {
                if (attrs != null) {
                    if ("NUMPOINTS".equals(attrs.getValue(0).toUpperCase())) { LakeValues[0][0] = attrs.getValue(0); }
                    if ("XCORS".equals(attrs.getValue(0).toUpperCase())) { LakeValues[0][1] = attrs.getValue(0); }
                    if ("YCORS".equals(attrs.getValue(0).toUpperCase())) { LakeValues[0][2] = attrs.getValue(0); }
                    if ("NAME".equals(attrs.getValue(0).toUpperCase())) {  LakeValues[0][3] = attrs.getValue(0); }
                }
            }
            // for parsing arena & building
            if ("ATTRIBUTE".equals(elName.toUpperCase()) & parseArena & parseBuilding) {
                if (attrs != null) {
                    if ("NUMPOINTS".equals(attrs.getValue(0).toUpperCase())) { BuildingValues[0][0] = attrs.getValue(0); }
                    if ("XCORS".equals(attrs.getValue(0).toUpperCase())) { BuildingValues[0][1] = attrs.getValue(0); }
                    if ("YCORS".equals(attrs.getValue(0).toUpperCase())) { BuildingValues[0][2] = attrs.getValue(0); }
                    if ("NAME".equals(attrs.getValue(0).toUpperCase())) { BuildingValues[0][3] = attrs.getValue(0); }
                    if ("FLOORS".equals(attrs.getValue(0).toUpperCase())) { BuildingValues[0][4] = attrs.getValue(0); }
                }
            }
            // for parsing arena & topography
            if ("ATTRIBUTE".equals(elName.toUpperCase()) & parseArena & parseTopo) {
                if (attrs != null) {
                    if ("NUMPOINTS".equals(attrs.getValue(0).toUpperCase())) { TopoValues[0][0] = attrs.getValue(0); }
                    if ("NUMPOINTSX".equals(attrs.getValue(0).toUpperCase())) { TopoValues[0][1] = attrs.getValue(0); }
                    if ("NUMPOINTSY".equals(attrs.getValue(0).toUpperCase())) { TopoValues[0][2] = attrs.getValue(0); }
                    if ("GRID".equals(attrs.getValue(0).toUpperCase())) { TopoValues[0][3] = attrs.getValue(0); }
                    if ("MAXHEIGHT".equals(attrs.getValue(0).toUpperCase())) { TopoValues[0][4] = attrs.getValue(0); }
                    if ("GRIDVALUES".equals(attrs.getValue(0).toUpperCase())) { TopoValues[0][5] = attrs.getValue(0); }
                }
            }
	}
    }

    public void endElement(String namespaceURI,
                           String lName, // local name
                           String qName  // qualified name
			   )
    throws SAXException {
	if (echoing) {
	    String elName = lName ;
	    if ("".equals(elName))
		elName = qName ;

            if("UNITINFO".equals(elName.toUpperCase())) { parseInf = false; }
            if("ARENAINFO".equals(elName.toUpperCase())) {  parseArena = false; }

            // create infantry unit
            if ("UNIT".equals(elName.toUpperCase()) & parseInf) {
                if (tempSch.compare(UnitValues)) {
                    try {
                    tmpBUI = new basicUnitInfo(Integer.parseInt(UnitValues[1][0]),UnitValues[1][1],UnitValues[1][2], 
                                                                Double.parseDouble(UnitValues[1][3]), Double.parseDouble(UnitValues[1][4]), Double.parseDouble(UnitValues[1][5]),   
                                                                natDatabase.weapLoadout(UnitValues[1][1],
                                                                    UnitValues[1][6],UnitValues[1][7],
                                                                    UnitValues[1][8],UnitValues[1][9],UnitValues[1][10],
                                                                    UnitValues[1][11],UnitValues[1][12],UnitValues[1][13],
                                                                    UnitValues[1][14],UnitValues[1][15])
                                                                );
                    if(natDatabase.checkItems(tmpBUI)) 
                        xmlCol.add(tmpBUI);

                    tmpBUI = null;
                    } catch (Exception e) { System.out.println("There was a problem encounted whilst creating a BUI Object. Please ensure that XML file is up to date. --- "+e); }
                }
                else 
                    System.out.println("Failed file format check");                
            }
            // create mapInfo
            if ("INFO".equals(elName.toUpperCase()) & parseArena & parseMapinfo) {   
                parseMapinfo = false;  
                xmlCol.add(new mapInfo(MapValues[1][0],
                                      Integer.parseInt(MapValues[1][1]),
                                      Integer.parseInt(MapValues[1][2])));
            }

            // create forest
            if ("FOREST".equals(elName.toUpperCase()) & parseArena & parseForest) { 
                parseForest = false;  
                xmlCol.add(new featForest(Integer.parseInt(ForestValues[1][0]),
                                      getSubNum(ForestValues[1][1]),
                                      getSubNum(ForestValues[1][2]),
                                      ForestValues[1][3]));
            }
            // create lake
            if ("LAKE".equals(elName.toUpperCase()) & parseArena & parseLake) { 
                parseLake = false;  
                xmlCol.add(new featLake(Integer.parseInt(LakeValues[1][0]),
                                      getSubNum(LakeValues[1][1]),
                                      getSubNum(LakeValues[1][2]),
                                      LakeValues[1][3]));
            }
            // create building
            if ("BUILDING".equals(elName.toUpperCase()) & parseArena & parseBuilding) {    
                parseBuilding = false;    
                xmlCol.add(new featBuilding(Integer.parseInt(BuildingValues[1][0]),
                                      getSubNum(BuildingValues[1][1]),
                                      getSubNum(BuildingValues[1][2]),
                                      BuildingValues[1][3],
                                      Integer.parseInt(BuildingValues[1][4])));     
            }
            // create topography
            if ("TOPOGRAPHY".equals(elName.toUpperCase()) & parseArena & parseTopo) {    
                parseTopo = false;   
                int[] tempTopo = getSubNum(TopoValues[1][5]);
                xmlCol.add(new topoObj(Integer.parseInt(TopoValues[1][1]),
                                       Integer.parseInt(TopoValues[1][2]),  
                                       Integer.parseInt(TopoValues[1][3]),  
                                       Integer.parseInt(TopoValues[1][4]),
                                       tempTopo));   
                System.out.println("PARSER - Topogrpahy added");      
            }


            if ("ATTRIBUTE".equals(elName.toUpperCase()) & parseInf) {
                String lc = lastCharacters.toString().trim() ;
                UnitValues[1][AttrItt] = lc;
                AttrItt++;
            }

            if ("ATTRIBUTE".equals(elName.toUpperCase()) & parseArena & parseMapinfo) {
                String lc = lastCharacters.toString().trim() ;
                MapValues[1][AttrItt] = lc;
                AttrItt++;
            }
            if ("ATTRIBUTE".equals(elName.toUpperCase()) & parseArena & parseForest) {
                String lc = lastCharacters.toString().trim() ;
                ForestValues[1][AttrItt] = lc;
                AttrItt++;
            }
            if ("ATTRIBUTE".equals(elName.toUpperCase()) & parseArena & parseLake) {
                String lc = lastCharacters.toString().trim() ;
                LakeValues[1][AttrItt] = lc;
                AttrItt++;
            }
            if ("ATTRIBUTE".equals(elName.toUpperCase()) & parseArena & parseBuilding) {
                String lc = lastCharacters.toString().trim() ;
                BuildingValues[1][AttrItt] = lc;
                AttrItt++;
            }
            if ("ATTRIBUTE".equals(elName.toUpperCase()) & parseArena & parseTopo) {
                String lc = lastCharacters.toString().trim() ;
                TopoValues[1][AttrItt] = lc;
                AttrItt++;
            }
	}
	lastCharacters.setLength(0) ;
    }

    public void characters (char buf [], int offset, int len)
    throws SAXException {
	lastCharacters.append(buf, offset, len) ;
    }
    

    /**
    * Used to retrive values spaced with commas. 
    * NB 'tempNum' has to be so large in order to allow for large maps
    *    must have quite a high overhead, but should be GC'd after the return
    */
    private int[] getSubNum(String arg1) {
        String start = arg1;
        int index;
        int cnt = 0;
        int[] tempNum = new int[1000]; 
        try { 
        while (start.length() != 0) {
            index = start.indexOf(",");
            if (index == -1) {
                tempNum[cnt] = Integer.parseInt(start.substring(0, start.length()).trim());            
                start = "";
            }
            else {
                tempNum[cnt] = Integer.parseInt(start.substring(0, index).trim());            
                start = start.substring(index+2, start.length());
            }
            cnt++;
            }
        }
        catch(Exception ex) {
            System.out.println("Array to small. Contact Seb for a fix. \n"+ex);
        }

        int[] tmp = new int[cnt];
        for (int i =0; i < cnt; i++) {
            tmp[i] = tempNum[i];
        }
        return tmp;
    }
}