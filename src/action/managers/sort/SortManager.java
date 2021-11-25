package action.managers.sort;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SortManager {
    public static class SortCriteria<T> implements Comparator<T> {
        private final List<Comparator<T>> comparators;
        private final boolean sortAscending;

        @SafeVarargs
        public SortCriteria(boolean sortAscending, Comparator<T>... comparators) {
            this.comparators = Arrays.stream(comparators).toList();
            this.sortAscending = sortAscending;
        }

        @Override
        public int compare(T obj1, T obj2) {
            for (Comparator<T> comparator : comparators) {
                int ordering = sortAscending ? 1 : -1;
                int result = ordering * comparator.compare(obj1, obj2);
                if (result != 0) {
                    return result;
                }
            }

            return 0;
        }
    }

    public static <T> List<T> sortByCriteria(List<T> list, SortCriteria<T> criteria) {
        return list.stream()
                .sorted(criteria)
                .toList();
    }
}
