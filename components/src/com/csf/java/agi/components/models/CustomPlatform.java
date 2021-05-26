package com.csf.java.agi.components.models;

import agi.foundation.celestial.CentralBodiesFacet;
import agi.foundation.celestial.EarthCentralBody;
import agi.foundation.cesium.*;
import agi.foundation.infrastructure.IdentifierExtension;
import agi.foundation.platforms.Platform;
import com.google.common.collect.ImmutableMap;

import java.awt.*;
import java.net.URI;
import java.util.HashMap;
import java.util.UUID;

public abstract class CustomPlatform extends Platform {
    protected EarthCentralBody earth = CentralBodiesFacet.getFromContext().getEarth();
    private final String id = getPlatformType() + "/" + getName() + "_"+ UUID.randomUUID();
    public abstract PlatformType getPlatformType();
    protected HashMap<String, SensorPlatform> m_Sensors = new HashMap<>();
    public ImmutableMap<String, SensorPlatform> getSensors() {
        return ImmutableMap.copyOf(m_Sensors);
    }

    public void addSensorPlatform(SensorPlatform sensorPlatform) {
        if (sensorPlatform != null) {
            m_Sensors.put(sensorPlatform.getId(), sensorPlatform);
        }
    }

    public CustomPlatform(final String platformName) {
        super(platformName);
        addDefaultIdentifierExtension();
    }

    public void addCesiumModelGraphicsExtension(String theModelPath) {
        ModelGraphics gxModel = new ModelGraphics();
        try {
            URI modelUri = new URI(theModelPath);
            CesiumResource model = new CesiumResource(modelUri, CesiumResourceBehavior.LINK_TO);
            gxModel.setModel(new ConstantCesiumProperty<CesiumResource>(model));
            gxModel.setScale(new ConstantCesiumProperty<Double>(400.0));
            gxModel.setShow(new ConstantCesiumProperty<Boolean>(true));
            this.getExtensions().add(new ModelGraphicsExtension(gxModel));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addCesiumGfxLabelExtension(String theLabel, Color theColor) {
        LabelGraphics gxLabel = new LabelGraphics();
        gxLabel.setText(new ConstantCesiumProperty<String>(theLabel));
        gxLabel.setFillColor(new ConstantCesiumProperty<Color>(theColor));
        gxLabel.setOutlineColor(new ConstantCesiumProperty<Color>(theColor));
        gxLabel.setOutlineWidth(new ConstantCesiumProperty<Double>(5.0));
        gxLabel.setHorizontalOrigin(new ConstantCesiumProperty<CesiumHorizontalOrigin>(CesiumHorizontalOrigin.CENTER));
        gxLabel.setVerticalOrigin(new ConstantCesiumProperty<CesiumVerticalOrigin>(CesiumVerticalOrigin.BOTTOM));
        this.getExtensions().add(new LabelGraphicsExtension(gxLabel));

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
