package com.csf.java.agi.components.models.platforms;

import agi.foundation.Trig;
import agi.foundation.coordinates.Cartographic;
import agi.foundation.geometry.AxesNorthEastDown;
import agi.foundation.geometry.PointCartographic;
import com.csf.java.agi.components.utils.ExtensionGenerator;

import java.awt.*;
import java.util.Optional;

/**
 * This class extends the Platform object to provide an easy to use implementation of a Facility
 */
public class FacilityPlatform extends CustomPlatform {
    private static final Double DEFAULT_PIXEL_SIZE = 10.0;

    public FacilityPlatform(
            String name,
            double latDeg,
            double lonDeg,
            double altMeters) {
        super(name);
        initialize(latDeg, lonDeg, altMeters);
    }

    public FacilityPlatform(
            String name,
            double latDeg,
            double lonDeg,
            double altMeters,
            Optional<Color> color,
            Optional<Double> pixelSize) {
        super(name);
        initialize(latDeg, lonDeg, altMeters);
        addLabel(getName(), Optional.empty(), Optional.empty(), Optional.empty());
        if (color.isPresent()) {
            ExtensionGenerator.updateOrAddPointGraphicsExtension(this, color.get(), pixelSize.orElse(DEFAULT_PIXEL_SIZE));
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
