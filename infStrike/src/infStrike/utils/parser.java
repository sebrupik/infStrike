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
import java.util.ArrayList;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParserFactory;  
import javax.xml.parsers.ParserConfigurationException;  
import javax.xml.parsers.SAXParser; 

public class parser {
    public static void fileRead(File file, varStoreObject varStore, nationDatabase2 natDatabase) {

        ArrayList unitList = new ArrayList() ;
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
            if (unitList.get(i) instanceof basicUnitInfo) {
                tmpBUI = (basicUnitInfo)unitList.get(i);
                if (!varStore.nameExists(tmpBUI.getName())) {
                    varStore.addUnit(tmpBUI);
                    System.out.print(".");
                }
                else 
                    System.out.println("Already present : "+tmpBUI.toString());
            }
            if (unitList.get(i) instanceof mapInfo) {
                varStore.addObj(unitList.get(i));
            }
            if (unitList.get(i) instanceof featForest) {
                varStore.addObj(unitList.get(i));
            }
            if (unitList.get(i) instanceof featLake) {
                varStore.addObj(unitList.get(i));
            }
            if (unitList.get(i) instanceof featBuilding) {
                varStore.addObj(unitList.get(i));
            } 
            if (unitList.get(i) instanceof topoObj) {
                varStore.setTopo(unitList.get(i));
            }
        }
        tmpUnit = null;
    }
}