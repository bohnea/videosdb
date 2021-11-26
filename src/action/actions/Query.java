package action.actions;

import action.Action;
import action.Filter;
import action.managers.search.ActorSearch;
import action.managers.search.UserSearch;
import action.managers.search.VideoSearch;
import action.managers.sort.ActorComparators;
import action.managers.sort.SortManager;
import action.managers.sort.UserComparators;
import action.managers.sort.VideoComparators;
import actor.Actor;
import actor.ActorsAwards;
import entertainment.Genre;
import entertainment.Video;
import user.User;
import utils.Utils;

import java.util.List;

public class Query extends Action {
    /**
     * Class holding functions for the queries applied on actors.
     */
    private static class ActorQuery {
        /**
         * Gets the first n actors sorted in ascending order by the average ratings of the
         * videos they have cast in, then by their names, only after they have been filtered.
         * @param query the query to execute
         * @return the query result
         */
        static List<Actor> average(final Query query) {
            // Get all the actors and remove the ones with no rated videos
            List<Actor> actors = ActorSearch
                    .getAllActors().stream()
                    // Filter out the ones with no ratings
                    .filter(actor -> actor.getMeanCastInVideoRatings() != 0.0d)
                    .toList();

            // Sort the actors by ratings, then by name
            return SortManager.sortByCriteria(
                    actors,
                    new SortManager.SortCriteria<>(
                            query.sortAscending,
                            ActorComparators.MEAN_RATING_COMPARATOR,
                            ActorComparators.NAME_COMPARATOR
                    ) // Get the first n actors
            ).subList(0, Math.min(query.number, actors.size()));
        }

        /**
         * Gets all the actors sorted in ascending order by their total award count,
         * then by their name, only after they have been filtered.
         * @param query the query to execute
         * @return the query result
         */
        static List<Actor> awards(final Query query) {
            // Get the filtered actors
            List<Actor> actors = query.filter.filterActors(
                    ActorSearch.getAllActors()
            );

            // Sort the actors by award count, then by name
            return SortManager.sortByCriteria(
                    actors,
                    new SortManager.SortCriteria<>(
                            query.sortAscending,
                            ActorComparators.AWARD_COUNT_COMPARATOR,
                            ActorComparators.NAME_COMPARATOR
                    )
            );
        }

        /**
         * Gets all the actors sorted in ascending order by their name,
         * only after they have been filtered.
         * @param query the query to execute
         * @return the query result
         */
        static List<Actor> filterDescription(final Query query) {
            // Get the filtered actors
            List<Actor> actors = query.filter.filterActors(
                    ActorSearch.getAllActors()
            );

            // Sort the actors by name
            return SortManager.sortByCriteria(
                    actors,
                    new SortManager.SortCriteria<>(
                            query.sortAscending,
                            ActorComparators.NAME_COMPARATOR
                    )
            );
        }
    }

    /**
     * Class holding functions for the queries applied on videos.
     */
    private static class VideoQuery {
        /**
         * Retrieves videos from the database depending on the given type.
         * @param type the type of video to retrieve
         * @return a list of all videos from the database of the specified type
         */
        private static List<Video> getVideosByObjectType(final ObjectType type) {
            return switch (type) {
                case VIDEO -> VideoSearch.getAllVideos();
                case MOVIE -> VideoSearch.getAllMovies();
                case SHOW -> VideoSearch.getAllShows();
            };
        }

        /**
         * Gets the first n videos sorted in ascending order by their total rating,
         * then by their name, only after they have been filtered.
         * @param query the query to execute
         * @return the query result
         */
        static List<Video> rating(final Query query) {
            List<Video> videos = query.filter
                    // Get the filtered videos of the given type
                    .filterVideos(getVideosByObjectType(query.objectType)).stream()
                    // Remove the unrated ones
                    .filter(video -> video.getTotalRating() != 0.0d)
                    // Make it into a list
                    .toList();

            // Sort the videos by rating, then by name
            return SortManager.sortByCriteria(
                    videos,
                    new SortManager.SortCriteria<>(
                            query.sortAscending,
                            VideoComparators.RATING_COMPARATOR,
                            VideoComparators.NAME_COMPARATOR
                    ) // Get the first n videos
            ).subList(0, Math.min(query.number, videos.size()));
        }

        /**
         * Gets the first n videos sorted in ascending order by the number of times they have
         * been added to favourites, then by their name, only after they have been filtered.
         * @param query the query to execute
         * @return the query result
         */
        static List<Video> favourite(final Query query) {
            List<Video> videos = query.filter
                    // Get the filtered videos of the given type
                    .filterVideos(getVideosByObjectType(query.objectType)).stream()
                    // Remove the ones that have not been added to favourites by anyone
                    .filter(video -> VideoSearch.getFavouriteCount(video) != 0)
                    // Make it into a list
                    .toList();

            // Sort the videos by favourite count, then by name
            return SortManager.sortByCriteria(
                    videos,
                    new SortManager.SortCriteria<>(
                            query.sortAscending,
                            VideoComparators.FAVOURITE_COUNT_COMPARATOR,
                            VideoComparators.NAME_COMPARATOR
                    ) // Get the first n videos
            ).subList(0, Math.min(query.number, videos.size()));
        }

        /**
         * Gets the first n videos sorted in ascending order by their duration, then by
         * their name, only after they have been filtered.
         * @param query the query to execute
         * @return the query result
         */
        static List<Video> longest(final Query query) {
            List<Video> videos = query.filter
                    // Get the filtered videos of the given type
                    .filterVideos(getVideosByObjectType(query.objectType));

            // Sort the videos by duration, then by name
            return SortManager.sortByCriteria(
                    videos,
                    new SortManager.SortCriteria<>(
                            query.sortAscending,
                            VideoComparators.DURATION_COMPARATOR,
                            VideoComparators.NAME_COMPARATOR
                    ) // Get the first n videos
            ).subList(0, Math.min(query.number, videos.size()));
        }

        /**
         * Gets the first n videos sorted in ascending order by their view count,
         * then by their name, only after they have been filtered.
         * @param query the query to execute
         * @return the query result
         */
        static List<Video> mostViewed(final Query query) {
            List<Video> videos = query.filter
                    // Get the filtered videos of the given type
                    .filterVideos(getVideosByObjectType(query.objectType)).stream()
                    // Remove the ones that have not been viewed by anyone
                    .filter(video -> VideoSearch.getViews(video) != 0)
                    // Make it into a list
                    .toList();

            // Sort the videos by views, then by name
            return SortManager.sortByCriteria(
                    videos,
                    new SortManager.SortCriteria<>(
                            query.sortAscending,
                            VideoComparators.VIEWS_COMPARATOR,
                            VideoComparators.NAME_COMPARATOR
                    ) // Get the first n videos
            ).subList(0, Math.min(query.number, videos.size()));
        }
    }

    private static class UserQuery {
        /**
         * Gets the first n users sorted in ascending order by their
         * number of given ratings, then by their name.
         * @param query the query to execute
         * @return the query result
         */
        static List<User> numberOfRatings(final Query query) {
            List<User> users = UserSearch
                    // Get all users
                    .getAllUsers().stream()
                    // Remove the ones with no ratings
                    .filter(user -> user.getRatingCount() != 0)
                    .toList();

            // Sort the users by rating counts, then by name
            return SortManager.sortByCriteria(
                    users,
                    new SortManager.SortCriteria<>(
                            query.sortAscending,
                            UserComparators.RATING_COUNT_COMPARATOR,
                            UserComparators.NAME_COMPARATOR
                    ) // Get the first n users
            ).subList(0, Math.min(query.number, users.size()));
        }
    }

    /**
     * Enum containing possible query types.
     */
    public enum Type {
        AVERAGE,
        AWARDS,
        FILTER_DESCRIPTION,
        RATING,
        FAVOURITE,
        LONGEST,
        MOST_VIEWED,
        NUMBER_OF_RATINGS,
        OTHER
    }

    /**
     * Enum containing possible sort orders.
     */
    public enum SortType {
        ASCENDING,
        DESCENDING
    }

    /**
     * Enum containing possible query object types.
     */
    public enum ObjectType {
        VIDEO,
        MOVIE,
        SHOW
    }

    private final Type type;
    private final int number;
    private final ObjectType objectType;
    private final boolean sortAscending;
    private Filter filter;

    public static final int YEAR_INDEX = 0;
    public static final int GENRE_INDEX = 1;
    public static final int WORDS_INDEX = 2;
    public static final int AWARDS_INDEX = 3;

    public Query(final Integer id, final Type type, final ObjectType objectType,
                 final int number, final List<List<String>> filter, final SortType sortType) {
        super(id);
        this.type = type;
        this.objectType = objectType;
        this.number = number;
        sortAscending = sortType == SortType.ASCENDING;

        // Stop if there are no filters
        if (filter == null) {
            return;
        }

        // Create the filter
        this.filter = createFilter(
                filter.get(YEAR_INDEX).get(0),   // Get the year string
                filter.get(GENRE_INDEX).get(0),  // Get the genre string
                filter.get(WORDS_INDEX),         // Get the words list
                filter.get(AWARDS_INDEX)         // Get the awards list
        );
    }

    /**
     * Creates a new Filter object from the given String parameters.
     * @param yearStr the year string
     * @param genreStr the genre string
     * @param words the words list
     * @param awardsStr the awards list
     * @return the new Filter object
     */
    private Filter createFilter(final String yearStr, final String genreStr,
                                final List<String> words, final List<String> awardsStr) {
        // Parse the year, if it exists
        int year = (yearStr == null) ? -1 : Integer.parseInt(yearStr);

        // Parse the genre, if it exists
        boolean sortByGenre = genreStr != null;
        Genre genre = Utils.stringToGenre(genreStr);

        // Parse the award list, if it exists
        List<ActorsAwards> awards =
                awardsStr == null
                        ? null
                        : awardsStr.stream()
                        .map(Utils::stringToAwards) // Turn the strings into awards;
                        .toList();

        // Check which filter constructor to use
        if (year != -1 || genre != null) {
            return new Filter(year, sortByGenre, genre);
        } else if (words != null || awards != null) {
            return new Filter(words, awards);
        }

        // If there are no filters, return an empty filter
        return new Filter();
    }

    /**
     * Executes the query.
     * @return a message with the result of the action execution
     */
    @Override
    public String execute() {
        // Retrieve the query
        return "Query result: " + switch (type) {
            case AVERAGE -> ActorQuery.average(this);
            case AWARDS -> ActorQuery.awards(this);
            case FILTER_DESCRIPTION -> ActorQuery.filterDescription(this);
            case RATING -> VideoQuery.rating(this);
            case FAVOURITE -> VideoQuery.favourite(this);
            case LONGEST -> VideoQuery.longest(this);
            case MOST_VIEWED -> VideoQuery.mostViewed(this);
            case NUMBER_OF_RATINGS -> UserQuery.numberOfRatings(this);
            default -> null;
        };
    }
}
