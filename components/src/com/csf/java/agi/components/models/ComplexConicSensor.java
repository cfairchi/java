package com.csf.java.agi.components.models;

import agi.foundation.Trig;
import agi.foundation.cesium.*;
import agi.foundation.cesium.advanced.IMaterialGraphics;
import agi.foundation.geometry.shapes.ComplexConic;
import agi.foundation.platforms.FieldOfViewExtension;

import java.awt.*;

public class ComplexConicSensor extends SensorPlatform {

    public ComplexConicSensor(String name, double innerHalfAngle, double outerHalfAngle, double minClockAngle, double maxClockAngle, Color color) {
        super(name);
        addComplexConicFovExtensionWithGraphics(innerHalfAngle, outerHalfAngle, minClockAngle, maxClockAngle, color);
    }

    private void addComplexConicFovExtensionWithGraphics(double theInnerHalfAngle, double theOuterHalfAngle, double theMinClockAngle, double theMaxClockAngle, Color theColor) {
        ComplexConic sensorShape = new ComplexConic(
                Trig.degreesToRadians(theInnerHalfAngle), Trig.degreesToRadians(theOuterHalfAngle),
                Trig.degreesToRadians(theMinClockAngle), Trig.degreesToRadians(theMaxClockAngle));
        FieldOfViewExtension fovExtension = new FieldOfViewExtension(sensorShape);
        //if (theRadius > 0) sensorShape.setRadius(theRadius);
        this.getExtensions().add(fovExtension);
        if (theColor != null) addComplexConicGfxExtension(theColor, true);
    }

    private void addComplexConicGfxExtension(Color theColor, Boolean theShowIntersection) {
        SensorFieldOfViewGraphics sFov = new SensorFieldOfViewGraphics();

        Color colorWithAlpha = new Color(theColor.getRed(), theColor.getGreen(), theColor.getBlue(), 50);
        SensorFieldOfViewGraphics sensorFovGfx = new SensorFieldOfViewGraphics();

        GridMaterialGraphics dome = new GridMaterialGraphics();
        dome.setColor(new ConstantCesiumProperty<Color>(colorWithAlpha));

        SolidColorMaterialGraphics lateral = new SolidColorMaterialGraphics();
        lateral.setColor(new ConstantCesiumProperty<Color>(colorWithAlpha));

        sensorFovGfx.setPortionToDisplay(new ConstantCesiumProperty<CesiumSensorVolumePortionToDisplay>(CesiumSensorVolumePortionToDisplay.BELOW_ELLIPSOID_HORIZON));
        sensorFovGfx.setShow(new ConstantCesiumProperty<Boolean>(true));
        sensorFovGfx.setShowIntersection(new ConstantCesiumProperty<Boolean>(theShowIntersection));
        sensorFovGfx.setIntersectionColor(new ConstantCesiumProperty<Color>(colorWithAlpha));
        sensorFovGfx.setDomeSurfaceMaterial(new ConstantCesiumProperty<IMaterialGraphics>(dome));
        sensorFovGfx.setLateralSurfaceMaterial(new ConstantCesiumProperty<IMaterialGraphics>(lateral));
        sensorFovGfx.setEllipsoidHorizonSurfaceMaterial(new ConstantCesiumProperty<IMaterialGraphics>(lateral));
        sensorFovGfx.setEllipsoidSurfaceMaterial(new ConstantCesiumProperty<IMaterialGraphics>(lateral));

        FieldOfViewGraphicsExtension gxFov = new FieldOfViewGraphicsExtension();
        gxFov.setFieldOfViewGraphics(sensorFovGfx);
        this.getExtensions().add(gxFov);
    }




}
