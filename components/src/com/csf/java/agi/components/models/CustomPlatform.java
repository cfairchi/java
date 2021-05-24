package com.csf.java.agi.components.models;

import agi.foundation.cesium.ConstantCesiumProperty;
import agi.foundation.cesium.Description;
import agi.foundation.cesium.DescriptionExtension;
import agi.foundation.infrastructure.IdentifierExtension;
import agi.foundation.platforms.Platform;

import java.util.UUID;

public abstract class CustomPlatform extends Platform {
    private final String id = getPlatformType() + "/" + getName() + "_"+ UUID.randomUUID();
    public abstract PlatformType getPlatformType();

    public CustomPlatform(final String platformName) {
        super(platformName);
        addDefaultIdentifierExtension();
    }

    public String getId() {
        return id;
    }

    private void addDefaultIdentifierExtension() {
        //Add Identification Extension
        this.getExtensions().add(new IdentifierExtension(id));
        Description desc = new Description();
        desc.setText(new ConstantCesiumProperty<String>(getName()));
        this.getExtensions().add(new DescriptionExtension(desc));

    }
}
