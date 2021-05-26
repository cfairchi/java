package com.csf.java.agi.components.models.platforms;

import agi.foundation.cesium.*;
import agi.foundation.cesium.advanced.IPolylineMaterialGraphics;
import agi.foundation.geometry.AxesVehicleVelocityLocalHorizontal;
import agi.foundation.propagators.Sgp4Propagator;
import agi.foundation.propagators.TwoLineElementSet;

import java.awt.*;

/** This class extends the Platform object to provide an easy to use implementation of a Satellite */
public class SatellitePlatform extends PropagatedPlatform {
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
        if (theLabelColor != null) this.addCesiumGfxLabelExtension(theName, theLabelColor);
        if (theOrbitLineColor != null) this.addCesiumOrbitGraphicsExtension(theOrbitLineColor);
    }

    @Override
    public PlatformType getPlatformType() {
        return PlatformType.SATELLITE;
    }

    public int getSSC() {
        return sscNum;
    }

    public TwoLineElementSet getTLE() {
        return new TwoLineElementSet(tle.toTleString());
    }

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

    public void addCesiumOrbitGraphicsExtension(Color theColor) {
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
