package com.csf.java.agi.components.models.platforms;

import agi.foundation.geometry.AxesVehicleVelocityLocalHorizontal;
import agi.foundation.propagators.Sgp4Propagator;
import agi.foundation.propagators.TwoLineElementSet;
import agi.foundation.time.Duration;
import com.csf.java.agi.components.utils.ExtensionGenerator;

import java.awt.*;
import java.util.Optional;

/**
 * This class extends the Platform object to provide an easy to use implementation of a Satellite
 */
public class SatellitePlatform extends PropagatedPlatform {
    private final int sscNum;
    private TwoLineElementSet tle = null;

    public SatellitePlatform(String theName, TwoLineElementSet tleSet) {
        super(theName);
        updateTLE(tleSet);
        sscNum = tleSet.getElementNumber();
    }

    public SatellitePlatform(String theName, TwoLineElementSet tleSet, Optional<Color> color, Optional<Color> labelColor,
                             Optional<Color> orbitLineColor) {
        super(theName);
        updateTLE(tleSet);
        sscNum = tleSet.getElementNumber();
        addLabel(getName(), labelColor, Optional.empty(), Optional.empty());

        if (color.isPresent()) {
            ExtensionGenerator.updateOrAddPointGraphicsExtension(this, color.get(), 10.0);
        }

        if (orbitLineColor.isPresent()) {
            ExtensionGenerator.updateOrAddPathGraphics(this, orbitLineColor.get(), Color.black, Duration.fromMinutes(44 * 60),
                                                       Duration.fromMinutes(44.0 * 60.0), 2.0, 1.0);
        }
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

}
