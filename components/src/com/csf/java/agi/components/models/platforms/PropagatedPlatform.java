package com.csf.java.agi.components.models.platforms;

import agi.foundation.MotionEvaluator1;
import agi.foundation.propagators.CartesianOnePointPropagator;

public abstract class PropagatedPlatform extends CustomPlatform {
    protected MotionEvaluator1 pointEvaluator = null;
    protected CartesianOnePointPropagator defaultPropagator;

    public PropagatedPlatform(final String theName) {
        super(theName);
    }

    public MotionEvaluator1 getPointEvaluator() {
        if (pointEvaluator == null) {
            pointEvaluator = defaultPropagator.getEvaluator();
        }
        return pointEvaluator;
    }


}
