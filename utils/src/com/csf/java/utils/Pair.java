package com.csf.java.utils;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;

public class Pair<A, B> implements Serializable {

    /**
     * Creates a new pair containing the given elements in order.
     */
    public static <A, B> Pair<A, B> of(A first, B second) {
        return new Pair<>(first, second);
    }

    /**
     * The first element of the pair.
     */
    public final A first;

    /**
     * The second element of the pair.
     */
    public final B second;

    /**
     * For subclass usage only. To create a new pair, use {@code Pair.of(first, second)}.
     */
    protected Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Returns the first element of this pair.
     */
    public A getFirst() {
        return first;
    }

    /**
     * Returns the second element of this pair.
     */
    public B getSecond() {
        return second;
    }

    /**
     * Returns a function that yields {@link #first}.
     */
    @SuppressWarnings("unchecked") // implementation is "fully variant"
    public static <A, B> Function<Pair<A, B>, A> firstFunction() {
        return (Function) PairFirstFunction.INSTANCE;
    }

    /**
     * Returns a function that yields {@link #second}.
     */
    @SuppressWarnings("unchecked") // implementation is "fully variant"
    public static <A, B> Function<Pair<A, B>, B> secondFunction() {
        return (Function) PairSecondFunction.INSTANCE;
    }

    /*
     * If we use the enum singleton pattern for these functions, Flume's type
     * inference chokes: http://b/4863010
     */

    /*
     * Note that this implementation doesn't involve B, and A's are only "passed
     * through", so it has become "fully variant" in both parameters.
     */
    private static final class PairFirstFunction<A, B>
            implements Function<Pair<A, B>, A>, Serializable {
        static final PairFirstFunction<Object, Object> INSTANCE = new PairFirstFunction<>();

        @Override
        public A apply(Pair<A, B> from) {
            return from.getFirst();
        }

        private Object readResolve() {
            return INSTANCE;
        }
    }

    /*
     * Note that this implementation doesn't involve A, and B's are only "passed
     * through", so it has become "fully variant" in both parameters.
     */
    private static final class PairSecondFunction<A, B>
            implements Function<Pair<A, B>, B>, Serializable {
        static final PairSecondFunction<Object, Object> INSTANCE = new PairSecondFunction<>();

        @Override
        public B apply(Pair<A, B> from) {
            return from.getSecond();
        }

        private Object readResolve() {
            return INSTANCE;
        }
    }

    /**
     * Returns a comparator that compares two Pair objects by comparing the result of {@link
     * #getFirst()} for each.
     */
    @SuppressWarnings("unchecked") // safe contravariant cast
    public static <A extends Comparable<? super A>, B> Comparator<Pair<A, B>> compareByFirst() {
        return (Comparator) FirstComparator.FIRST_COMPARATOR;
    }

    /**
     * Returns a comparator that compares two Pair objects by comparing the result of {@link
     * #getSecond()} for each.
     */
    @SuppressWarnings("unchecked") // safe contravariant cast
    public static <A, B extends Comparable<? super B>> Comparator<Pair<A, B>> compareBySecond() {
        return (Comparator) SecondComparator.SECOND_COMPARATOR;
    }

    private enum FirstComparator implements Comparator<Pair<Comparable<?>, Object>> {
        FIRST_COMPARATOR;

        @Override
        public int compare(Pair<Comparable<?>, Object> pair1, Pair<Comparable<?>, Object> pair2) {
            @SuppressWarnings("unchecked")
            Comparable<Object> left = (Comparable<Object>) pair1.getFirst();
            Comparable<?> right = pair2.getFirst();

            /*
             * Technically unsafe, but tolerable. If the comparables are badly
             * behaved, this comparator will be equally badly behaved.
             */
            int result = left.compareTo(right);
            return result;
        }
    }

    private enum SecondComparator implements Comparator<Pair<Object, Comparable<?>>> {
        SECOND_COMPARATOR;

        @Override
        public int compare(Pair<Object, Comparable<?>> pair1, Pair<Object, Comparable<?>> pair2) {
            @SuppressWarnings("unchecked")
            Comparable<Object> left = (Comparable<Object>) pair1.getSecond();
            Comparable<?> right = pair2.getSecond();

            /*
             * Technically unsafe, but tolerable. If the comparables are badly
             * behaved, this comparator will be equally badly behaved.
             */
            int result = left.compareTo(right);
            return result;
        }
    }

    @Override
    public boolean equals(Object object) {
        // if (object != null && object.getClass() == getClass()) {
        if (object instanceof Pair) {
            Pair<?, ?> that = (Pair<?, ?>) object;
            return Objects.equals(this.getFirst(), that.getFirst())
                    && Objects.equals(this.getSecond(), that.getSecond());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash1 = first == null ? 0 : first.hashCode();
        int hash2 = second == null ? 0 : second.hashCode();
        return 31 * hash1 + hash2;
    }


    @Override
    public String toString() {
        return "(" + getFirst() + ", " + getSecond() + ")";
    }

    private static final long serialVersionUID = 747826592375603043L;
}
