package com.griefcraft.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class LocaleClassLoader extends ClassLoader {

    @Override
    protected URL findResource(String name) {
        File file = new File(Updater.DEST_LIBRARY_FOLDER + "locale/" + name);

        try {
            return new URL("file:" + file.getAbsolutePath());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return super.findResource(name);
    }

}
