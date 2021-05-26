package com.csf.java.agi.components.accessquery;


import agi.foundation.access.AccessQueryResult;
import agi.foundation.time.TimeInterval;
import agi.foundation.time.TimeInterval1;
import agi.foundation.time.TimeIntervalCollection1;
import com.csf.java.agi.components.enums.Accessibility;
import com.csf.java.agi.components.enums.AccessibilityLevel;
import com.csf.java.agi.components.enums.Constraint;
import com.csf.java.agi.components.enums.SatisfactionLevel;
import com.csf.java.agi.components.models.access.AccessInterval;
import com.csf.java.agi.components.models.access.AccessibilityWithConstraints;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides functionality to combine multiple access query results into one non-overlapping sorted
 * list of {@Link AccessInterval} objects
 */
public class AccessQueryResultConjunction {
    private final AccessQueryConstraintResultCollection marginalResults;
    private final AccessQueryConstraintResultCollection strongResults;
    private final TimeInterval analysisInterval;
    private final String transmitterModelId;
    private final MergeAccessIntervalCallback mergeTimeIntervalDataCallback =
            new MergeAccessIntervalCallback();

    private final TimeIntervalCollection1<AccessibilityWithConstraints> mergedTimeIntervalCollection =
            new TimeIntervalCollection1<>();

    public AccessQueryResultConjunction(
            final TimeInterval analysisInterval, String transceiverModelId) {
        this.analysisInterval = analysisInterval;
        this.transmitterModelId = transceiverModelId;
        Preconditions.checkArgument(analysisInterval.getIsStartIncluded());
        Preconditions.checkArgument(analysisInterval.getIsStopIncluded());
        marginalResults = new AccessQueryConstraintResultCollection(analysisInterval);
        strongResults = new AccessQueryConstraintResultCollection(analysisInterval);
    }

    /**
     * Adds the provided {@link AccessQueryResult} and {@link Constraint} to the {@link
     * AccessQueryConstraintResultCollection} of marginal results. Also, merges the {@link
     * AccessQueryResult} into maintained list of merged intervals
     */
    public void addMarginalResult(AccessQueryResult result, Constraint constraint) {
        addResult(result, constraint, SatisfactionLevel.MARGINAL, marginalResults);
    }

    /**
     * Adds the provided {@link AccessQueryResult} and {@link Constraint} to the {@link
     * AccessQueryConstraintResultCollection} of strong results. Also, merges the {@link
     * AccessQueryResult} into maintained list of merged intervals
     */
    public void addStrongResult(AccessQueryResult result, Constraint constraint) {
        addResult(result, constraint, SatisfactionLevel.STRONG, strongResults);
    }

    /**
     * Returns {@link ArrayList} of {@link AccessInterval} objects. Intervals are ordered by start
     * time, and there should be no overlapping intervals.
     */
    public ArrayList<AccessInterval> getOrderedAccessIntervals() {
        ArrayList<AccessInterval> accessIntervals = new ArrayList<>();

        mergedTimeIntervalCollection.forEach(
                interval -> {
                    accessIntervals.add(
                            new AccessInterval(new TimeInterval1<>(interval.getStart(),
                                    interval.getStop(),
                                    interval.getData(),
                                    true,
                                    true),
                                    AccessibilityWithConstraints.getAccessibility(interval.getData()),
                                    generateNoAccessReasonsForTimeInterval(interval)));

                });

        return accessIntervals;
    }

    /**
     * Returns true if there are no marginal or strong satisfied intervals
     */
    public boolean satisfiedIntervalsAreEmpty() {
        return getMarginalResults().getSatisfiedIntervalIntersection().isEmpty()
                && getStrongResults().getSatisfiedIntervalIntersection().isEmpty();
    }

    public TimeInterval getAnalysisInterval() {
        return analysisInterval;
    }

    public AccessQueryConstraintResultCollection getMarginalResults() {
        return marginalResults;
    }

    public AccessQueryConstraintResultCollection getStrongResults() {
        return strongResults;
    }

    private void addResult(
            AccessQueryResult result,
            Constraint constraint,
            SatisfactionLevel satisfactionLevel,
            AccessQueryConstraintResultCollection constraintCollection) {
        AccessQueryConstraintResult constraintResult =
                new AccessQueryConstraintResult(constraint, result, analysisInterval);

        // Add result to appropriate marginal/strong collection
        constraintCollection.addResult(constraint, constraintResult);

        TimeIntervalCollection1<AccessibilityWithConstraints> newIntervals =
                new TimeIntervalCollection1<>();

        // Add all the UNKNOWN intervals to newIntervals
        newIntervals.add(
                constraintResult.getUnknownIntervals(),
                AccessibilityWithConstraints.create(
                        ImmutableSet.of(constraint), AccessibilityLevel.UNKNOWN, satisfactionLevel));

        // Add all the UNSATISFIED intervals to newIntervals
        newIntervals.add(
                constraintResult.getUnsatisfiedIntervals(),
                AccessibilityWithConstraints.create(
                        ImmutableSet.of(constraint), AccessibilityLevel.UNSATISFIED, satisfactionLevel));

        // Add all the SATISFIED intervals to newIntervals
        newIntervals.add(
                constraintResult.getSatisfiedIntervals(),
                AccessibilityWithConstraints.create(
                        ImmutableSet.of(constraint), AccessibilityLevel.SATISFIED, satisfactionLevel));

        synchronized (mergedTimeIntervalCollection) {
            // Merge all the newIntervals with the existing mergedIntervals
            mergedTimeIntervalCollection.addMergingData(newIntervals, mergeTimeIntervalDataCallback);
        }
    }

    private Set<Constraint> generateNoAccessReasonsForTimeInterval(
            TimeInterval1<AccessibilityWithConstraints> timeInterval) {

        if (Accessibility.ACCESS_EXISTS.equals(
                AccessibilityWithConstraints.getAccessibility(timeInterval.getData()))) {
            return new HashSet<>();
        }

        return timeInterval.getData().getConstraints();
    }

//  /** This class and this function assume that all intervals contain closed start/end times. */
//  private static Time.TimeInterval fromAgiInterval(
//      TimeInterval1<AccessibilityWithConstraints> agiInterval) {
//    Preconditions.checkNotNull(agiInterval);
//    TimeInterval1<AccessibilityWithConstraints> interval = agiInterval;
//    Preconditions.checkArgument(agiInterval.getIsStartIncluded());
//    Preconditions.checkArgument(agiInterval.getIsStopIncluded());
//
//    return Time.TimeInterval.newBuilder()
//        .setStartTime(DateTimes.buildSerializableDateTime(interval.getStart()))
//        .setEndTime(DateTimes.buildSerializableDateTime(interval.getStop()))
//        .build();
//  }
}
