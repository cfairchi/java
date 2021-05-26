package com.csf.java.agi.components.models.platforms;

import agi.foundation.IServiceProvider;
import agi.foundation.TypeLiteral;
import agi.foundation.access.AccessConstraint;
import agi.foundation.access.LinkInstantaneous;
import agi.foundation.access.LinkRole;
import agi.foundation.cesium.ConstantCesiumProperty;
import agi.foundation.cesium.Description;
import agi.foundation.cesium.DescriptionExtension;
import agi.foundation.cesium.advanced.CesiumProperty;
import agi.foundation.communications.antennas.BaseGainPattern;
import agi.foundation.coordinates.Cartesian;
import agi.foundation.coordinates.UnitCartesian;
import agi.foundation.geometry.Axes;
import agi.foundation.geometry.AxesTargetingLink;
import agi.foundation.geometry.VectorFixed;
import agi.foundation.geometry.shapes.SensorFieldOfView;
import agi.foundation.infrastructure.CopyContext;
import agi.foundation.infrastructure.IdentifierExtension;
import com.csf.java.agi.components.models.access.ConstraintWithReason;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import agi.foundation.platforms.Platform;
import java.util.Set;

/**
 * Models an antenna as a {@link Platform} with a {@link BaseGainPattern} and constraints.
 */
public final class Antenna extends CustomPlatform {
    private final String id;
    private final BaseGainPattern gainPattern;
    private final Axes bodyAxes;
    private final Set<ConstraintWithReason> atLeastMarginalConstraints;
    private final Set<ConstraintWithReason> strongAccessConstraints;
    private final Set<SensorFieldOfView> obstructions;
    private final boolean isTargetable;


    /**
     * @param gainPattern  The antenna {@link BaseGainPattern}.
     * @param bodyAxes     The body {@link Axes} of the platform on which the antenna is mounted.
     * @param isTargetable Specifies whether or not the antenna can be pointed at a target.
     */
    public Antenna(String id, BaseGainPattern gainPattern, Axes bodyAxes, boolean isTargetable) {
        super(id);
        this.id = id;
        this.gainPattern = gainPattern;
        this.bodyAxes = bodyAxes;
        this.isTargetable = isTargetable;
        atLeastMarginalConstraints = Sets.newConcurrentHashSet();
        strongAccessConstraints = Sets.newConcurrentHashSet();
        obstructions = Sets.newConcurrentHashSet();
        this.getExtensions().add(new IdentifierExtension(id));
    }

    protected Antenna(Antenna existingInstance, CopyContext context) {
        super(existingInstance, context);
        this.id = existingInstance.id;
        // BaseGainPattern implementations included with STK do not implement IThreadAware, so
        // if the passed context is CopyForAnotherThread, then context.updateReference will return a
        // direct reference the existing gain pattern rather than a new copy of the gain pattern.
        gainPattern = context.updateReference(existingInstance.getGainPattern());

        // This ensures that cloned antennas have independent body axes so that changing the
        // orientation of one antenna does not change the orientation of its clone. However, we must
        // still allow the CopyContext the opportunity to update the reference before the reference is
        // copied explicitly. See the Javadoc explanation from AGI at https://goo.gl/y8tMP5.
        Axes bodyAxesReference = context.updateReference(existingInstance.getBodyAxes());
        if (bodyAxesReference == existingInstance.getBodyAxes()) {
            bodyAxesReference = (Axes) existingInstance.getBodyAxes().clone(context);
        }
        bodyAxes = bodyAxesReference;

        // Esnure that this antenna's constraints are independent from existingInstance.
        atLeastMarginalConstraints =
                cloneConstraints(existingInstance.getAtLeastMarginalConstraints(), context);
        strongAccessConstraints =
                cloneConstraints(existingInstance.getStrongAccessConstraints(), context);

        // These member variables are thread safe and do not implement the ICloneWithContext interface.
        obstructions = context.updateReference(existingInstance.getObstructions());
        isTargetable = context.updateReference(existingInstance.isTargetable());
        context.addObjectMapping(existingInstance, this);
    }

    @Override
    public PlatformType getPlatformType() {
        return PlatformType.ANTENNA;
    }

    @Override
    public final Object clone(CopyContext context) {
        return new Antenna(this, context);
    }

    /**
     * Returns the antenna gain pattern.
     */
    public BaseGainPattern getGainPattern() {
        return gainPattern;
    }

    /**
     * Returns the antenna constraints that must be satisfied for at-least marginal access.
     */
    public Set<ConstraintWithReason> getAtLeastMarginalConstraints() {
        return atLeastMarginalConstraints;
    }

    /**
     * Returns the antenna constraints for strong access.
     */
    public Set<ConstraintWithReason> getStrongAccessConstraints() {
        return strongAccessConstraints;
    }

    /**
     * Returns the field of regard obstructions as an {@link ImmutableSet}.
     */
    public ImmutableSet<SensorFieldOfView> getObstructions() {
        return ImmutableSet.copyOf(obstructions);
    }

    public void addObstruction(SensorFieldOfView obstruction) {
        obstructions.add(obstruction);
    }

    /**
     * Returns {@code true} if the antenna can be pointed/steered towards a target.
     */
    public boolean isTargetable() {
        return isTargetable;
    }

    /**
     * Returns the body {@link Axes} of the platform on which the antenna is mounted.
     */
    public Axes getBodyAxes() {
        return bodyAxes;
    }

    /**
     * Orients the antenna so that its Z-axis is aligned along a link to the target {@link Antenna},
     * and its X-axis is constrained toward that vector.
     *
     * @param target The target.
     */
    public void setTarget(IServiceProvider target) {
        Preconditions.checkState(isTargetable);
        LinkInstantaneous link = new LinkInstantaneous(this, target);
        VectorFixed localXAxis =
                new VectorFixed(bodyAxes, Cartesian.toCartesian(UnitCartesian.getUnitX()));
        setOrientationAxes(new AxesTargetingLink(link, LinkRole.TRANSMITTER, localXAxis));
    }

    /**
     * Returns the constant text value of the description extension if it exists
     */
    public String getConstantDescriptionText() {
        DescriptionExtension descriptionExtension =
                getExtensions().getByType(new TypeLiteral<DescriptionExtension>() {
                });
        if (descriptionExtension == null) {
            return "";
        }
        CesiumProperty<String> descProp = descriptionExtension.getDescription().getText();
        if (descProp instanceof ConstantCesiumProperty) {
            ConstantCesiumProperty<String> txt = (ConstantCesiumProperty<String>) descProp;
            return txt.getValue();
        }
        return "";
    }



    public String getId() {
        return id;
    }

    /**
     * Sets the description extension to be a constant value containing the provided text
     */
    public void setConstantDescriptionText(String text) {
        DescriptionExtension descriptionExtension =
                getExtensions().getByType(new TypeLiteral<DescriptionExtension>() {
                });
        if (descriptionExtension != null) {
            getExtensions().remove(descriptionExtension);
        }
        Description description = new Description();
        description.setText(new ConstantCesiumProperty<>(text));
        getExtensions().add(new DescriptionExtension(description));
    }

    /**
     * Helper method to clone constraints from an existing source so that setting the constrained link
     * end or constrained link does not affect the clone.
     */
    private static Set<ConstraintWithReason> cloneConstraints(
            Set<ConstraintWithReason> source, CopyContext context) {
        Set<ConstraintWithReason> output = Sets.newConcurrentHashSet();
        for (ConstraintWithReason existing : source) {
            AccessConstraint constraintReference = context.updateReference(existing.constraint());
            if (constraintReference == existing.constraint()) {
                constraintReference = (AccessConstraint) existing.constraint().clone(context);
            }
            output.add(ConstraintWithReason.create(constraintReference, existing.noAccessReason()));
        }
        return output;
    }
}
