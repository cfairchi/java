package com.csf.java.agi.components.models;

import java.util.ArrayList;
import java.util.List;

public enum PlatformType {
    UNKNOWN("Unknown", 0),
    SATELLITE("Satellite", 1),
    SENSOR("Sensor", 2);

    private final String displayName;
    private final Integer order;

    PlatformType(String displayName, Integer order) {
        this.displayName = displayName;
        this.order = order;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Integer getOrder() {
        return order;
    }

    public static List<String> displayNames() {
        List<String> displayNames = new ArrayList<>();
        for (PlatformType level : PlatformType.values()) {
            displayNames.add(level.displayName);
        }
        return displayNames;
    }
}
