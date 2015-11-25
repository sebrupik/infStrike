package infStrike.utils;

import infStrike.objects.weapFile;

import java.util.*;
import java.net.*;
import java.io.*;
import java.io.File;
import java.util.jar.*;

public final class WeaponClassLoader extends URLClassLoader {
    private String file, name;
    File fileTmp;
    Object runtimeObject;
    URLClassLoader jarLoader;
    weapFile weap;

    public WeaponClassLoader(URL[] urls, Vector weapons) {
        super(urls);
		
        for (int i = 0; i < urls.length; i++) {
            file = urls[i].getFile();
            fileTmp = new File(file);
            System.out.println("Does "+fileTmp.toString()+" exists : "+fileTmp.exists());
            name = file.substring(file.lastIndexOf("/") + 1,file.lastIndexOf("."));

            runtimeObject = null;
            try {
                jarLoader = URLClassLoader.newInstance(new URL[] {urls[i]});
                System.out.println("JARloader trying : "+jarLoader.getURLs()[0].toString());
                runtimeObject = jarLoader.loadClass(name+"/"+name).newInstance();
            }
            catch (Exception e) {
                System.err.println("Stinking error : "+e);
            }

            if (runtimeObject != null) {
                weap = (weapFile)runtimeObject;
                weapons.addElement(weap);
            }
        }
    }
}

