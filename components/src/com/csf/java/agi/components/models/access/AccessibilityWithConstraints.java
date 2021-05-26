package com.csf.java.agi.components.models.access;

import com.csf.java.agi.components.enums.Accessibility;
import com.csf.java.agi.components.enums.AccessibilityLevel;
import com.csf.java.agi.components.enums.Constraint;
import com.csf.java.agi.components.enums.SatisfactionLevel;
import com.google.common.collect.ImmutableSet;

import java.util.Objects;

/**
 * An accesibility with {@link Constraint}s for that accessibility classification.
 */
public class AccessibilityWithConstraints {
    private final ImmutableSet<Constraint> constraints;
    private final AccessibilityLevel accessibilityLevel;
    private final SatisfactionLevel satisfactionLevel;

    public static Accessibility getAccessibility(
            AccessibilityWithConstraints accessibilityWithConstraints) {
        if (AccessibilityLevel.UNSATISFIED.equals(accessibilityWithConstraints.getAccessibilityLevel())) {
            return Accessibility.NO_ACCESS;
        }

        if (AccessibilityLevel.SATISFIED.equals(accessibilityWithConstraints.getAccessibilityLevel())) {
            if (SatisfactionLevel.STRONG.equals(accessibilityWithConstraints.getSatisfactionLevel())) {
                return Accessibility.ACCESS_EXISTS;
            } else {
                return Accessibility.ACCESS_MARGINAL;
            }
        }
        return Accessibility.ACCESS_UNKNOWN;
    }

    public static AccessibilityWithConstraints create(
            ImmutableSet<Constraint> constraints,
            AccessibilityLevel accessibilityLevel,
            SatisfactionLevel satisfactionLevel) {
        return new AccessibilityWithConstraints(
                constraints, accessibilityLevel, satisfactionLevel);
    }

    public AccessibilityWithConstraints(ImmutableSet<Constraint> constraints,
                                        AccessibilityLevel accessibilityLevel,
                                        SatisfactionLevel satisfactionLevel) {
        this.constraints = constraints;
        this.accessibilityLevel = accessibilityLevel;
        this.satisfactionLevel = satisfactionLevel;
    }

    public AccessibilityLevel getAccessibilityLevel() {
        return accessibilityLevel;
    }

    public SatisfactionLevel getSatisfactionLevel() {
        return satisfactionLevel;
    }

    public ImmutableSet<Constraint> getConstraints() {
        return constraints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccessibilityWithConstraints that = (AccessibilityWithConstraints) o;
        return Objects.equals(constraints, that.constraints) &&
                accessibilityLevel == that.accessibilityLevel &&
                satisfactionLevel == that.satisfactionLevel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(constraints, accessibilityLevel, satisfactionLevel);
    }
}
