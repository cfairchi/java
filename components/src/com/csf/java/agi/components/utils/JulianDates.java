package com.csf.java.agi.components.utils;

import agi.foundation.time.JulianDate;

import java.time.Instant;
import java.time.ZoneOffset;

/**
 * A helper class for {@link JulianDate}s.
 */
public final class JulianDates {

    private JulianDates() {
    }

    /**
     * Builds and returns a {@link JulianDate} equivalent to the given {@link Instant} at UTC.
     */
    public static JulianDate from(Instant instant) {
        return new JulianDate(instant.atZone(ZoneOffset.UTC));
    }
}
