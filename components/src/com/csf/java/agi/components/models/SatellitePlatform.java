package com.csf.java.agi.components.models;

import agi.foundation.cesium.*;
import agi.foundation.cesium.advanced.IPolylineMaterialGraphics;
import agi.foundation.geometry.AxesVehicleVelocityLocalHorizontal;
import agi.foundation.propagators.Sgp4Propagator;
import agi.foundation.propagators.TwoLineElementSet;

import java.awt.*;
import java.net.URI;
import java.util.Map;

/** This class extends the Platform object to provide an easy to use implementation of a Satellite */
public class SatellitePlatform extends Vehicle {
    private final int sscNum;
    private TwoLineElementSet tle = null;

    public SatellitePlatform(String theName, TwoLineElementSet tleSet) {
        super(theName);
        updateTLE(tleSet);
        sscNum = tleSet.getElementNumber();
    }

    public SatellitePlatform(String theName, TwoLineElementSet tleSet, Color theLabelColor, Color theOrbitLineColor) {
        super(theName);
        updateTLE(tleSet);
        sscNum = tleSet.getElementNumber();
        if (theLabelColor != null) this.addGfxLabelExtension(theName, theLabelColor);
        if (theOrbitLineColor != null) this.addOrbitGraphicsExtension(theOrbitLineColor);
    }

    @Override
    public PlatformType getPlatformType() {
        return PlatformType.SATELLITE;
    }

    public int getSSC() { return sscNum; }
    public TwoLineElementSet getTLE() { return tle; }

    public void updateTLE(TwoLineElementSet tle) {
        if (tle == null) {
            return;
        }
        pointEvaluator = null;
        this.tle = tle;
        defaultPropagator = new Sgp4Propagator(tle);
        this.setLocationPoint(defaultPropagator.createPoint());

        //Setup AxesVehicleVelocityLocalHorizontal as default orientation axes
        AxesVehicleVelocityLocalHorizontal vvlh = new AxesVehicleVelocityLocalHorizontal(earth.getInertialFrame(), this.getLocationPoint());
        this.setOrientationAxes(vvlh);
    }

    public Map<String, SensorPlatform> getSensors() {
        return m_Sensors;
    }

    /** Adds an existing SensorPlatform to be attached to this satellite */
    public void addSensorPlatform(SensorPlatform sensorPlatform) {
        if (sensorPlatform != null) {
            m_Sensors.put(sensorPlatform.getId(), sensorPlatform);
        }
    }


    /**
     * Adds a Cesium model graphics extension to this satellite using the model located at the provided path
     * @param theModelPath File path to the desired Cesium model
     */
    public void addModelGraphicsExtension(String theModelPath) {
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

    /**
     * Sets up PathGraphics to represent orbit path graphics in Cesium
     * @param theColor The Color of the orbit path line
     */
    public void addOrbitGraphicsExtension(Color theColor) {
        PolylineOutlineMaterialGraphics material = new PolylineOutlineMaterialGraphics();
        material.setColor(new ConstantCesiumProperty<Color>(theColor));
        material.setOutlineColor(new ConstantCesiumProperty<Color>(theColor));
        material.setOutlineWidth(new ConstantCesiumProperty<Double>(1.0));

        PathGraphics gxPath = new PathGraphics();
        gxPath.setLeadTime(new ConstantCesiumProperty<Double>(44.0 * 60.0));
        gxPath.setTrailTime(new ConstantCesiumProperty<Double>(44.0 * 60.0));
        gxPath.setMaterial(new ConstantCesiumProperty<IPolylineMaterialGraphics>(material));
        gxPath.setWidth(new ConstantCesiumProperty<>(2.0));
        this.getExtensions().add(new PathGraphicsExtension(gxPath));
    }


}
