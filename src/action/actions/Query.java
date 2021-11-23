package action.actions;

import action.Action;
import action.managers.Filter;
import actor.ActorsAwards;
import entertainment.Genre;
import utils.Utils;

import java.util.List;

public class Query extends Action {
    private static class ActorQuery {
        static void average(Query query) {

        }

        static void awards(Query query) {

        }

        static void filterDescription(Query query) {

        }
    }

    private static class VideoQuery {
        static void rating(Query query) {

        }

        static void favourite(Query query) {

        }

        static void longest(Query query) {

        }

        static void mostViewed(Query query) {

        }
    }

    private static class UserQuery {
        static void numberOfRatings(Query query) {

        }
    }

    public enum Type {
        AVERAGE,
        AWARDS,
        FILTER_DESCRIPTION,
        RATING,
        FAVOURITE,
        LONGEST,
        MOST_VIEWED,
        NUMBER_OF_RATINGS
    }

    public enum SortType {
        ASCENDING,
        DESCENDING
    }

    private Type type;
    private int number;
    private SortType sortType;
    private Filter filter;

    private Filter createFilter(String yearStr, String genreStr, List<String> words, List<String> awardsStr) {
        // Parse the strings, if they exist
        int year = (yearStr == null) ? -1 : Integer.parseInt(yearStr);
        Genre genre = Utils.stringToGenre(genreStr);
        List<ActorsAwards> awards =
                awardsStr == null
                ? null
                : awardsStr.stream()
                        .map(Utils::stringToAwards) // Turn the strings into awards;
                        .toList();

        // Check which filter constructor to use
        if (year != -1 || genre != null) {
            return new Filter(year, genre);
        } else if (words != null || awards != null) {
            return new Filter(words, awards);
        }

        // If there are no filters, return nothing
        return null;
    }

    public Query(Integer id, Type type, int number, List<List<String>> filter, SortType sortType) {
        super(id);
        this.type = type;
        this.number = number;
        this.sortType = sortType;

        // Stop if there are no filters
        if (filter == null) {
            return;
        }

        // Create the filter
        this.filter = createFilter(
                filter.get(0).get(0), // Get the year string
                filter.get(1).get(0), // Get the genre string
                filter.get(2),        // Get the words list
                filter.get(3)         // Get the awards list
        );
    }

    @Override
    public String execute() {
        // Construct the return string
        StringBuilder message = new StringBuilder("Query result: [");

        // Do the desired action
        switch (type) {
            case AVERAGE -> ActorQuery.average(this);
            case AWARDS -> ActorQuery.awards(this);
            case FILTER_DESCRIPTION -> ActorQuery.filterDescription(this);
            case RATING -> VideoQuery.rating(this);
            case FAVOURITE -> VideoQuery.favourite(this);
            case LONGEST -> VideoQuery.longest(this);
            case MOST_VIEWED -> VideoQuery.mostViewed(this);
            case NUMBER_OF_RATINGS -> UserQuery.numberOfRatings(this);
        }

        message.append("]");
        return message.toString();
    }
}
