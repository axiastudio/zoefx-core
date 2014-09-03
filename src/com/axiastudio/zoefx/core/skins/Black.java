package com.axiastudio.zoefx.core.skins;

import java.util.Optional;

/**
 * User: tiziano
 * Date: 23/08/14
 * Time: 10:35
 */
public class Black implements ZSkin {

    @Override
    public String resourcesFolder() {
        return "/com/axiastudio/zoefx/core/skins/black/";
    }

    @Override
    public String getName() {
        return "Black";
    }

    @Override
    public Optional<String> getStyle() {
        return Optional.of("/com/axiastudio/zoefx/core/skins/black/style.css");
    }

}
