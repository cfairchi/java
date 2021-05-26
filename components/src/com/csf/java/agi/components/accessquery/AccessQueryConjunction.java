package com.csf.java.agi.components.accessquery;

import agi.foundation.access.AccessQueryResult;

/**
 * The logical conjunction of multiple access queries over a set of time intervals. Any single query
 * being unsatisfied at an instant renders the entire conjunction unsatisfied at that instant. Any
 * single query having an unknown result at an instant renders an otherwise satisfied conjunction
 * unknown at that instant.
 */
final class AccessQueryConjunction {
  /*  private final RangeSet<Instant> unknownIntervals = TreeRangeSet.create();
    private final RangeMap<Instant, ImmutableList<String>> unsatisfiedIntervals =
            TreeRangeMap.create();
    private final RangeSet<Instant> satisfactionIntervals = TreeRangeSet.create();

    *//**
     * Adds the {@link AccessQueryResult} to the conjunction. The human-readable
     * string is appended to any unsatisfied intervals.
     *
     * <p>The {@link AccessQueryResult} must be the result of a query over the same considered
     * intervals as the conjunction.
     *//*
    public void add(
            AccessQueryResult accessQueryResult,
            ImmutableRangeSet<Instant> consideredIntervals,
            String queryNegation) {
        ImmutableRangeSet<Instant> queryUnknownIntervals =
                ImmutableRangeSet.copyOf(
                        TimeIntervalCollections.toInstantRangeSet(accessQueryResult.getUnknownIntervals()));
        ImmutableRangeSet<Instant> querySatisfactionIntervals =
                ImmutableRangeSet.copyOf(
                        TimeIntervalCollections.toInstantRangeSet(
                                accessQueryResult.getSatisfactionIntervals()));
        ImmutableRangeSet<Instant> queryUnsatisfiedIntervals =
                queryUnknownIntervals
                        .union(querySatisfactionIntervals)
                        .complement()
                        .intersection(consideredIntervals);

        // Update unsatisfiedIntervals with the new query results:
        // unsatisfiedIntervals := unsatisfiedIntervals ∪ queryUnsatisfiedIntervals
        ImmutableList<String> queryNegationAsList = ImmutableList.of(queryNegation);
        for (Range<Instant> unsatisfiedInterval : queryUnsatisfiedIntervals.asRanges()) {
            unsatisfiedIntervals.merge(
                    unsatisfiedInterval,
                    queryNegationAsList,
                    (a, b) -> new ImmutableList.Builder<String>().addAll(a).addAll(b).build());
        }

        // Update unknownIntervals with the new query results:
        // unknownIntervals := unknownIntervals ∪ queryUnknownIntervals ∖ queryUnsatisfiedIntervals
        unknownIntervals.addAll(queryUnknownIntervals);
        unknownIntervals.removeAll(queryUnsatisfiedIntervals);

        // Update satisfactionIntervals with the new query results:
        // satisfactionIntervals := (satisfactionIntervals ∪ querySatisfactionIntervals) ∖
        // (queryUnsatisfiedIntervals ∪ queryUnknownIntervals)
        satisfactionIntervals.addAll(querySatisfactionIntervals);
        satisfactionIntervals.removeAll(queryUnsatisfiedIntervals);
        satisfactionIntervals.removeAll(queryUnknownIntervals);
    }

    *//**
     * Returns all queried time intervals over which the result of the conjunction is unknown.
     *//*
    public ImmutableRangeSet<Instant> getUnknownIntervals() {
        return ImmutableRangeSet.copyOf(unknownIntervals);
    }

    *//**
     * Returns all queried time intervals over which the conjunction is unsatisfied.
     *//*
    public ImmutableRangeMap<Instant, ImmutableList<String>> getUnsatisfiedIntervals() {
        return ImmutableRangeMap.copyOf(unsatisfiedIntervals);
    }

    *//**
     * Returns all queried time intervals over which the conjunction is satisfied.
     *//*
    public ImmutableRangeSet<Instant> getSatisfactionIntervals() {
        return ImmutableRangeSet.copyOf(satisfactionIntervals);
    }*/
}
