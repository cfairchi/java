package com.csf.java.agi.components.models;

import agi.foundation.MotionEvaluator1;
import agi.foundation.celestial.CentralBodiesFacet;
import agi.foundation.celestial.EarthCentralBody;
import agi.foundation.cesium.*;
import agi.foundation.propagators.CartesianOnePointPropagator;

import java.awt.*;
import java.util.HashMap;

public abstract class Vehicle extends CustomPlatform {
    EarthCentralBody earth = CentralBodiesFacet.getFromContext().getEarth();
    protected HashMap<String, SensorPlatform> m_Sensors = new HashMap<String, SensorPlatform>();
    protected MotionEvaluator1 pointEvaluator = null;
    protected CartesianOnePointPropagator defaultPropagator;

    public Vehicle(final String theName) {
        super(theName);
    }

    public MotionEvaluator1 getPointEvaluator() {
        if (pointEvaluator == null) {
            pointEvaluator = defaultPropagator.getEvaluator();
        }
        return pointEvaluator;
    }

    /**
     * Sets up the LabelGraphics to show a label attached to the SatellitePlatform in Cesium
     * @param theLabel The Text that should be displayed as the label
     * @param theColor TheColor of the Label Text
     */
    public void addGfxLabelExtension(String theLabel, Color theColor) {
        LabelGraphics gxLabel = new LabelGraphics();
        gxLabel.setText(new ConstantCesiumProperty<String>(theLabel));
        gxLabel.setFillColor(new ConstantCesiumProperty<Color>(theColor));
        gxLabel.setOutlineColor(new ConstantCesiumProperty<Color>(theColor));
        gxLabel.setOutlineWidth(new ConstantCesiumProperty<Double>(5.0));
        gxLabel.setHorizontalOrigin(new ConstantCesiumProperty<CesiumHorizontalOrigin>(CesiumHorizontalOrigin.CENTER));
        gxLabel.setVerticalOrigin(new ConstantCesiumProperty<CesiumVerticalOrigin>(CesiumVerticalOrigin.BOTTOM));
        this.getExtensions().add(new LabelGraphicsExtension(gxLabel));

    }
}
