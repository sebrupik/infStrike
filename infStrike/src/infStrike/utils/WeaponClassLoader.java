package infStrike.utils;

import infStrike.objects.weapFile;

import java.util.ArrayList;
import java.net.*;
import java.io.File;

public final class WeaponClassLoader extends URLClassLoader {
    private String file, name;
    File fileTmp;
    Object runtimeObject;
    URLClassLoader jarLoader;
    weapFile weap;

    public WeaponClassLoader(URL[] urls, ArrayList<weapFile> weapons) {
        super(urls);
		
        for (URL url : urls) {
            file = url.getFile();
            fileTmp = new File(file);
            System.out.println("Does "+fileTmp.toString()+" exists : "+fileTmp.exists());
            name = file.substring(file.lastIndexOf("/") + 1,file.lastIndexOf("."));
            runtimeObject = null;
            try {
                jarLoader = URLClassLoader.newInstance(new URL[]{url});
                System.out.println("JARloader trying : "+jarLoader.getURLs()[0].toString());
                //runtimeObject = jarLoader.loadClass(name+"/"+name).newInstance();
                runtimeObject = jarLoader.loadClass(name+"."+name).newInstance();
            } catch (Exception e) {
                System.err.println("Stinking error : "+e);
            }
            if (runtimeObject != null) {
                weap = (weapFile)runtimeObject;
                weapons.add(weap);
            }
        }
    }
}

