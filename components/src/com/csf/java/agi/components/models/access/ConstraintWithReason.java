package com.csf.java.agi.components.models.access;

import agi.foundation.access.AccessConstraint;
import com.csf.java.agi.components.enums.NoAccessReason;

public class ConstraintWithReason {
    private final AccessConstraint constraint;
    private final NoAccessReason noAccessReason;


    public static ConstraintWithReason create(AccessConstraint constraint, NoAccessReason noAccessReason) {
        return new ConstraintWithReason(constraint, noAccessReason);
    }

    public ConstraintWithReason(AccessConstraint constraint, NoAccessReason noAccessReason) {
        this.constraint = constraint;
        this.noAccessReason = noAccessReason;
    }


    public AccessConstraint constraint() {
        return constraint;
    }

    public NoAccessReason noAccessReason() {
        return noAccessReason;
    }
}
