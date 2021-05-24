package com.csf.java.models;

public class Tle {
    private final String name;
    private final String line1;
    private final String line2;
    private final String sscNum;

    public Tle(String name, String line1, String line2) {
        this.name = name.trim();
        this.line1 = line1;
        this.line2 = line2;
        this.sscNum = line1.substring(2, 7);
    }

    public String getName() {
        return name;
    }

    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
    }

    public String getSscNum() {
        return sscNum;
    }

}
