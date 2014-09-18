package com.axiastudio.zoefx.core.skins;

import com.axiastudio.zoefx.core.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: tiziano
 * Date: 23/08/14
 * Time: 14:06
 */
public class Skins {

    private static String activeSkinName=null;
    private static List<String> skins = new ArrayList<>();

    public static synchronized void registerSkin(ZSkin skin){
        Utilities.registerUtility(skin, ZSkin.class, skin.getName());
        skins.add(skin.getName());
        if( activeSkinName == null ){
            activeSkinName = skin.getName();
        }
    }

    public static ZSkin querySkin(String name){
        return Utilities.queryUtility(ZSkin.class, name);
    }

    public static synchronized ZSkin getActiveSkin(){
        if( activeSkinName == null ){
            registerSkin(new Black());
        }
        return querySkin(activeSkinName);
    }

    public static synchronized void activateSkin(String name){
        if( Utilities.queryUtility(ZSkin.class, name) != null ){
            activeSkinName = name;
        }
    }

    public static List<ZSkin> getSkins(){
        return skins.stream().map(name -> Utilities.queryUtility(ZSkin.class, name)).collect(Collectors.toList());
    }

}
