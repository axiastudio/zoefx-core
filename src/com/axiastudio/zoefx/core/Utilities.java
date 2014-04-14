package com.axiastudio.zoefx.core;

import java.util.HashMap;

/**
 * User: tiziano
 * Date: 18/03/14
 * Time: 20:46
 */
public class Utilities {

    private static HashMap utilities = new HashMap();

    /**
     * Registers the unnamed utility for the given interface.
     *
     * @param utility The utility object to register
     * @param iface The interface implemented by the utility
     *
     */
    public static void registerUtility(Object utility, Class iface){
        Utilities.registerUtility(utility, iface, ".");

    }

    /**
     * Registers the named utility for the given interface.
     *
     * @param utility The utility object to register
     * @param iface The interface implemented by the utility
     * @param name The string name
     *
     */
    public static void registerUtility(Object utility, Class iface, String name){
        HashMap hm;
        Object hmObject = Utilities.utilities.get(iface);
        if( hmObject != null ){
            hm = (HashMap) hmObject;
        } else {
            hm = new HashMap();
        }
        hm.put(name, utility);
        Utilities.utilities.put(iface, hm);
    }

    /**
     * Query the unnamed utility with the given interface.
     *
     * @param iface The interface implemented by the utility
     * @return  The utility
     *
     */
    public static <T> T queryUtility(Class<T> iface){
        return Utilities.queryUtility(iface, ".");
    }

    /**
     * Query the named utility with the given interface.
     *
     * @param iface The interface implemented by the utility
     * @param name The string name
     * @return  The utility
     *
     */
    public static <T> T queryUtility(Class<T> iface, String name){
        HashMap hm;
        T utility = null;
        Object hmObject = Utilities.utilities.get(iface);
        if( hmObject != null ){
            hm = (HashMap) hmObject;
            utility = (T) hm.get(name);
        }
        return utility;
    }


}
