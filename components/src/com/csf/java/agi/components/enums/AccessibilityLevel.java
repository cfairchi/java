package com.csf.java.agi.components.enums;

public enum AccessibilityLevel {
    UNKNOWN("Unknown"),
    SATISFIED("Satisfied"),
    UNSATISFIED("Unsatisfied");

    private final String name;

    AccessibilityLevel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
