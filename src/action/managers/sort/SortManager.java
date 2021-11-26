package action.managers.sort;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public final class SortManager {
    private SortManager() { }

    /**
     * Class used by the SortManager, holding the comparators and the ordering.
     * @param <T> the type of object to apply the comparators to
     */
    public static final class SortCriteria<T> implements Comparator<T> {
        private final List<Comparator<T>> comparators;
        private final boolean sortAscending;

        /**
         * Constructor fot rhe sort criteria.
         * @param sortAscending the ordering of the elements
         * @param comparators the comparators of the objects
         */
        @SafeVarargs
        public SortCriteria(final boolean sortAscending, final Comparator<T>... comparators) {
            this.comparators = Arrays.stream(comparators).toList();
            this.sortAscending = sortAscending;
        }

        /**
         * Iterates through each comparator and compares the two given objects. If the
         * result of a comparator is, at any point, not equal to 0, returns the current
         * result. Otherwise, continues to the next comparator.
         * @param obj1 the first object
         * @param obj2 the second object
         * @return the final result of the comparisons
         */
        @Override
        public int compare(final T obj1, final T obj2) {
            // Go through each comparator
            for (Comparator<T> comparator : comparators) {
                // Get the ordering and the result of the comparator
                int ordering = sortAscending ? 1 : -1;
                int result = ordering * comparator.compare(obj1, obj2);

                // If the objects are 'equal', go to the next comparator
                if (result != 0) {
                    return result;
                }
            }

            // The objects are 'equal' in terms of the given comparators
            return 0;
        }
    }

    /**
     * Sorts the given list by the given criteria.
     * @param listToSort the list to be sorted
     * @param criteria the criteria to sort by
     * @param <T> the type of the compared objects
     * @return the sorted list
     */
    public static <T> List<T> sortByCriteria(final List<T> listToSort,
                                             final SortCriteria<T> criteria) {
        return listToSort.stream()
                .sorted(criteria)
                .toList();
    }
}
