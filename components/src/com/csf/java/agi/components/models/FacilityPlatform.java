package com.csf.java.agi.components.models;

import agi.foundation.Trig;
import agi.foundation.celestial.CentralBodiesFacet;
import agi.foundation.celestial.EarthCentralBody;
import agi.foundation.cesium.*;
import agi.foundation.cesium.advanced.IMaterialGraphics;
import agi.foundation.coordinates.Cartesian;
import agi.foundation.coordinates.Cartographic;
import agi.foundation.geometry.AxesNorthEastDown;
import agi.foundation.geometry.PointCartographic;
import java.awt.*;

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
            this.addCesiumGfxLabelExtension(name, labelColor);
            this.addCesiumPointGfxExtension(color, pixelSize);
        }
    }

    @Override
    public PlatformType getPlatformType() {
        return PlatformType.FACILITY;
    }

    public void addCesiumEllipseGfxExtension(Color color) {
        EllipsoidGraphics ellipseGfx = new EllipsoidGraphics();
        ellipseGfx.setFill(new ConstantCesiumProperty<Boolean>(true));
        SolidColorMaterialGraphics material = new SolidColorMaterialGraphics();
        material.setColor(new ConstantCesiumProperty<Color>(color));
        ellipseGfx.setMaterial(new ConstantCesiumProperty<IMaterialGraphics>(material));
        ellipseGfx.setRadii(new ConstantCesiumProperty<Cartesian>(new Cartesian(1000,1000,1000)));

        EllipsoidGraphicsExtension gfxExtension = new EllipsoidGraphicsExtension(ellipseGfx);
        this.getExtensions().add(gfxExtension);
    }

    public void addCesiumGfxLabelExtension(String theLabel, Color color) {
        LabelGraphics gxLabel = new LabelGraphics();
        gxLabel.setText(new ConstantCesiumProperty<String>(theLabel));
        gxLabel.setFillColor(new ConstantCesiumProperty<Color>(color));
        gxLabel.setOutlineColor(new ConstantCesiumProperty<Color>(color));
        gxLabel.setOutlineWidth(new ConstantCesiumProperty<Double>(5.0));
        gxLabel.setHorizontalOrigin(new ConstantCesiumProperty<CesiumHorizontalOrigin>(CesiumHorizontalOrigin.CENTER));
        gxLabel.setVerticalOrigin(new ConstantCesiumProperty<CesiumVerticalOrigin>(CesiumVerticalOrigin.BOTTOM));
        this.getExtensions().add(new LabelGraphicsExtension(gxLabel));
    }

    public void addCesiumPointGfxExtension(Color color, double pixelSize) {
        PointGraphics gxPoint = new PointGraphics();
        gxPoint.setColor(new ConstantCesiumProperty<Color>(color));
        gxPoint.setOutlineColor(new ConstantCesiumProperty<Color>(new Color(0x1e, 0x90, 0xff, 0x5f)));
        gxPoint.setOutlineWidth(new ConstantCesiumProperty<Double>(2.0));
        gxPoint.setPixelSize(new ConstantCesiumProperty<Double>(pixelSize));
        gxPoint.setShow(new ConstantCesiumProperty<Boolean>(true));
        this.getExtensions().add(new PointGraphicsExtension(gxPoint));
    }

    /** Adds Cesium Fan Graphics Extension */
    public void addFanGfxExtension() {
//        FanGraphics gxFan = new FanGraphics();
//        gxFan.setMaterial(new ConstantCesiumProperty<MaterialGraphics>(new SolidColorMaterialGraphics(new Color(0x1e, 0x90, 0xff, 0x5f))));
//        gxFan.setOutlineColor(new ConstantCesiumProperty<Color>(new Color(0x00, 0x00, 0xff, 0xff)));
//        gxFan.setRadius(new ConstantCesiumProperty<Double>(40000.0));
//        this.getExtensions().add(new AzimuthElevationMaskGraphicsExtension(gxFan));
    }

    private void initialize(double latDeg, double lonDeg, double altMeters) {
        double longitude = Trig.degreesToRadians(lonDeg);
        double latitude = Trig.degreesToRadians(latDeg);
        this.setLocationPoint(new PointCartographic(earth, new Cartographic(longitude, latitude, altMeters)));
        this.setOrientationAxes(new AxesNorthEastDown(earth, this.getLocationPoint()));
    }


}
