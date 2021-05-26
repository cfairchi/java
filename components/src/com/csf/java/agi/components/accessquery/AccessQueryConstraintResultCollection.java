package com.csf.java.agi.components.accessquery;

import agi.foundation.time.TimeInterval;
import agi.foundation.time.TimeIntervalCollection;
import com.csf.java.agi.components.enums.Constraint;
import com.google.common.base.Preconditions;

import java.util.EnumMap;
import java.util.Map;


/**
 * Contains AccessQueryConstraintResults and provides functionality to merge these results.
 */
public class AccessQueryConstraintResultCollection {
    private final Map<Constraint, AccessQueryConstraintResult> results =
            new EnumMap<>(Constraint.class);
    private final TimeInterval analysisInterval;

    public AccessQueryConstraintResultCollection(TimeInterval analysisInterval) {
        this.analysisInterval = analysisInterval;
    }

    /**
     * Add an {@link AccessQueryConstraintResult} to this collection.
     */
    public void addResult(
            Constraint constraint, AccessQueryConstraintResult accessQueryConstraintResult) {
        results.put(constraint, accessQueryConstraintResult);
    }

    /**
     * Returns true if there are no {@link AccessQueryConstraintResult} in this collection.
     */
    public boolean isEmpty() {
        return results.isEmpty();
    }

    /**
     * Returns a {@link TimeIntervalCollection} that contains all satisfied intervals from this
     * collection with the intervals from the provided {@link AccessQueryConstraintResultCollection}
     * removed.
     */
    public TimeIntervalCollection getRemainingSatisfiedIntervals(
            AccessQueryConstraintResultCollection collectionToRemove) {
        if (collectionToRemove.results.isEmpty()) {
            return getSatisfiedIntervalIntersection();
        }

        // Intermediate TimeIntervalCollection is needed because TimeIntervalCollection.remove may
        // return non-closed intervals, but we are working all with closed intervals
        TimeIntervalCollection tic = new TimeIntervalCollection();
        tic.addAll(getSatisfiedIntervalIntersection());
        tic.remove(collectionToRemove.getSatisfiedIntervalIntersection());
        TimeIntervalCollection remainingIntervals = new TimeIntervalCollection();
        for (TimeInterval interval : tic) {
            remainingIntervals.add(new TimeInterval(interval.getStart(), interval.getStop()));
        }
        return remainingIntervals;
    }

    /**
     * Provides the intersection of all satisfied intervals in this collection.
     */
    public TimeIntervalCollection getSatisfiedIntervalIntersection() {
        Preconditions.checkNotNull(analysisInterval);
        TimeIntervalCollection allSatisfiedIntervals = new TimeIntervalCollection();
        if (results.isEmpty()) {
            return allSatisfiedIntervals;
        }

        allSatisfiedIntervals.add(analysisInterval);
        for (AccessQueryConstraintResult result : results.values()) {
            allSatisfiedIntervals = allSatisfiedIntervals.intersect(result.getSatisfiedIntervals());
        }

        return allSatisfiedIntervals;
    }
}
