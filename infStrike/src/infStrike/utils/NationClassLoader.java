package infStrike.utils;

import infStrike.objects.nationFile2;

import java.util.*;
import java.net.*;
import java.io.*;
import java.io.File;
import java.util.jar.*;

public final class NationClassLoader extends URLClassLoader {
    private String file, name;
    File fileTmp;
    Object runtimeObject;
    URLClassLoader jarLoader;
    nationFile2 natFile;

    public NationClassLoader(URL[] urls, Vector nations) {
        super(urls);
		
        for (int i = 0; i < urls.length; i++) {
            file = urls[i].getFile();
            fileTmp = new File(file);
            System.out.println("NationClassLoader - Does "+fileTmp.toString()+" exists : "+fileTmp.exists());
            name = file.substring(file.lastIndexOf("/") + 1,file.lastIndexOf("."));

            runtimeObject = null;
            try {
                jarLoader = URLClassLoader.newInstance(new URL[] {urls[i]});
                System.out.println("NationClassLoader - JARloader trying : "+jarLoader.getURLs()[0].toString());
                runtimeObject = jarLoader.loadClass(name+"/"+name).newInstance();
            }
            catch (Exception e) {
                System.err.println("NationClassLoader - Stinking error : "+e);
            }

            if (runtimeObject != null) {
                natFile = (nationFile2)runtimeObject;
                nations.addElement(natFile);
            }
        }
    }
}

