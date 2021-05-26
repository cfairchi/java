package com.csf.java.agi.components.enums;

public enum Accessibility {
    ACCESS_UNKNOWN("Unknown"),
    ACCESS_EXISTS("Marginal"),
    ACCESS_MARGINAL("Satisfied"),
    NO_ACCESS("Unsatisfied");

    private final String name;

    Accessibility(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
