package com.axiastudio.zoefx.core.skins;

import com.axiastudio.zoefx.core.Utilities;

/**
 * User: tiziano
 * Date: 23/08/14
 * Time: 14:06
 */
public class Skins {

    private static String activeSkinName=null;

    public static void registerSkin(ZSkin skin){
        Utilities.registerUtility(skin, ZSkin.class, skin.getName());
        if( activeSkinName == null ){
            activeSkinName = skin.getName();
        }
    }

    public static ZSkin querySkin(String name){
        return Utilities.queryUtility(ZSkin.class, name);
    }

    public static ZSkin getActiveSkin(){
        if( activeSkinName == null ){
            registerSkin(new Black());
        }
        return querySkin(activeSkinName);
    }

    public static void activateSkin(String name){
        if( Utilities.queryUtility(ZSkin.class, name) != null ){
            activeSkinName = name;
        }
    }

}
