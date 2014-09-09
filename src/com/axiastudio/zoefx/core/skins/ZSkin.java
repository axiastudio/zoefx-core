package com.axiastudio.zoefx.core.skins;

import java.util.Optional;

/**
 * User: tiziano
 * Date: 23/08/14
 * Time: 10:34
 */
public interface ZSkin {

    public String getName();
    public default Boolean noIcons() { return Boolean.FALSE; };
    public default Optional<String> getStyle(){
        return Optional.empty();
    };

}
