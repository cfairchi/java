package com.csf.java.agi.components.models;

import agi.foundation.MotionEvaluator1;
import agi.foundation.celestial.CentralBodiesFacet;
import agi.foundation.celestial.EarthCentralBody;
import agi.foundation.cesium.*;
import agi.foundation.propagators.CartesianOnePointPropagator;
import com.google.common.collect.ImmutableMap;

import java.awt.*;
import java.util.HashMap;

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
