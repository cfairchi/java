package com.csf.java.agi.components.models.access;

import com.csf.java.agi.components.enums.Constraint;
import com.csf.java.agi.components.enums.NoAccessReason;

public class NoAccessResult {
    private final NoAccessReason noAccessReason;
    private final Constraint constraint;

    public NoAccessResult(NoAccessReason noAccessReason, Constraint constraint) {
        this.noAccessReason = noAccessReason;
        this.constraint = constraint;
    }

    public NoAccessReason getNoAccessReason() {
        return noAccessReason;
    }

    public Constraint getConstraint() {
        return constraint;
    }


}
