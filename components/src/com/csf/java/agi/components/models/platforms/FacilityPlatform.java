package com.csf.java.agi.components.models.platforms;

import agi.foundation.Trig;
import agi.foundation.cesium.*;
import agi.foundation.cesium.advanced.IMaterialGraphics;
import agi.foundation.coordinates.Cartesian;
import agi.foundation.coordinates.Cartographic;
import agi.foundation.geometry.AxesNorthEastDown;
import agi.foundation.geometry.PointCartographic;
import com.csf.java.agi.components.utils.ExtensionGenerator;

import java.awt.*;
import java.util.Optional;

/** This class extends the Platform object to provide an easy to use implementation of a Facility */
public class FacilityPlatform extends CustomPlatform {

    public FacilityPlatform(String name, double latDeg, double lonDeg, double altMeters) {
        super(name);
        initialize(latDeg, lonDeg, altMeters);
    }

    public FacilityPlatform(String name, double latDeg, double lonDeg, double altMeters, Color color, Color labelColor, double pixelSize) {
        super(name);
        initialize(latDeg, lonDeg, altMeters);
        if (color != null) {
            ExtensionGenerator.updateOrAddPointGraphicsExtension(this, color, pixelSize);
      }
    }

    @Override
    public PlatformType getPlatformType() {
        return PlatformType.FACILITY;
    }

    private void initialize(double latDeg, double lonDeg, double altMeters) {
        double longitude = Trig.degreesToRadians(lonDeg);
        double latitude = Trig.degreesToRadians(latDeg);
        this.setLocationPoint(new PointCartographic(earth, new Cartographic(longitude, latitude, altMeters)));
        this.setOrientationAxes(new AxesNorthEastDown(earth, this.getLocationPoint()));
    }
}
