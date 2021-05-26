package com.csf.java.agi.components.models.platforms;

import agi.foundation.Trig;
import agi.foundation.access.LinkInstantaneous;
import agi.foundation.access.LinkRole;
import agi.foundation.celestial.CentralBodiesFacet;
import agi.foundation.celestial.EarthCentralBody;
import agi.foundation.coordinates.AxisIndicator;
import agi.foundation.coordinates.Cartesian;
import agi.foundation.geometry.*;
import agi.foundation.platforms.Platform;
import com.csf.java.agi.components.utils.ExtensionGenerator;

import java.awt.*;
import java.util.Optional;

/**
 * This class extends the Platform object to provide an easy to use implementation of a Sensor
 */
public class SensorPlatform extends CustomPlatform {
    private final EarthCentralBody earth = CentralBodiesFacet.getFromContext().getEarth();

    public SensorPlatform(String theName, Optional<Color> color) {
        super(theName);
        ExtensionGenerator.updateAddOrRemoveFovGraphicsExtension(this, color.orElse(Color.RED), 1000, true);
    }

    public void setParentBasedLocation() {
        if (this.getParent() != null) {
            this.setLocationPoint(new PointFixedOffset(this.getParent().getReferenceFrame(), Cartesian.getZero()));
            setOrientationAxes_NED();
        }
    }

    public void setOrientationAxes_NED() {
        this.setOrientationAxes(new AxesNorthEastDown(earth, this.getParent().getLocationPoint()));
    }

    public void setOrientationAxes_ENU() {
        this.setOrientationAxes(new AxesEastNorthUp(earth, this.getParent().getLocationPoint()));
    }

    public void setOrientationAxes_VVLH() {
        this.setOrientationAxes(new AxesVehicleVelocityLocalHorizontal(earth.getInertialFrame(), this.getLocationPoint()));
    }

    /**
     * Sets up the sensor orientation to point at a provided platform
     *
     * @param theTarget The Platfrom that the sensor should point at
     */
    public void setOrientationAxes_Pointing(Platform theTarget) {
        //First we need to setup a Link between this sensor and the target platform
        LinkInstantaneous pointingSensorLink = new LinkInstantaneous(this, theTarget);

        //Create reference vector to feed to AxesTargetingLink.  This vector represents the reference direction vector.
        //The Z-axis of this set of axes will be constrained to minimize the angular separation from this vector
        Vector reference = new VectorFixed(earth.getFixedFrame().getAxes(), new Cartesian(0.0, 0.0, 1.0));

        //Create AxesTargetingLink by providing the link we created above and the reference vector
        AxesTargetingLink targetingAxes = new AxesTargetingLink(pointingSensorLink, LinkRole.TRANSMITTER, reference);
        this.setOrientationAxes(targetingAxes);
    }

    /**
     * Sets up the sensor orientation to point at the provided azimuth and elevation
     *
     * @param theAzimuthDeg   Azimuth pointing offset in degrees
     * @param theElevationDeg Elevation pointing offset in degrees
     */
    public void setOrientationAxes_Pointing(double theAzimuthDeg, double theElevationDeg) {
        double azimuth = Trig.degreesToRadians(theAzimuthDeg);
        double elevation = Trig.degreesToRadians(theElevationDeg);

        Vector pointingVector = new VectorFixed(getOrientationAxes(),
                                                new Cartesian(Math.cos(elevation) * Math.sin(azimuth),
                                                              Math.cos(elevation) * Math.cos(azimuth),
                                                              Math.sin(elevation)));

        //* Once again, without more information the reference vector is mostly arbitrary.
        Vector eastVector = new VectorFixed(getOrientationAxes(), new Cartesian(1.0, 0.0, 0.0));

        //* Constraint the Z-Axis to be along the pointing vector, and then tie the X-Axis as closed to our reference vector (Due East)
        // as it can be.
        Axes sensorAxes = new AxesAlignedConstrained(pointingVector, AxisIndicator.THIRD, eastVector, AxisIndicator.FIRST);

        setOrientationAxes(sensorAxes);
    }


    @Override
    public PlatformType getPlatformType() {
        return PlatformType.SENSOR;
    }
}
