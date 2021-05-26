package com.csf.java.agi.components.models.platforms;

import agi.foundation.celestial.CentralBodiesFacet;
import agi.foundation.celestial.EarthCentralBody;
import agi.foundation.cesium.*;
import agi.foundation.infrastructure.CopyContext;
import agi.foundation.infrastructure.IdentifierExtension;
import agi.foundation.platforms.Platform;
import com.csf.java.agi.components.utils.ExtensibleObjects;
import com.csf.java.agi.components.utils.ExtensionGenerator;
import com.google.common.collect.ImmutableMap;

import java.awt.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public abstract class CustomPlatform extends Platform {
    protected EarthCentralBody earth = CentralBodiesFacet.getFromContext().getEarth();
    private final String id;
    public abstract PlatformType getPlatformType();
    protected HashMap<String, SensorPlatform> m_Sensors = new HashMap<>();
    public ImmutableMap<String, SensorPlatform> getSensors() {
        return ImmutableMap.copyOf(m_Sensors);
    }

    public CustomPlatform(final String platformName) {
        super(platformName);
        id = getPlatformType() + "/" + getName() + "_"+ UUID.randomUUID();
        initialize();
    }

    public CustomPlatform(CustomPlatform existingInstance, CopyContext copyContext) {
        super("CopyOf_" + existingInstance.getName());
        id = getPlatformType() + "/" + getName() + "_"+ UUID.randomUUID();
        initialize();
    }

    public String getId() {
        return id;
    }

    public void addSensorPlatform(SensorPlatform sensorPlatform) {
        if (sensorPlatform != null) {
            m_Sensors.put(sensorPlatform.getId(), sensorPlatform);
        }
    }

    private void initialize() {
        ExtensibleObjects.addOrReplaceIdentifierExtension(this, id);
        ExtensibleObjects.addOrReplaceDescriptionExtension(this, id);
        ExtensionGenerator.updateOrAddLabelExtension(this, getName(), Color.WHITE, Optional.empty(), Optional.empty());
    }


}
