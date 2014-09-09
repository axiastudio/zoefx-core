package com.axiastudio.zoefx.core.skins;

import java.util.Optional;

/**
 * User: tiziano
 * Date: 29/08/14
 * Time: 14:23
 */
public class NoIcons implements ZSkin {

    @Override
    public String getName() {
        return "NoIcons";
    }

    @Override
    public Boolean noIcons() {
        return Boolean.TRUE;
    }

    @Override
    public Optional<String> getStyle() {
        return Optional.of("/com/axiastudio/zoefx/core/skins/noicons/style.css");
    }
}
