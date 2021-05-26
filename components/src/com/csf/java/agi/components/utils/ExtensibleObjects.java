package com.csf.java.agi.components.utils;

import agi.foundation.TypeLiteral;
import agi.foundation.cesium.*;
import agi.foundation.infrastructure.ExtensibleObject;
import agi.foundation.infrastructure.ExtensibleObjectCollection;
import agi.foundation.infrastructure.IdentifierExtension;
import agi.foundation.platforms.AzimuthElevationMaskExtension;
import agi.foundation.platforms.FieldOfViewExtension;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Utility class for ExtensibleObject operations.
 */
public final class ExtensibleObjects {

    /**
     * Returns the string of the {@link IdentifierExtension} from the provided {@link
     * ExtensibleObject}. Throws {@link IllegalArgumentException} if an {@link IdentifierExtension} is
     * not found.
     */
    public static String getIdentifierExtensionValueFrom(ExtensibleObject object) {
        Optional<IdentifierExtension> idExtension = getIdentifierExtension(object);
        checkArgument(idExtension.isPresent());
        return idExtension.get().getIdentifier();
    }

    /**
     * Removes existing {@link IdentifierExtension} if present from provided {@link ExtensibleObject}
     * and adds new IdentifierExtension with provided id.
     */
    public static void addOrReplaceIdentifierExtension(ExtensibleObject extensibleObject, String id) {
        // Removes existing extension if it exists.
        Optional<IdentifierExtension> originalExt = getIdentifierExtension(extensibleObject);
        if (originalExt.isPresent()) {
            extensibleObject.getExtensions().remove(originalExt.get());
        }
        extensibleObject.getExtensions().add(new IdentifierExtension(id));
    }

    /**
     * Removes existing {@link DescriptionExtension} if present from the provided {@link
     * ExtensibleObject} and adds a new DescriptionExtension with the provided text.
     */
    public static void addOrReplaceDescriptionExtension(
            ExtensibleObject extensibleObject, String text) {
        // Removes existing extension if it exists.
        Optional<DescriptionExtension> originalExt = getDescriptionExtension(extensibleObject);
        if (originalExt.isPresent()) {
            extensibleObject.getExtensions().remove(originalExt.get());
        }

        Description description = new Description();
        description.setText(new ConstantCesiumProperty<>(text));
        extensibleObject.getExtensions().add(new DescriptionExtension(description));
    }

    /**
     * Builds a object that is otherwise empty except an identifier and CZML deletion extension.
     */
    public static ExtensibleObjectCollection buildDeletionObjectsFor(String identifier) {
        ExtensibleObjectCollection extensibleObjectCollection = new ExtensibleObjectCollection();
        ExtensibleObject deletionObject = new ExtensibleObject();
        deletionObject.getExtensions().add(new IdentifierExtension(identifier));
        deletionObject.getExtensions().add(new CesiumDeleteExtension());
        extensibleObjectCollection.add(deletionObject);
        return extensibleObjectCollection;
    }

    /**
     * Returns {@link IdentifierExtension} from provided {@link ExtensibleObject} if present.
     */
    public static Optional<IdentifierExtension> getIdentifierExtension(
            ExtensibleObject extensibleObject) {
        return Optional.ofNullable(
                extensibleObject.getExtensions().getByType(new TypeLiteral<IdentifierExtension>() {
                }));
    }

    /**
     * Returns {@link PointGraphicsExtension} from provided {@link ExtensibleObject} if present.
     */
    public static Optional<PointGraphicsExtension> getPointGraphicsExtension(
            ExtensibleObject extensibleObject) {
        return Optional.ofNullable(
                extensibleObject.getExtensions().getByType(new TypeLiteral<PointGraphicsExtension>() {
                }));
    }

    /**
     * Returns {@link DescriptionExtension} from provided {@link ExtensibleObject} if present.
     */
    public static Optional<DescriptionExtension> getDescriptionExtension(
            ExtensibleObject extensibleObject) {
        return Optional.ofNullable(
                extensibleObject.getExtensions().getByType(new TypeLiteral<DescriptionExtension>() {
                }));
    }

    /**
     * Returns {@link LabelGraphicsExtension} from provided {@link ExtensibleObject} if present.
     */
    public static Optional<LabelGraphicsExtension> getLabelGraphicsExtension(
            ExtensibleObject extensibleObject) {
        return Optional.ofNullable(
                extensibleObject.getExtensions().getByType(new TypeLiteral<LabelGraphicsExtension>() {
                }));
    }

    /**
     * Returns {@link PathGraphicsExtension} from provided {@link ExtensibleObject} if present.
     */
    public static Optional<PathGraphicsExtension> getPathGraphicsExtension(
            ExtensibleObject extensibleObject) {
        return Optional.ofNullable(
                extensibleObject.getExtensions().getByType(new TypeLiteral<PathGraphicsExtension>() {
                }));
    }

    /**
     * Returns {@link ModelGraphicsExtension} from provided {@link ExtensibleObject} if present.
     */
    public static Optional<ModelGraphicsExtension> getModelGraphicsExtension(
            ExtensibleObject extensibleObject) {
        return Optional.ofNullable(
                extensibleObject.getExtensions().getByType(new TypeLiteral<ModelGraphicsExtension>() {
                }));
    }

    /**
     * Returns {@link AzimuthElevationMaskGraphicsExtension} from provided {@link ExtensibleObject} if
     * present.
     */
    public static Optional<AzimuthElevationMaskGraphicsExtension>
    getAzimuthElevationMaskGraphicsExtension(ExtensibleObject extensibleObject) {
        return Optional.ofNullable(
                extensibleObject
                        .getExtensions()
                        .getByType(new TypeLiteral<AzimuthElevationMaskGraphicsExtension>() {
                        }));
    }

    /**
     * Returns {@link AzimuthElevationMaskExtension} from provided {@link ExtensibleObject} if
     * present.
     */
    public static Optional<AzimuthElevationMaskExtension> getAzimuthElevationMaskExtension(
            ExtensibleObject extensibleObject) {
        return Optional.ofNullable(
                extensibleObject
                        .getExtensions()
                        .getByType(new TypeLiteral<AzimuthElevationMaskExtension>() {
                        }));
    }

    /**
     * Returns {@link FieldOfViewExtension} from provided {@link ExtensibleObject} if present.
     */
    public static Optional<FieldOfViewExtension> getFieldOfViewExtension(
            ExtensibleObject extensibleObject) {
        return Optional.ofNullable(
                extensibleObject.getExtensions().getByType(new TypeLiteral<FieldOfViewExtension>() {
                }));
    }

    /**
     * Returns {@link FieldOfViewGraphicsExtension} from provided {@link ExtensibleObject} if present.
     */
    public static Optional<FieldOfViewGraphicsExtension> getFieldOfViewGraphicsExtension(
            ExtensibleObject extensibleObject) {
        return Optional.ofNullable(
                extensibleObject
                        .getExtensions()
                        .getByType(new TypeLiteral<FieldOfViewGraphicsExtension>() {
                        }));
    }

    /**
     * Returns {@link LinkGraphicsExtension} from provided {@link ExtensibleObject} if present.
     */
    public static Optional<LinkGraphicsExtension> getLinkGraphicsExtension(
            ExtensibleObject extensibleObject) {
        return Optional.ofNullable(
                extensibleObject.getExtensions().getByType(new TypeLiteral<LinkGraphicsExtension>() {
                }));
    }

    private ExtensibleObjects() {
    }
}
