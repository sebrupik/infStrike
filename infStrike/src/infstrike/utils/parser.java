package infStrike.utils;

import infStrike.objects.featForest;
import infStrike.objects.featLake;
import infStrike.objects.featBuilding;
import infStrike.objects.mapInfo;
import infStrike.objects.topoObj;
import infStrike.objects.infBasic;
import infStrike.objects.basicUnitInfo;
import infStrike.objects.varStoreObject;
import infStrike.objects.nationDatabase2;

import java.io.File;
import java.util.Vector;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParserFactory;  
import javax.xml.parsers.ParserConfigurationException;  
import javax.xml.parsers.SAXParser; 

public class parser {
    public static void fileRead(File file, varStoreObject varStore, nationDatabase2 natDatabase) {

        Vector unitList = new Vector() ;
        infBasic tmpUnit;
        basicUnitInfo tmpBUI;
        
        // Use the default (non-validating) parser
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            // Parse the input
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse( file, new XMLHandler(unitList, natDatabase) );
        }
	catch (Throwable t) {
            t.printStackTrace ();
        }
        
        for (int i = 0; i < unitList.size(); i++) {
            if (unitList.elementAt(i) instanceof basicUnitInfo) {
                tmpBUI = (basicUnitInfo)unitList.elementAt(i);
                if (!varStore.nameExists(tmpBUI.getName())) {
                    varStore.addUnit(tmpBUI);
                    System.out.print(".");
                }
                else 
                    System.out.println("Already present : "+tmpBUI.toString());
            }
            if (unitList.elementAt(i) instanceof mapInfo) {
                varStore.addObj(unitList.elementAt(i));
            }
            if (unitList.elementAt(i) instanceof featForest) {
                varStore.addObj(unitList.elementAt(i));
            }
            if (unitList.elementAt(i) instanceof featLake) {
                varStore.addObj(unitList.elementAt(i));
            }
            if (unitList.elementAt(i) instanceof featBuilding) {
                varStore.addObj(unitList.elementAt(i));
            } 
            if (unitList.elementAt(i) instanceof topoObj) {
                varStore.setTopo(unitList.elementAt(i));
            }
        }
        tmpUnit = null;
    }
}