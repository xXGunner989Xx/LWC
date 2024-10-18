package com.griefcraft.lwc;

public class LWCInfo {

    /**
     * String value of LWCInfo.VERSION.
     */
    public static String FULL_VERSION;

    /**
     * LWC's version.
     * <p/>
     * Initialized to bogus value, but it will be set properly once the plugin starts up based
     * on the version listed in plugin.xml.
     */
    public static double VERSION;

    /**
     * Rather than managing the version in multiple spots, I added this method which will be
     * invoked from Plugin startup to set the version, which is pulled from the plugin.yml file.
     *
     * @param version
     * @author morganm
     */
    public static void setVersion(String version) {
        VERSION = Double.parseDouble(version);
        FULL_VERSION = version;
    }
}
