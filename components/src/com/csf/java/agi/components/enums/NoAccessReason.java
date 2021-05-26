package com.csf.java.agi.components.enums;

public enum NoAccessReason {
    MARGINAL("Marginal"),
    STRONG("Strong");

    private final String name;

    NoAccessReason(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
