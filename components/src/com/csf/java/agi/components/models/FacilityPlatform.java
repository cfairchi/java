package com.csf.java.agi.components.models;

import agi.foundation.Trig;
import agi.foundation.celestial.CentralBodiesFacet;
import agi.foundation.celestial.EarthCentralBody;
import agi.foundation.cesium.*;
import agi.foundation.cesium.advanced.IMaterialGraphics;
import agi.foundation.coordinates.Cartesian;
import agi.foundation.coordinates.Cartographic;
import agi.foundation.coordinates.Rectangular;
import agi.foundation.geometry.AxesNorthEastDown;
import agi.foundation.geometry.PointCartographic;
import agi.foundation.infrastructure.IdentifierExtension;
import agi.foundation.platforms.Platform;

import java.awt.*;

/** This class extends the Platform object to provide an easy to use implementation of a Facility */
public class FacilityPlatform extends CustomPlatform {
    EarthCentralBody earth = CentralBodiesFacet.getFromContext().getEarth();

    /**
     * @param theName   Facility Name
     * @param theLatDeg Latitude in degrees of the facility location
     * @param theLonDeg Longitude in degrees of the facility location
     * @param theHeight Height in meters of the facility location
     */
    public FacilityPlatform(String theName, double theLatDeg, double theLonDeg, double theHeight) {
        super(theName);
        initialize(theName, theLatDeg, theLonDeg, theHeight);
    }

    /**
     * @param theName      Facility Name
     * @param theLatDeg    Latitude in degrees of the facility location
     * @param theLonDeg    Longitude in degrees of the facility location
     * @param theHeight    Height in meters of the facility location
     * @param theColor     If not null a Cesium Label graphics extension and point graphics extension are automatically generated
     * @param thePixelSize Pixel size of the cesium point graphics extension
     */
    public FacilityPlatform(String theName, double theLatDeg, double theLonDeg, double theHeight, Color theColor, Color theLabelColor, double thePixelSize) {
        super(theName);
        initialize(theName, theLatDeg, theLonDeg, theHeight);
        if (theColor != null) {
            this.addGfxLabelExtension(theName, theLabelColor);
            this.addPointGfxExtension(theColor, thePixelSize);
        }
    }

    public void addEllipseGfxExtension(Color theColor) {
        EllipsoidGraphics ellipseGfx = new EllipsoidGraphics();
        ellipseGfx.setFill(new ConstantCesiumProperty<Boolean>(true));
        SolidColorMaterialGraphics material = new SolidColorMaterialGraphics();
        material.setColor(new ConstantCesiumProperty<Color>(theColor));
        ellipseGfx.setMaterial(new ConstantCesiumProperty<IMaterialGraphics>(material));
        ellipseGfx.setRadii(new ConstantCesiumProperty<Cartesian>(new Cartesian(1000,1000,1000)));

        EllipsoidGraphicsExtension gfxExtension = new EllipsoidGraphicsExtension(ellipseGfx);
        this.getExtensions().add(gfxExtension);
    }
    /**
     * Adds Cesium Graphics Label Extension
     * @param theLabel The Label text
     */
    public void addGfxLabelExtension(String theLabel, Color theColor) {
        LabelGraphics gxLabel = new LabelGraphics();
        gxLabel.setText(new ConstantCesiumProperty<String>(theLabel));
        gxLabel.setFillColor(new ConstantCesiumProperty<Color>(theColor));
        gxLabel.setOutlineColor(new ConstantCesiumProperty<Color>(theColor));
        gxLabel.setOutlineWidth(new ConstantCesiumProperty<Double>(5.0));
        gxLabel.setHorizontalOrigin(new ConstantCesiumProperty<CesiumHorizontalOrigin>(CesiumHorizontalOrigin.CENTER));
        gxLabel.setVerticalOrigin(new ConstantCesiumProperty<CesiumVerticalOrigin>(CesiumVerticalOrigin.BOTTOM));
        this.getExtensions().add(new LabelGraphicsExtension(gxLabel));
    }

    /**
     * Adds Cesium Point Graphics Extension
     * @param theColor     The Point color
     * @param thePixelSize The desired pixel size of the point
     */
    public void addPointGfxExtension(Color theColor, double thePixelSize) {
        PointGraphics gxPoint = new PointGraphics();
        gxPoint.setColor(new ConstantCesiumProperty<Color>(theColor));
        gxPoint.setOutlineColor(new ConstantCesiumProperty<Color>(new Color(0x1e, 0x90, 0xff, 0x5f)));
        gxPoint.setOutlineWidth(new ConstantCesiumProperty<Double>(2.0));
        gxPoint.setPixelSize(new ConstantCesiumProperty<Double>(thePixelSize));
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

    private void initialize(String theName, double theLatDeg, double theLonDeg, double theHeight) {
        this.getExtensions().add(new IdentifierExtension("/Scenario/Facility/" + theName));
        double longitude = Trig.degreesToRadians(theLonDeg);
        double latitude = Trig.degreesToRadians(theLatDeg);
        this.setLocationPoint(new PointCartographic(earth, new Cartographic(longitude, latitude, theHeight)));
        //this.setOrientationAxes(Axes.getRoot());
        this.setOrientationAxes(new AxesNorthEastDown(earth, this.getLocationPoint()));
    }

    @Override
    public PlatformType getPlatformType() {
        return PlatformType.FACILITY;
    }
}
