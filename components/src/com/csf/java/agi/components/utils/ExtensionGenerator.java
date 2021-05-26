package com.csf.java.agi.components.utils;

import agi.foundation.Bounds;
import agi.foundation.Trig;
import agi.foundation.TypeLiteral;
import agi.foundation.access.LinkInstantaneous;
import agi.foundation.cesium.*;
import agi.foundation.cesium.advanced.CesiumProperty;
import agi.foundation.coordinates.Rectangular;
import agi.foundation.geometry.shapes.ComplexConic;
import agi.foundation.geometry.shapes.SensorFieldOfView;
import agi.foundation.infrastructure.CopyContext;
import agi.foundation.infrastructure.ExtensibleObject;
import agi.foundation.infrastructure.IdentifierExtension;
import agi.foundation.infrastructure.ObjectExtension;
import agi.foundation.platforms.AzimuthElevationMaskExtension;
import agi.foundation.platforms.FieldOfViewExtension;
import agi.foundation.platforms.Platform;
import agi.foundation.time.Duration;
import agi.foundation.time.TimeIntervalCollection1;

import com.csf.java.agi.components.models.platforms.Antenna;
import com.google.common.collect.ImmutableSet;

import java.awt.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Provides helper functions to remove, create and update graphics extensions
 */
public class ExtensionGenerator {
    private ExtensionGenerator() {
    }

    /**
     * If an existing {@link PointGraphicsExtension} is present on the provided {@link
     * ExtensibleObject} it is removed. A new PointGraphicsExtension is then added to the provided
     * ExtensibleObject.
     */
    public static void updateOrAddPointGraphicsExtension(
            ExtensibleObject extensibleObject, Color color, double pixelSize) {

        // Removes existing extension if it exists.
        Optional<ObjectExtension> originalExt =
                Optional.ofNullable(
                        extensibleObject
                                .getExtensions()
                                .getByType(new TypeLiteral<PointGraphicsExtension>() {
                                }));
        if (originalExt.isPresent()) {
            extensibleObject.getExtensions().remove(originalExt.get());
        }

        PointGraphics platformPoint = new PointGraphics();
        platformPoint.setColor(new ConstantCesiumProperty<>(color));
        platformPoint.setPixelSize(new ConstantCesiumProperty<>(pixelSize));
        platformPoint.setShow(new ConstantCesiumProperty<>(true));
        platformPoint.setOutlineWidth(new ConstantCesiumProperty<>(0.0));
        platformPoint.setOutlineColor(new ConstantCesiumProperty<>(Colors.NODE_HIGHLIGHT));
        extensibleObject.getExtensions().add(new PointGraphicsExtension(platformPoint));
    }

    public static void updateOrAddDescriptionExtension(
            ExtensibleObject extensibleObject, String text) {
        // Removes existing extension if it exists.
        Optional<DescriptionExtension> originalExt =
                Optional.ofNullable(
                        extensibleObject.getExtensions().getByType(new TypeLiteral<DescriptionExtension>() {
                        }));
        if (originalExt.isPresent()) {
            extensibleObject.getExtensions().remove(originalExt.get());
        }

        Description description = new Description();
        description.setText(new ConstantCesiumProperty<>(text));
        extensibleObject.getExtensions().add(new DescriptionExtension(description));
    }

    /**
     * If an existing {@link LabelGraphicsExtension} is present on the provided {@link
     * ExtensibleObject} it is removed. A new LabelGraphicsExtension is then added to the
     * providedExtensibleObject.
     */
    public static void updateOrAddLabelExtension(
            ExtensibleObject extensibleObject,
            String text,
            Color color,
            Optional<Color> backgroundColor,
            Optional<String> cssFont) {

        // Removes existing extension if it exists.
        Optional<LabelGraphicsExtension> originalExt =
                Optional.ofNullable(
                        extensibleObject
                                .getExtensions()
                                .getByType(new TypeLiteral<LabelGraphicsExtension>() {
                                }));
        if (originalExt.isPresent()) {
            extensibleObject.getExtensions().remove(originalExt.get());
        }

        // Adds the name of the node.
        LabelGraphics label = new LabelGraphics();
        label.setText(new ConstantCesiumProperty<>(text));
        if (cssFont.isPresent()) {
            label.setFont(new ConstantCesiumProperty<>(cssFont.get()));
        }
        label.setFillColor(new ConstantCesiumProperty<>(color));
        label.setDistanceDisplayCondition(new ConstantCesiumProperty<>(new Bounds(0, 4e6)));
        label.setPixelOffset(new ConstantCesiumProperty<>(new Rectangular(0., 19.)));
        label.setBackgroundPadding(new ConstantCesiumProperty<>(new Rectangular(5, 5)));
        if (backgroundColor.isPresent()) {
            label.setBackgroundColor(new ConstantCesiumProperty<>(backgroundColor.get()));
            label.setShowBackground(new ConstantCesiumProperty<>(true));
        }
        extensibleObject.getExtensions().add(new LabelGraphicsExtension(label));
    }

    /**
     * If an existing {@link PathGraphicsExtension} is present on the provided {@link
     * ExtensibleObject} it is removed. A new PathGraphicsExtension is then added to the provided
     * ExtensibleObject.
     */
    public static void updateOrAddPathGraphics(
            ExtensibleObject extensibleObject,
            Color color,
            Color outlineColor,
            Duration leadDuration,
            Duration trailDuration,
            double width,
            double outlineWidth) {

        // Removes existing extension if it exists.
        Optional<PathGraphicsExtension> originalExt =
                Optional.ofNullable(
                        extensibleObject
                                .getExtensions()
                                .getByType(new TypeLiteral<PathGraphicsExtension>() {
                                }));
        originalExt.ifPresent(pathGraphicsExtension -> extensibleObject.getExtensions().remove(pathGraphicsExtension));

        PathGraphics path = new PathGraphics();
        PolylineOutlineMaterialGraphics pathMaterial = new PolylineOutlineMaterialGraphics();
        pathMaterial.setColor(new ConstantCesiumProperty<>(Colors.makeTransparent(outlineColor, 0x10)));
        pathMaterial.setOutlineWidth(new ConstantCesiumProperty<>(outlineWidth));
        pathMaterial.setOutlineColor(new ConstantCesiumProperty<>(Colors.makeTransparent(color, 0x10)));
        path.setMaterial(new ConstantCesiumProperty<>(pathMaterial));
        path.setWidth(CesiumProperty.toCesiumProperty(width));
        path.setLeadTime(CesiumProperty.toCesiumProperty(leadDuration.getSeconds()));
        path.setTrailTime(CesiumProperty.toCesiumProperty(trailDuration.getSeconds()));

        extensibleObject.getExtensions().add(new PathGraphicsExtension(path));
    }

    /**
     * If an existing {@link ModelGraphicsExtension} is present on the provided {@link
     * ExtensibleObject} it is removed. If the provided {@link URI} is present it is used to add a new
     * ModelGraphicsExtension to the provided ExtensibleObject.
     */
    public static void updateAddOrRemoveModelGraphics(
            ExtensibleObject extensibleObject, Optional<URI> uri) {

        // Removes existing extension if it exists.
        Optional<ModelGraphicsExtension> originialExt =
                Optional.ofNullable(
                        extensibleObject
                                .getExtensions()
                                .getByType(new TypeLiteral<ModelGraphicsExtension>() {
                                }));
        if (originialExt.isPresent()) {
            extensibleObject.getExtensions().remove(originialExt.get());
        }

        if (!uri.isPresent()) {
            return;
        }

        ModelGraphics model = new ModelGraphics();
        model.setModel(
                new ConstantCesiumProperty<>(
                        new CesiumResource(uri.get(), CesiumResourceBehavior.LINK_TO)));
        model.setDistanceDisplayCondition(new ConstantCesiumProperty<>(new Bounds(1e3, 1e4)));
        extensibleObject.getExtensions().add(new ModelGraphicsExtension(model));
    }

    /**
     * If an existing {@link AzimuthElevationMaskGraphicsExtension} is present on the provided {@link
     * ExtensibleObject} it is removed. If the provided ExtensibleObject contains an {@link
     * AzimuthElevationMaskExtension} it is used to add a new AzimuthElevationMaskGraphicsExtension to
     * the provided ExtensibleObject.
     */
    public static void updateAddOrRemoveAzElMaskGraphicsExtension(
            ExtensibleObject extensibleObject,
            double projectionRange,
            int numberOfRings,
            Color fillColor,
            boolean show) {

        // Removes existing extension if it exists.
        Optional<AzimuthElevationMaskGraphicsExtension> originalExt =
                Optional.ofNullable(
                        extensibleObject
                                .getExtensions()
                                .getByType(new TypeLiteral<AzimuthElevationMaskGraphicsExtension>() {
                                }));
        if (originalExt.isPresent()) {
            extensibleObject.getExtensions().remove(originalExt.get());
        }

        if (!show) {
            return;
        }

        // If there is no AzimuthElevationMaskExtension, return without creating graphics.
        if (!Optional.ofNullable(
                extensibleObject
                        .getExtensions()
                        .getByType(new TypeLiteral<AzimuthElevationMaskExtension>() {
                        }))
                .isPresent()) {
            return;
        }

        AzimuthElevationMaskGraphics azElMaskGraphics = new AzimuthElevationMaskGraphics();
        azElMaskGraphics.setFill(new ConstantCesiumProperty<>(true));
        azElMaskGraphics.setMaterial(
                new ConstantCesiumProperty<>(new SolidColorMaterialGraphics(fillColor)));

        // Sets the visualization to have 5 rings that project up to 1km range.
        azElMaskGraphics.setNumberOfRings(new ConstantCesiumProperty<>(numberOfRings));
        azElMaskGraphics.setProjection(
                new ConstantCesiumProperty<>(AzimuthElevationMaskGraphicsProjection.PROJECT_TO_RANGE));
        azElMaskGraphics.setOutlineWidth(new ConstantCesiumProperty<>(1.0));
        azElMaskGraphics.setOutlineColor(new ConstantCesiumProperty<>(fillColor.brighter()));
        azElMaskGraphics.setOutline(new ConstantCesiumProperty<>(true));
        azElMaskGraphics.setProjectionRange(new ConstantCesiumProperty<>(projectionRange));
        azElMaskGraphics.setShow(new ConstantCesiumProperty<>(show));

        extensibleObject
                .getExtensions()
                .add(new AzimuthElevationMaskGraphicsExtension(azElMaskGraphics));
    }

    /**
     * If an existing {@link FieldOfViewGraphicsExtension} is present on the provided {@link
     * ExtensibleObject} it is removed. If the provided ExtensibleObject contains an {@link
     * FieldOfViewExtension} it is used to add a new FieldOfViewGraphicsExtension to the provided
     * ExtensibleObject.
     */
    public static void updateAddOrRemoveFovGraphicsExtension(
            final ExtensibleObject extensibleObject,
            final Color fillColor,
            final double projectionRangeMeters,
            boolean show) {

        Optional<FieldOfViewExtension> fovExt =
                Optional.ofNullable(
                        extensibleObject.getExtensions().getByType(new TypeLiteral<FieldOfViewExtension>() {
                        }));

        if (!fovExt.isPresent() || !show) {
            return;
        }

        // TODO(fairchildc): Currently the fov extensions here are invalid CustomFOVs (they crash
        // cesium).  So for now I am replacing them with conic sensors.  Need to figure out why custom
        // sensors are bad and fix them.
        extensibleObject.getExtensions().remove(fovExt.get());

        // Removes existing extension if it exists.
        Optional<FieldOfViewGraphicsExtension> originalExt =
                Optional.ofNullable(
                        extensibleObject
                                .getExtensions()
                                .getByType(new TypeLiteral<FieldOfViewGraphicsExtension>() {
                                }));
        if (originalExt.isPresent()) {
            extensibleObject.getExtensions().remove(originalExt.get());
        }

        if (!show) {
            return;
        }

        SensorFieldOfView fov =
                new ComplexConic(0, Trig.degreesToRadians(0.5), 0, Trig.degreesToRadians(360));
        fov.setRadius(projectionRangeMeters);
        extensibleObject.getExtensions().add(new FieldOfViewExtension(fov));

        FieldOfViewGraphicsExtension fovGraphicsExtension =
                generateFovGraphicsExtension(fillColor, show);

        extensibleObject.getExtensions().add(fovGraphicsExtension);
    }

    /**
     * If an existing {@link LinkGraphics} is present on the provided {@link LinkInstantaneous} it is
     * removed. If a provided link color or route color is present they are used to add a new {@link
     * LinkGraphicsExtension} to the provided LinkInstantaneous.
     */
    public static void updateAddOrRemoveLinkGraphicsExtension(
            LinkInstantaneous link,
            double outlineWidthPt,
            double lineWidthPt,
            Optional<Color> linkColor,
            Optional<Color> routeColor) {

        // Removes existing extension if it exists.
        Optional<LinkGraphicsExtension> originalExt =
                Optional.ofNullable(
                        link.getExtensions().getByType(new TypeLiteral<LinkGraphicsExtension>() {
                        }));
        if (originalExt.isPresent()) {
            link.getExtensions().remove(originalExt.get());
        }

        // If there is no linkColor or routeColor it is assumed that the link graphics should not be
        // displayed, so a new LinkGraphicsExtension is not added
        if (linkColor.isPresent() || routeColor.isPresent()) {
            // Cesium defaults to color black and line width 1 pt. Width of 0 pt is not supported.
            // Outlines are drawn as a box, with specified width, inside the line.
            LinkGraphics linkGraphics = new LinkGraphics();
            linkGraphics.setWidth(new ConstantCesiumProperty<>(lineWidthPt + outlineWidthPt));
            linkGraphics.setShow(new ConstantCesiumProperty<>(true));
            PolylineOutlineMaterialGraphics line = new PolylineOutlineMaterialGraphics();
            line.setColor(new ConstantCesiumProperty<>(linkColor.orElse(Colors.TRANSPARENT)));
            line.setOutlineColor(new ConstantCesiumProperty<>(routeColor.orElse(Colors.TRANSPARENT)));
            line.setOutlineWidth(new ConstantCesiumProperty<>(outlineWidthPt));
            linkGraphics.setMaterial(new ConstantCesiumProperty<>(line));
            link.getExtensions().add(new LinkGraphicsExtension(linkGraphics));
        }
    }

    /**
     * If an existing {@link LinkGraphics} is present on the provided {@link LinkInstantaneous} it is
     * removed. Provided link colors and route colors are used to add a new {@link
     * LinkGraphicsExtension} to the provided LinkInstantaneous.
     */
    public static void updateAddOrRemoveLinkGraphicsExtension(
            LinkInstantaneous link,
            double outlineWidthPt,
            double lineWidthPt,
            TimeIntervalCollection1<Boolean> showLinks,
            TimeIntervalCollection1<Color> linkColors,
            TimeIntervalCollection1<Color> routeColors) {

        // Removes existing extension if it exists.
        Optional<LinkGraphicsExtension> originalExt =
                Optional.ofNullable(
                        link.getExtensions().getByType(new TypeLiteral<LinkGraphicsExtension>() {
                        }));
        if (originalExt.isPresent()) {
            link.getExtensions().remove(originalExt.get());
        }

        LinkGraphics linkGraphics = new LinkGraphics();
        linkGraphics.setWidth(new ConstantCesiumProperty<>(lineWidthPt + outlineWidthPt));
        linkGraphics.setShow(new TimeIntervalCesiumProperty<Boolean>(showLinks));
        PolylineOutlineMaterialGraphics line = new PolylineOutlineMaterialGraphics();
        line.setColor(new TimeIntervalCesiumProperty<Color>(linkColors));
        line.setOutlineColor(new TimeIntervalCesiumProperty<Color>(routeColors));
        line.setOutlineWidth(new ConstantCesiumProperty<>(outlineWidthPt));
        linkGraphics.setMaterial(new ConstantCesiumProperty<>(line));
        link.getExtensions().add(new LinkGraphicsExtension(linkGraphics));
    }

    /**
     * If provided {@link Antenna} contains obstruction {@link SensorFieldOfView} objects, a new
     * platform is created for each SensorFieldOfView and a {@link FieldOfViewGraphicsExtension} is
     * added to the platform to display the SensorFieldOfView.
     */
    public static List<Platform> generateObstructionPlatformsWithGraphics(
            Antenna antenna, double projectionRange, Color fillColor, boolean show, boolean showLabel) {
        List<Platform> obsPlatforms = new ArrayList<>();
        ImmutableSet<SensorFieldOfView> obsFovs = antenna.getObstructions();

        if (obsFovs.isEmpty() || !show) {
            return obsPlatforms;
        }

        int index = 0;
        // For each obstruction, build a platform to hold the fov graphics.
        for (SensorFieldOfView fov : obsFovs) {
            Platform obstructionPlatform = (Platform) antenna.clone(new CopyContext());

            final String platformId = "_ObstructionPlatform_" + index;
            obstructionPlatform.setName(antenna.getName() + platformId);

            IdentifierExtension idExtension =
                    obstructionPlatform
                            .getExtensions()
                            .getByType(new TypeLiteral<IdentifierExtension>() {
                            });

            idExtension.setIdentifier(idExtension.getIdentifier().concat(platformId));

            obstructionPlatform.getExtensions().clear();
            obstructionPlatform.getExtensions().add(idExtension);

            if (showLabel) {
                LabelGraphics label = new LabelGraphics();
                label.setText(new ConstantCesiumProperty<>(idExtension.getIdentifier()));
                label.setFillColor(new ConstantCesiumProperty<>(Color.BLACK));
                label.setBackgroundColor(new ConstantCesiumProperty<>(Color.GRAY));
                label.setPixelOffset(new ConstantCesiumProperty<>(new Rectangular(0.0, 50.0)));
                obstructionPlatform.getExtensions().add(new LabelGraphicsExtension(label));
            }

            SensorFieldOfView sensorFovToAdd = fov;
            sensorFovToAdd.setRadius(projectionRange);
            obstructionPlatform.getExtensions().add(new FieldOfViewExtension(sensorFovToAdd));
            obstructionPlatform.getExtensions().add(generateFovGraphicsExtension(fillColor, show));
            obsPlatforms.add(obstructionPlatform);
        }
        return obsPlatforms;
    }

    private static FieldOfViewGraphicsExtension generateFovGraphicsExtension(
            Color color, boolean show) {
        SensorFieldOfViewGraphics fovGraphics = new SensorFieldOfViewGraphics();

        // Hides the surface of the sphere/radius projecting outward.
        fovGraphics.setShowDomeSurfaces(new ConstantCesiumProperty<>(false));

        // Sets the lateral surface material graphics.
        SolidColorMaterialGraphics lateralSurfaceMaterial = new SolidColorMaterialGraphics(color);
        fovGraphics.setLateralSurfaceMaterial(new ConstantCesiumProperty<>(lateralSurfaceMaterial));
        fovGraphics.setShow(new ConstantCesiumProperty<>(show));
        return new FieldOfViewGraphicsExtension(fovGraphics);
    }
}
