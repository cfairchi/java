package com.csf.java.agi.components.enums;

public enum SatisfactionLevel {
    MARGINAL("Marginal"),
    STRONG("Strong");

    private final String name;

    SatisfactionLevel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
