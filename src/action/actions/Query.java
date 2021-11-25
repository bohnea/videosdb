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
    private static class ActorQuery {
        static List<Actor> average(Query query) {
            // Get all the actors and remove the ones with no rated videos
            List<Actor> actors = ActorSearch
                    .getAllActors().stream()
                    // Filter out the ones with no ratings
                    .filter(actor -> actor.getMeanCastInVideoRatings() != 0.0d)
                    .toList();

            // Sort the actors by ratings, then by name
            return SortManager.sortByCriteria(
                    actors,
                    new SortManager.SortCriteria<Actor>(
                            query.sortAscending,
                            ActorComparators.meanRatingComparator,
                            ActorComparators.nameComparator
                    ) // Get the first n actors
            ).subList(0, Math.min(query.number, actors.size()));
        }

        static List<Actor> awards(Query query) {
            // Get the filtered actors
            List<Actor> actors = query.filter.filterActors(
                    ActorSearch.getAllActors()
            );

            // Sort the actors by award count, then by name
            return SortManager.sortByCriteria(
                    actors,
                    new SortManager.SortCriteria<>(
                            query.sortAscending,
                            ActorComparators.awardCountComparator,
                            ActorComparators.nameComparator
                    )
            );
        }

        static List<Actor> filterDescription(Query query) {
            // Get the filtered actors
            List<Actor> actors = query.filter.filterActors(
                    ActorSearch.getAllActors()
            );

            // Sort the actors by award count, then by name
            return SortManager.sortByCriteria(
                    actors,
                    new SortManager.SortCriteria<>(
                            query.sortAscending,
                            ActorComparators.nameComparator
                    )
            );
        }
    }

    private static class VideoQuery {
        private static List<Video> getVideosByObjectType(ObjectType type) {
            return switch (type) {
                case VIDEO -> VideoSearch.getAllVideos();
                case MOVIE -> VideoSearch.getAllMovies();
                case SHOW -> VideoSearch.getAllShows();
            };
        }

        static List<Video> rating(Query query) {
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
                            VideoComparators.ratingComparator,
                            VideoComparators.nameComparator
                    ) // Get the first n videos
            ).subList(0, Math.min(query.number, videos.size()));
        }

        static List<Video> favourite(Query query) {
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
                            VideoComparators.favouriteCountComparator,
                            VideoComparators.nameComparator
                    ) // Get the first n videos
            ).subList(0, Math.min(query.number, videos.size()));
        }

        static List<Video> longest(Query query) {
            List<Video> videos = query.filter
                    // Get the filtered videos of the given type
                    .filterVideos(getVideosByObjectType(query.objectType));

            // Sort the videos by duration, then by name
            return SortManager.sortByCriteria(
                    videos,
                    new SortManager.SortCriteria<>(
                            query.sortAscending,
                            VideoComparators.durationComparator,
                            VideoComparators.nameComparator
                    ) // Get the first n videos
            ).subList(0, Math.min(query.number, videos.size()));
        }

        static List<Video> mostViewed(Query query) {
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
                            VideoComparators.viewsComparator,
                            VideoComparators.nameComparator
                    ) // Get the first n videos
            ).subList(0, Math.min(query.number, videos.size()));
        }
    }

    private static class UserQuery {
        static List<User> numberOfRatings(Query query) {
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
                            UserComparators.ratingCountComparator,
                            UserComparators.nameComparator
                    ) // Get the first n users
            ).subList(0, Math.min(query.number, users.size()));
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

    public Query(Integer id, Type type, ObjectType objectType, int number, List<List<String>> filter, SortType sortType) {
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
                filter.get(0).get(0), // Get the year string
                filter.get(1).get(0), // Get the genre string
                filter.get(2),        // Get the words list
                filter.get(3)         // Get the awards list
        );
    }

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

        // If there are no filters, return an empty filter
        return new Filter();
    }

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
        };
    }
}
