package com.csf.java.agi.components.models.access;

import agi.foundation.time.TimeInterval;
import com.csf.java.agi.components.enums.Accessibility;
import com.csf.java.agi.components.enums.Constraint;

import java.time.ZonedDateTime;
import java.util.*;

/**
 * Represents a time interval with a related set of constraints and an {@link Accessibility}
 * classification
 */
public class ConstrainedInterval implements Comparable<ConstrainedInterval> {

    private final Set<Constraint> constraints = new HashSet<>();
    private final Accessibility accessibility;
    private final TimeInterval interval;
    private final ZonedDateTime start;
    private final ZonedDateTime stop;

    private final Map<Accessibility, Integer> accessibilityRank = new HashMap<>();

    public ConstrainedInterval(
            Accessibility accessibility, Set<Constraint> constraints, TimeInterval interval) {
        this.constraints.addAll(constraints);
        this.accessibility = accessibility;
        this.interval = interval;
        this.start = interval.getStart().toDateTime();
        this.stop = interval.getStop().toDateTime();

        accessibilityRank.put(Accessibility.ACCESS_UNKNOWN, 0);
        accessibilityRank.put(Accessibility.NO_ACCESS, 1);
        accessibilityRank.put(Accessibility.ACCESS_EXISTS, 2);
        accessibilityRank.put(Accessibility.ACCESS_MARGINAL, 3);
    }

    public boolean isDegenerate() {
        return start.equals(stop);
    }

    public List<ConstrainedInterval> merge(ConstrainedInterval intervalToMerge) {
        List<ConstrainedInterval> mergedIntervals = new ArrayList<>();

        // If they don't intersect return both
        if (this.getInterval().intersect(intervalToMerge.getInterval()).getIsEmpty()) {
            mergedIntervals.add(this);
            mergedIntervals.add(intervalToMerge);
            return mergedIntervals;
        }
        if (fullyContains(intervalToMerge)) {
            return mergeContainingInterval(this, intervalToMerge);
        }

        if (intervalToMerge.fullyContains(this)) {
            return mergeContainingInterval(intervalToMerge, this);
        }

        if (startIsBeforeOrEqual(intervalToMerge.getStart())) {
            return mergeOverlappingIntervals(this, intervalToMerge);
        } else {
            return mergeOverlappingIntervals(intervalToMerge, this);
        }
    }

    private List<ConstrainedInterval> mergeOverlappingIntervals(
            ConstrainedInterval first, ConstrainedInterval second) {
        List<ConstrainedInterval> mergedIntervals = new ArrayList<>();

        ConstrainedInterval newConstrainedInterval =
                new ConstrainedInterval(
                        first.getAccessibility(),
                        first.getConstraints(),
                        new TimeInterval(first.getInterval().getStart(), second.getInterval().getStart()));
        if (!newConstrainedInterval.isDegenerate()) {
            mergedIntervals.add(newConstrainedInterval);
        }

        Set<Constraint> combinedConstraints = new HashSet<>();
        if (first.accessibility.equals(second.getAccessibility())) {
            combinedConstraints.addAll(second.getConstraints());
            combinedConstraints.addAll(first.getConstraints());
        } else {
            combinedConstraints.addAll(second.getConstraints());
        }

        Accessibility combinedAccessibility =
                (accessibilityRank.get(first.getAccessibility())
                        < accessibilityRank.get(second.accessibility))
                        ? first.getAccessibility()
                        : second.getAccessibility();

        newConstrainedInterval =
                new ConstrainedInterval(
                        combinedAccessibility,
                        (Accessibility.ACCESS_EXISTS.equals(combinedAccessibility))
                                ? new HashSet<>()
                                : combinedConstraints,
                        new TimeInterval(second.getInterval().getStart(), first.getInterval().getStop()));
        if (!newConstrainedInterval.isDegenerate()) {
            mergedIntervals.add(newConstrainedInterval);
        }

        newConstrainedInterval =
                new ConstrainedInterval(
                        second.getAccessibility(),
                        second.getConstraints(),
                        new TimeInterval(first.getInterval().getStop(), second.getInterval().getStop()));
        if (!newConstrainedInterval.isDegenerate()) {
            mergedIntervals.add(newConstrainedInterval);
        }

        return mergedIntervals;
    }

    private List<ConstrainedInterval> mergeContainingInterval(
            ConstrainedInterval outsideInterval, ConstrainedInterval insideInterval) {
        List<ConstrainedInterval> mergedIntervals = new ArrayList<>();

        ConstrainedInterval newConstrainedInterval =
                new ConstrainedInterval(
                        outsideInterval.getAccessibility(),
                        outsideInterval.getConstraints(),
                        new TimeInterval(
                                outsideInterval.getInterval().getStart(), insideInterval.getInterval().getStart()));

        if (!newConstrainedInterval.isDegenerate()) {
            mergedIntervals.add(newConstrainedInterval);
        }

        Set<Constraint> combinedConstraints = new HashSet<>();
        if (outsideInterval.accessibility.equals(insideInterval.getAccessibility())) {
            combinedConstraints.addAll(insideInterval.getConstraints());
            combinedConstraints.addAll(outsideInterval.getConstraints());
        } else {
            combinedConstraints.addAll(insideInterval.getConstraints());
        }

        Accessibility combinedAccessibility =
                (accessibilityRank.get(outsideInterval.getAccessibility())
                        < accessibilityRank.get(insideInterval.accessibility))
                        ? outsideInterval.getAccessibility()
                        : insideInterval.getAccessibility();

        newConstrainedInterval =
                new ConstrainedInterval(
                        combinedAccessibility,
                        (Accessibility.ACCESS_EXISTS.equals(combinedAccessibility))
                                ? new HashSet<>()
                                : combinedConstraints,
                        new TimeInterval(
                                insideInterval.getInterval().getStart(), insideInterval.getInterval().getStop()));
        if (!newConstrainedInterval.isDegenerate()) {
            mergedIntervals.add(newConstrainedInterval);
        }

        newConstrainedInterval =
                new ConstrainedInterval(
                        outsideInterval.getAccessibility(),
                        outsideInterval.getConstraints(),
                        new TimeInterval(
                                insideInterval.getInterval().getStop(), outsideInterval.getInterval().getStop()));

        if (!newConstrainedInterval.isDegenerate()) {
            mergedIntervals.add(newConstrainedInterval);
        }

        return mergedIntervals;
    }

    private boolean fullyContains(ConstrainedInterval interval) {
        return startIsBeforeOrEqual(interval.getStart()) && stopIsAfterOrEqual(interval.getStop());
    }

    private boolean startIsBeforeOrEqual(ZonedDateTime dt) {
        return start.isBefore(dt) || start.equals(dt);
    }

    private boolean stopIsAfterOrEqual(ZonedDateTime dt) {
        return stop.isAfter(dt) || stop.equals(dt);
    }

    public TimeInterval getInterval() {
        return interval;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public ZonedDateTime getStop() {
        return stop;
    }

    public Set<Constraint> getConstraints() {
        return constraints;
    }

    public Accessibility getAccessibility() {
        return accessibility;
    }

    @Override
    public int compareTo(ConstrainedInterval otherInterval) {
        return this.start.compareTo(otherInterval.start);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ConstrainedInterval) {
            ConstrainedInterval that = (ConstrainedInterval) object;
            return this.constraints.equals(that.constraints)
                    && this.accessibility.equals(that.accessibility)
                    && this.interval.equals(that.interval);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(constraints, accessibility, interval, start, stop);
    }
}
