package com.csf.java.agi.components.accessquery;

import agi.foundation.access.AccessQueryResult;
import agi.foundation.time.TimeInterval;
import agi.foundation.time.TimeIntervalCollection;
import com.csf.java.agi.components.enums.Constraint;


/**
 * Contains results from one AccessQuery tied to a constraint
 */
public class AccessQueryConstraintResult {
    private final Constraint constraint;
    private final TimeIntervalCollection unknownIntervals = new TimeIntervalCollection();
    private final TimeIntervalCollection unsatisfiedIntervals = new TimeIntervalCollection();
    private final TimeIntervalCollection satisfiedIntervals;

    public AccessQueryConstraintResult(
            Constraint constraint, AccessQueryResult accessQueryResult, TimeInterval analysisInterval) {
        this.constraint = constraint;

        satisfiedIntervals = accessQueryResult.getSatisfactionIntervals();

        addIntervalsToCollection(accessQueryResult.getUnknownIntervals(), unknownIntervals);

        TimeIntervalCollection satisfiedAndUnknownIntervals =
                new TimeIntervalCollection(satisfiedIntervals);
        satisfiedAndUnknownIntervals.add(unknownIntervals);

        addIntervalsToCollection(
                satisfiedAndUnknownIntervals.complement(
                        analysisInterval.getStart(), analysisInterval.getStop()),
                unsatisfiedIntervals);
    }

    private static void addIntervalsToCollection(
            TimeIntervalCollection collectionToAdd, TimeIntervalCollection newCollection) {

        for (TimeInterval interval : collectionToAdd) {
            if (!interval.getIsStartIncluded() || !interval.getIsStopIncluded()) {
                newCollection.add(new TimeInterval(interval.getStart(), interval.getStop(), true, true));
            } else {
                newCollection.add(interval);
            }
        }
    }

    public Constraint getConstraint() {
        return constraint;
    }

    public TimeIntervalCollection getUnknownIntervals() {
        return unknownIntervals;
    }

    public TimeIntervalCollection getUnsatisfiedIntervals() {
        return unsatisfiedIntervals;
    }

    public TimeIntervalCollection getSatisfiedIntervals() {
        return satisfiedIntervals;
    }
}
