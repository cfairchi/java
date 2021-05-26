package com.csf.java.agi.components.utils;

import agi.foundation.time.JulianDate;
import agi.foundation.time.TimeInterval;
import com.google.common.collect.BoundType;
import com.google.common.collect.Range;

import java.time.Instant;

/**
 * A helper class for {@link TimeInterval}s.
 */
public final class TimeIntervals {

    private TimeIntervals() {
    }

    /**
     * Builds and returns a {@link TimeInterval} equivalent to the given {@link Range<Instant>}.
     */
    public static TimeInterval from(Range<Instant> instantRange) {
        if (!instantRange.hasLowerBound() && !instantRange.hasUpperBound()) {
            return TimeInterval.getInfinite();
        }
        if (!instantRange.hasLowerBound()) {
            return new TimeInterval(
                    JulianDate.getMinValue(),
                    JulianDates.from(instantRange.upperEndpoint()),
                    /* isStartIncluded= */ true,
                    instantRange.upperBoundType() == BoundType.CLOSED);
        }
        if (!instantRange.hasUpperBound()) {
            return new TimeInterval(
                    JulianDates.from(instantRange.lowerEndpoint()),
                    JulianDate.getMaxValue(),
                    instantRange.lowerBoundType() == BoundType.CLOSED,
                    /* isStopIncluded= */ true);
        }
        return new TimeInterval(
                JulianDates.from(instantRange.lowerEndpoint()),
                JulianDates.from(instantRange.upperEndpoint()),
                instantRange.lowerBoundType() == BoundType.CLOSED,
                instantRange.upperBoundType() == BoundType.CLOSED);
    }

    /**
     * Builds and returns a {@code Range<Instant>} equivalent to the given {@link TimeInterval}.
     */
    public static Range<Instant> toInstantRange(TimeInterval timeInterval) {
        // The endpoints of TimeInterval.getInfinite() and TimeInterval.getEmpty() throw
        // ArgumentOutOfRangeException exceptions from within JulianDate.toDateTime() (see b/20227070),
        // so they are treated as special cases.
        if (timeInterval.equals(TimeInterval.getInfinite())) {
            return Range.all();
        }
        if (timeInterval.getIsEmpty()) {
            return Range.closedOpen(Instant.EPOCH, Instant.EPOCH);
        }

        return Range.range(
                timeInterval.getStart().toDateTime().toInstant(),
                timeInterval.getIsStartIncluded() ? BoundType.CLOSED : BoundType.OPEN,
                timeInterval.getStop().toDateTime().toInstant(),
                timeInterval.getIsStopIncluded() ? BoundType.CLOSED : BoundType.OPEN);
    }
}
