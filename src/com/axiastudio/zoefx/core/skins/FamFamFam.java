package com.axiastudio.zoefx.core.skins;

import java.util.Optional;

/**
 * User: tiziano
 * Date: 23/08/14
 * Time: 10:46
 */
public class FamFamFam implements ZSkin {

    @Override
    public String resourcesFolder() {
        return "/com/axiastudio/zoefx/core/skins/famfamfam/";
    }

    @Override
    public String getName() {
        return "famfamfam";
    }

    @Override
    public Optional<String> getStyle() {
        return Optional.of("/com/axiastudio/zoefx/core/skins/famfamfam/style.css");
    }
}
