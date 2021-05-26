package com.csf.java.agi.components.models.platforms;

import com.csf.java.agi.components.utils.ExtensionGenerator;

import java.awt.*;
import java.util.Optional;

public class RectangularSensor extends SensorPlatform {
    public RectangularSensor(String name, double inTrackDeg, double crossTrackDeg, Optional<Color> color) {
        super(name, color);
        ExtensionGenerator.addRectangularFovExtension(this, inTrackDeg, crossTrackDeg);
    }

}
