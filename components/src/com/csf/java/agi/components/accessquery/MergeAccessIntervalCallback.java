package com.csf.java.agi.components.accessquery;

import agi.foundation.time.MergeTimeIntervalDataCallback;
import com.csf.java.agi.components.enums.AccessibilityLevel;
import com.csf.java.agi.components.enums.Constraint;
import com.csf.java.agi.components.enums.SatisfactionLevel;
import com.csf.java.agi.components.models.access.AccessibilityWithConstraints;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.HashSet;
import java.util.Set;

class MergeAccessIntervalCallback
        extends MergeTimeIntervalDataCallback<AccessibilityWithConstraints> {


    private static final ImmutableMap<AccessibilityLevel, Integer> ACCESSIBILITY_RANKING =
            ImmutableMap.<AccessibilityLevel, Integer>builder()
                    .put(AccessibilityLevel.UNKNOWN, 0)
                    .put(AccessibilityLevel.UNSATISFIED, 1)
                    .put(AccessibilityLevel.SATISFIED, 2)
                    .build();

    private static final ImmutableMap<SatisfactionLevel, Integer> SATISFACTION_RANKING =
            ImmutableMap.<SatisfactionLevel, Integer>builder()
                    .put(SatisfactionLevel.MARGINAL, 0)
                    .put(SatisfactionLevel.STRONG, 1)
                    .build();

    @Override
    public AccessibilityWithConstraints invoke(
            AccessibilityWithConstraints first, AccessibilityWithConstraints second) {

        Set<Constraint> allConstraints = new HashSet<>();
        allConstraints.addAll(first.getConstraints());
        allConstraints.addAll(second.getConstraints());

        // Same Accessibility level
        if (first.getAccessibilityLevel().equals(second.getAccessibilityLevel())) {
            if (first.getSatisfactionLevel().equals(second.getSatisfactionLevel())) {
                return AccessibilityWithConstraints.create(
                        ImmutableSet.copyOf(allConstraints),
                        first.getAccessibilityLevel(),
                        first.getSatisfactionLevel());
            }

            // Same AccessibilityLevel, different satisfaction level
            SatisfactionLevel newSatisfactionLevel =
                    (SATISFACTION_RANKING.get(first.getSatisfactionLevel())
                            > SATISFACTION_RANKING.get(second.getSatisfactionLevel()))
                            ? first.getSatisfactionLevel()
                            : second.getSatisfactionLevel();
            return AccessibilityWithConstraints.create(
                    ImmutableSet.copyOf(allConstraints), first.getAccessibilityLevel(), newSatisfactionLevel);
        }

        // Different AccessibilityLevel, same SatisfactionLevel
        if (ACCESSIBILITY_RANKING.get(first.getAccessibilityLevel())
                < ACCESSIBILITY_RANKING.get(second.getAccessibilityLevel())) {

            AccessibilityLevel newAccessibilityLevel = first.getAccessibilityLevel();
            SatisfactionLevel newSatisfactionLevel = first.getSatisfactionLevel();

            if (SatisfactionLevel.STRONG.equals(newSatisfactionLevel)
                    && AccessibilityLevel.UNSATISFIED.equals(newAccessibilityLevel)) {
                newAccessibilityLevel = AccessibilityLevel.SATISFIED;
                newSatisfactionLevel = SatisfactionLevel.MARGINAL;
            }

            return AccessibilityWithConstraints.create(
                    ImmutableSet.copyOf(first.getConstraints()), newAccessibilityLevel, newSatisfactionLevel);
        } else {
            AccessibilityLevel newAccessibilityLevel = second.getAccessibilityLevel();
            SatisfactionLevel newSatisfactionLevel = second.getSatisfactionLevel();

            if (SatisfactionLevel.STRONG.equals(newSatisfactionLevel)
                    && AccessibilityLevel.UNSATISFIED.equals(newAccessibilityLevel)) {
                newAccessibilityLevel = AccessibilityLevel.SATISFIED;
                newSatisfactionLevel = SatisfactionLevel.MARGINAL;
            }

            return AccessibilityWithConstraints.create(
                    ImmutableSet.copyOf(second.getConstraints()), newAccessibilityLevel, newSatisfactionLevel);
        }
    }
}
