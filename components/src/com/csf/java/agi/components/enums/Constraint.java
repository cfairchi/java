package com.csf.java.agi.components.enums;

public enum Constraint {
    LINE_OF_SIGHT("Line of Sight"),
    UNKNOWN("Unknown");

    private final String name;

    Constraint(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
