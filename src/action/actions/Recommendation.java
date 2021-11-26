package action.actions;

import action.Action;
import action.managers.search.UserSearch;
import action.managers.search.VideoSearch;
import action.managers.sort.SortManager;
import action.managers.sort.VideoComparators;
import entertainment.Genre;
import entertainment.Video;
import user.User;

import java.util.Arrays;
import java.util.List;

public class Recommendation extends Action {
    /**
     * Basic membership functionality.
     */
    private static class BasicRecommendation {
        /**
         * Recommends the first unwatched video from the database.
         * @param videos the unwatched videos
         * @return the title of the video
         */
        static List<String> standard(final List<Video> videos) {
            // Check if the list is empty
            if (videos.isEmpty()) {
                return null;
            }

            // Otherwise, create a success message
            return List.of(videos.get(0).getTitle());
        }

        /**
         * Recommends the first unwatched video from the database, after sorting the videos
         * in descending order by their total rating.
         * @param videos the unwatched videos
         * @return the title of the video
         */
        static List<String> bestUnseen(final List<Video> videos) {
            // Sort the videos by rating
            List<Video> sortedVideos = SortManager.sortByCriteria(
                    videos,
                    new SortManager.SortCriteria<>(
                            false,
                            VideoComparators.RATING_COMPARATOR
                    ) // Get the first n videos
            );

            // Check if the list is empty
            if (sortedVideos.isEmpty()) {
                return null;
            }

            // Otherwise, create a success message
            return List.of(sortedVideos.get(0).getTitle());
        }
    }

    /**
     * Premium membership functionality.
     */
    private static class PremiumRecommendation {
        /**
         * Recommends the first unwatched video from the database, of the genre with the most
         * amount of views and with yet unwatched videos.
         * @param videos the unwatched videos
         * @return the title of the video
         */
        static List<String> popular(final List<Video> videos) {
            // Get all the genres sorted by views
            List<Genre> sortedGenres = SortManager.sortByCriteria(
                    Arrays.asList(Genre.values()),
                    new SortManager.SortCriteria<>(
                            false,
                            VideoComparators.GENRE_VIEWS_COMPARATOR
                    )
            );

            // Go through each genre and get all unwatched videos of that genre
            for (Genre genre : sortedGenres) {
                // Get the unwatched videos
                List<Video> unwatchedVideos = VideoSearch.getVideosOfGenre(videos, genre);

                // If it's not empty, return the first video's title
                if (!unwatchedVideos.isEmpty()) {
                    return List.of(unwatchedVideos.get(0).getTitle());
                }
            }

            // If there are no videos found, return null
            return null;
        }

        /**
         * Recommends the first unwatched video from the database, after sorting the videos
         * in descending order by the number of users that have added them to favourites.
         * @param videos the unwatched videos
         * @return the title of the video
         */
        static List<String> favourite(final List<Video> videos) {
            List<Video> sortedVideos = videos.stream()
                    // Remove the ones that have not been added to favourites by anyone
                    .filter(video -> VideoSearch.getFavouriteCount(video) != 0)
                    // Make it into a list
                    .toList();

            // Check if the list is empty
            if (sortedVideos.isEmpty()) {
                return null;
            }

            // Sort the videos by favourite count
            return List.of(SortManager.sortByCriteria(
                    sortedVideos,
                    new SortManager.SortCriteria<>(
                            false,
                            VideoComparators.FAVOURITE_COUNT_COMPARATOR
                    ) // Get the first video in the list, and then its title
            ).get(0).getTitle());
        }

        /**
         * Recommends all unwatched videos of a given genre from the database, after sorting
         * them in ascending order by their total rating, then by their name.
         * @param videos the unwatched videos
         * @param genre the genre to search after
         * @return the titles of the videos
         */
        static List<String> search(final List<Video> videos, final Genre genre) {
            // Get all unwatched videos of the given genre
            List<Video> sortedVideos = VideoSearch.getVideosOfGenre(videos, genre);

            // Check if the list is empty
            if (sortedVideos.isEmpty()) {
                return null;
            }

            // Sort the videos by rating, then by name
            return SortManager.sortByCriteria(
                    sortedVideos,
                    new SortManager.SortCriteria<>(
                            true,
                            VideoComparators.RATING_COMPARATOR,
                            VideoComparators.NAME_COMPARATOR
                    )
            ).stream()
                    // Get the titles
                    .map(Video::getTitle)
                    .toList();
        }
    }

    /**
     * Enum containing possible recommendation types.
     */
    public enum Type {
        STANDARD,
        BEST_UNSEEN,
        POPULAR,
        FAVOURITE,
        SEARCH
    }

    private final Type type;
    private final String username;
    private final Genre genre;

    public Recommendation(final Integer id, final Type type,
                          final String username, final Genre genre) {
        super(id);
        this.type = type;
        this.username = username;
        this.genre = genre;
    }

    /**
     * Writes the output of the given action to the message, without the results of the
     * recommendation. Writes both fail and success messages, based on the recommendation type.
     * @param message the message to add text to
     * @param recommendationType the recommendation type
     * @param isSuccessful success / fail message
     */
    private void writeMessageBeginning(final StringBuilder message,
                                       final Type recommendationType, final boolean isSuccessful) {
        message.append(
                // Get the string from the type
                switch (recommendationType) {
                    case STANDARD -> "Standard";
                    case BEST_UNSEEN -> "BestRatedUnseen";
                    case POPULAR -> "Popular";
                    case FAVOURITE -> "Favorite";
                    case SEARCH -> "Search";
                }
        ).append("Recommendation");

        if (isSuccessful) {
            message.append(" result: ");
        } else {
            message.append(" cannot be applied!");
        }
    }

    /**
     * Writes the second part of the message, the results of the recommendation,
     * either as a list or a single string.
     * @param message the message to add text to
     * @param result the results of the recommendation
     * @param isList write as a list / individual
     */
    private void writeResultToMessage(final StringBuilder message,
                                      final List<String> result, final boolean isList) {
        if (isList) {
            message.append(result);
        } else {
            message.append(result.get(0));
        }
    }

    /**
     * Executes the recommendation.
     * @return a message with the result of the action execution
     */
    @Override
    public String execute() {
        // Construct the return string
        StringBuilder message = new StringBuilder();

        // Get the user by the username from the database
        User user = UserSearch.getUserByUsername(username);
        assert user != null;

        // Check if the user's subscription is correct
        if (user.getSubscriptionType() == User.SubscriptionType.BASIC
                && (type == Type.POPULAR || type == Type.FAVOURITE || type == Type.SEARCH)) {
            writeMessageBeginning(message, type, false);
            return message.toString();
        }

        // Get all the unwatched videos by the user
        List<Video> videos = VideoSearch.getUnwatchedVideos(
                VideoSearch.getAllVideos(),
                user
        );

        // Get the recommendation results
        List<String> result = switch (type) {
            case STANDARD -> BasicRecommendation.standard(videos);
            case BEST_UNSEEN -> BasicRecommendation.bestUnseen(videos);
            case POPULAR -> PremiumRecommendation.popular(videos);
            case FAVOURITE -> PremiumRecommendation.favourite(videos);
            case SEARCH -> PremiumRecommendation.search(videos, genre);
        };

        // Start writing the message
        writeMessageBeginning(message, type, result != null);

        // If there is a result
        if (result != null) {
            // Write the result
            // If the type is Search, print it as a list
            writeResultToMessage(message, result, type == Type.SEARCH);
        }

        return message.toString();
    }
}
