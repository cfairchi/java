package com.csf.java.agi.components.models.access;

import agi.foundation.time.TimeInterval1;
import com.csf.java.agi.components.enums.Accessibility;
import com.csf.java.agi.components.enums.Constraint;
import com.google.common.collect.ImmutableSet;

import java.util.Set;

public class AccessInterval {
    private TimeInterval1<AccessibilityWithConstraints> timeInterval;
    private Accessibility accessibility;
    private final ImmutableSet<Constraint> noAccessReasons;

    public AccessInterval(TimeInterval1<AccessibilityWithConstraints> timeInterval,
                          Accessibility accessibility,
                          Set<Constraint> noAccessReasons) {
        this.timeInterval = timeInterval;
        this.accessibility = accessibility;
        this.noAccessReasons = ImmutableSet.copyOf(noAccessReasons);
    }


    public Accessibility getAccessibility() {
        return accessibility;
    }

    public TimeInterval1<AccessibilityWithConstraints> getTimeInterval() {
        return timeInterval;
    }

    public ImmutableSet<Constraint> getNoAccessReasons() {
        return noAccessReasons;
    }
}
