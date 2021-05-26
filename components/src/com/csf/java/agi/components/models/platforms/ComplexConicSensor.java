package com.csf.java.agi.components.models.platforms;

import com.csf.java.agi.components.utils.ExtensionGenerator;

import java.awt.*;
import java.util.Optional;

public class ComplexConicSensor extends SensorPlatform {
    public ComplexConicSensor(String name, double innerHalfAngle, double outerHalfAngle, double minClockAngle, double maxClockAngle,
                              Optional<Color> color) {
        super(name, color);
        ExtensionGenerator.addComplexConicFovExtension(this, innerHalfAngle, outerHalfAngle, minClockAngle, maxClockAngle);
    }


}
