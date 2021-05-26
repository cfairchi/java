package com.csf.java.agi.components.models.platforms;

import agi.foundation.Trig;
import agi.foundation.cesium.*;
import agi.foundation.cesium.advanced.IMaterialGraphics;
import agi.foundation.geometry.shapes.RectangularPyramid;
import agi.foundation.platforms.FieldOfViewExtension;
import com.csf.java.agi.components.utils.ExtensionGenerator;

import java.awt.*;

public class RectangularSensor extends SensorPlatform {
    public RectangularSensor(String name, double theInTrackDeg, double theCrossTrackDeg, Color theColor) {
        super(name);
        addRectangularFovExtension(theInTrackDeg, theCrossTrackDeg, theColor);
    }

    private void addRectangularFovExtension(double theVerticalInTrackDeg, double theHorizontalCrossTrackDeg, Color theColor) {
        RectangularPyramid fov = new RectangularPyramid(Trig.degreesToRadians(theVerticalInTrackDeg), Trig.degreesToRadians(theHorizontalCrossTrackDeg));
        ExtensionGenerator.updateAddOrRemoveFovGraphicsExtension(this, Color.RED, 1000, true);
        getExtensions().add(new FieldOfViewExtension(fov));
        if (theColor != null) addRectangularGfxExtension(theColor);
    }

    private void addRectangularGfxExtension(Color theColor) {
        Color colorWithAlpha = new Color(theColor.getRed(), theColor.getGreen(), theColor.getBlue(), 50);

        GridMaterialGraphics dome = new GridMaterialGraphics();
        dome.setColor(new ConstantCesiumProperty<Color>(theColor));

        SolidColorMaterialGraphics lateral = new SolidColorMaterialGraphics();
        lateral.setColor(new ConstantCesiumProperty<Color>(theColor));

        SensorFieldOfViewGraphics sensorFovGfx = new SensorFieldOfViewGraphics();
        sensorFovGfx.setDomeSurfaceMaterial(new ConstantCesiumProperty<IMaterialGraphics>(dome));
        sensorFovGfx.setLateralSurfaceMaterial(new ConstantCesiumProperty<IMaterialGraphics>(lateral));

        FieldOfViewGraphicsExtension gxFov = new FieldOfViewGraphicsExtension();
        gxFov.setFieldOfViewGraphics(sensorFovGfx);
        this.getExtensions().add(gxFov);

//        Color colorWithAlpha = new Color(theColor.getRed(), theColor.getGreen(), theColor.getBlue(), 50);
//        RectangularPyramidFieldOfViewGraphics gxFov = new RectangularPyramidFieldOfViewGraphics();
//
//        gxFov.setPortionToDisplay(new ConstantCesiumProperty<CesiumSensorVolumePortionToDisplay>(CesiumSensorVolumePortionToDisplay.COMPLETE));
//        gxFov.setShow(new ConstantCesiumProperty<Boolean>(true));
//
//        ConstantCesiumProperty<MaterialGraphics> sensorMaterial = new ConstantCesiumProperty<MaterialGraphics>(new SolidColorMaterialGraphics(theColor));
//
//
//        gxFov.setLateralSurfaceMaterial(new ConstantCesiumProperty<MaterialGraphics>(new SolidColorMaterialGraphics(colorWithAlpha)));
//        //gxFov.setEllipsoidHorizonSurfaceMaterial(new ConstantCesiumProperty<MaterialGraphics>(new SolidColorMaterialGraphics(test)));
//        gxFov.setShowIntersection(new ConstantCesiumProperty<Boolean>(true));
//        gxFov.setIntersectionColor(new ConstantCesiumProperty<Color>(theColor));
//        this.getExtensions().add(new FieldOfViewGraphicsExtension(gxFov));
    }


}
