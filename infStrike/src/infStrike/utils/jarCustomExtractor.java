/**
* The user creates an instance of this class by passing it the
* URL of the JAR file in its constructor. Once created the user
* can run methods to return a JPEG or the info file from the JAR file.
*
* I'm so proud of this class!
*/

package infStrike.utils;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.jar.*;
import javax.swing.ImageIcon;

public class jarCustomExtractor {
    private URL url;    
    private URL jarURL;
    private JarURLConnection jarConnection;
    private JarFile jarFile;
    private JarEntry jarEntry;

    String name, file;

    private InputStream in;
    private byte[] data;
    private int contentLength, bytesRead, offset;

    public jarCustomExtractor(URL arg1) {
        this.url = arg1;
        try {
            jarURL = new URL("jar:"+url+"!/"); 
            jarConnection = (JarURLConnection)jarURL.openConnection();
            jarFile = jarConnection.getJarFile();
        }
        catch (IOException e) {
            System.err.println(e);
        }
        file = url.getFile();
        name = file.substring(file.lastIndexOf("/") + 1,file.lastIndexOf("."));

        for (Enumeration e = jarFile.entries() ; e.hasMoreElements() ;) {
            System.out.println("JARFile contents : "+e.nextElement());
        }
    }

    /**
    * This is a pretty poor check as it only checks number of file in the JAR and
    * not their format (.info, .setup, .class, .java, .jpg)
    */
    public boolean checkJar() {
        if (name.startsWith("gun")) {
            System.out.println("this is a gun jar");
            if(jarFile.size() == 6) 
                return true;
            else
                System.out.println("jarCustomExtractor/checkJar - There are the wrong number of files in this jar file : "+jarFile.size());
        }
        
        if (name.startsWith("nat")) {
            System.out.println("this is a nation jar");
            if(jarFile.size() == 4) 
                return true;
            else
                System.out.println("jarCustomExtractor/checkJar - There are the wrong number of files in this jar file : "+jarFile.size());
        }
 
        return false;
    }

    public String extractInfo() {
        System.out.println("jarCustomExtractor/extractInfo - About to extract some info.");
        jarEntry = jarFile.getJarEntry(name+"/"+name+".info");
        extractJarEntry();
        return new String(data);
    }

    public ImageIcon extractImage() {
        System.out.println("jarCustomExtractor/extractImage - About to extract an image.");
        jarEntry = jarFile.getJarEntry(name+"/"+name+".jpg");
        extractJarEntry();
        return new ImageIcon(data);
    }

    public String[] extractSetup() {
        System.out.println("jarCustomExtractor/extractSetup - About to extract setup information.");
        jarEntry = jarFile.getJarEntry(name+"/"+name+".setup");
        extractJarEntry();
        String[] strAr = new String(data).split(";");
        for (int i=0; i<strAr.length; i++)
            strAr[i] = strAr[i].trim();

        return strAr;
    }

    /**
    * Given a JarEntry this simple extracts it to a inputstream which is then
    * piped to the byte array 'data'.
    * Check 'fileGrabber' class in fileMonster for full file writing code.
    */
    private void extractJarEntry() {
        data = null;
        System.out.println("jarCustomExtractor/extractJarEntry - Extracting "+jarEntry.getName()+" "+jarEntry.getSize());
        try {
            in = jarFile.getInputStream(jarEntry);
            contentLength = (int)jarEntry.getSize(); 
            data = new byte[contentLength];
            bytesRead = 0;
            offset = 0;

            while (offset < contentLength) {
                bytesRead = in.read(data, offset, data.length-offset);
                if (bytesRead == -1) 
                    break;
                offset += bytesRead;
            }
            in.close();
        }
        catch (IOException e) {
            System.err.println(e);
        }
    }
}