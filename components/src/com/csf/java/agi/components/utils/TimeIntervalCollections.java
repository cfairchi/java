package com.csf.java.agi.components.utils;

import agi.foundation.time.*;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;

import java.time.Instant;
import java.util.Collection;

/**
 * A helper class for {@code TimeIntervalCollection}s.
 */
public final class TimeIntervalCollections {

    private TimeIntervalCollections() {
    }

    /**
     * Builds and returns a {@code RangeSet<Instant>} equivalent to the given {@link
     * TimeIntervalCollection}.
     */
    public static RangeSet<Instant> toInstantRangeSet(TimeIntervalCollection timeIntervalCollection) {
        RangeSet<Instant> rangeSet = TreeRangeSet.create();
        for (TimeInterval timeInterval : timeIntervalCollection) {
            rangeSet.add(TimeIntervals.toInstantRange(timeInterval));
        }
        return rangeSet;
    }

    /**
     * Builds and returns a {@link TimeIntervalCollection} equivalent to the given {@code
     * RangeSet<Instant>}.
     */
    public static TimeIntervalCollection from(RangeSet<Instant> rangeSet) {
        TimeIntervalCollection timeIntervalCollection = new TimeIntervalCollection();
        for (Range<Instant> range : rangeSet.asRanges()) {
            timeIntervalCollection.add(TimeIntervals.from(range));
        }
        return timeIntervalCollection;
    }

    /**
     * Returns true if the provided {@link TimeIntervalCollection} contains the provided {@link
     * TimeInterval}.
     */
    public static boolean contains(
            TimeIntervalCollection timeIntervalCollection, TimeInterval interval) {
        for (TimeInterval currentInterval : timeIntervalCollection) {
            if (currentInterval.contains(interval.getStart())
                    && currentInterval.contains(interval.getStop())) {
                return true;
            }
        }
        return false;
    }

    /*
     * Maps a TimeIntervalCollection to a TimeIntevalCollection1<Boolean> where true means the input
     * had that time interval and false means it was missing
     */
    public static TimeIntervalCollection1<Boolean> complement(
            TimeIntervalCollection in, JulianDate from, JulianDate to) {
        TimeIntervalCollection complement = in.complement(from, to);
        TimeIntervalCollection1<Boolean> out = new TimeIntervalCollection1<Boolean>();
        for (TimeInterval on : in) {
            out.add(new TimeInterval1<Boolean>(on.getStart(), on.getStop(), true, false, true));
        }
        for (TimeInterval off : complement) {
            out.add(new TimeInterval1<Boolean>(off.getStart(), off.getStop(), false, false, true));
        }
        return out;
    }

    public static TimeInterval getBoundaries(Collection<TimeIntervalCollection> all) {
        JulianDate start = JulianDate.getMaxValue();
        JulianDate end = JulianDate.getMinValue();
        for (TimeIntervalCollection collection : all) {
            if (!collection.getStart().isIdentical(JulianDate.getMinValue())
                    && JulianDate.lessThan(collection.getStart(), start)) {
                start = collection.getStart();
            }
            if (!collection.getStop().isIdentical(JulianDate.getMaxValue())
                    && JulianDate.lessThan(end, collection.getStop())) {
                end = collection.getStop();
            }
        }
        return new TimeInterval(start, end);
    }

    public static <T> TimeInterval getBoundaries1(Collection<TimeIntervalCollection1<T>> all) {
        JulianDate start = JulianDate.getMaxValue();
        JulianDate end = JulianDate.getMinValue();
        for (TimeIntervalCollection1<T> collection : all) {
            if (!collection.getStart().isIdentical(JulianDate.getMinValue())
                    && JulianDate.lessThan(collection.getStart(), start)) {
                start = collection.getStart();
            }
            if (!collection.getStop().isIdentical(JulianDate.getMaxValue())
                    && JulianDate.lessThan(end, collection.getStop())) {
                end = collection.getStop();
            }
        }
        return new TimeInterval(start, end);
    }

    public static TimeIntervalCollection of(TimeInterval... intervals) {
        TimeIntervalCollection out = new TimeIntervalCollection();
        for (int i = 0; i < intervals.length; i++) {
            out.add(intervals[i]);
        }
        return out;
    }

    /**
     * Will remove the first elements in the collection until it reaches the maxSize
     *
     * @return true if elements were removed from the collection
     */
    public static boolean cropBeginningToSize(TimeIntervalCollection collection, int maxSize) {
        if (collection.size() <= maxSize) {
            return false;
        }
        while (collection.size() > maxSize) {
            collection.remove(0);
        }
        return true;
    }

    /**
     * Will remove the first elements in the collection until it reaches the maxSize
     *
     * @return true if elements were removed from the collection
     */
    public static <T> boolean cropBeginningToSize(
            TimeIntervalCollection1<T> collection, int maxSize) {
        if (collection.size() <= maxSize) {
            return false;
        }
        while (collection.size() > maxSize) {
            collection.remove(0);
        }
        return true;
    }
}
