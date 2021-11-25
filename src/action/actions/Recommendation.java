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
    private static class BasicRecommendation {
        static List<String> standard(List<Video> videos) {
            // Check if the list is empty
            if (videos.isEmpty()) {
                return null;
            }

            // Otherwise, create a success message
            return List.of(videos.get(0).getTitle());
        }

        static List<String> bestUnseen(List<Video> videos) {
            // Sort the videos by rating
            videos = SortManager.sortByCriteria(
                    videos,
                    new SortManager.SortCriteria<>(
                            false,
                            VideoComparators.ratingComparator
                    ) // Get the first n videos
            );

            // Check if the list is empty
            if (videos.isEmpty()) {
                return null;
            }

            // Otherwise, create a success message
            return List.of(videos.get(0).getTitle());
        }
    }

    private static class PremiumRecommendation {
        static List<String> popular(List<Video> videos) {
            // Get all the genres sorted by views
            List<Genre> sortedGenres = SortManager.sortByCriteria(
                    Arrays.asList(Genre.values()),
                    new SortManager.SortCriteria<>(
                            false,
                            VideoComparators.genreViewsComparator
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

        static List<String> favourite(List<Video> videos) {
            videos = videos.stream()
                    // Remove the ones that have not been added to favourites by anyone
                    .filter(video -> VideoSearch.getFavouriteCount(video) != 0)
                    // Make it into a list
                    .toList();

            // Check if the list is empty
            if (videos.isEmpty()) {
                return null;
            }

            // Sort the videos by favourite count
            return List.of(SortManager.sortByCriteria(
                    videos,
                    new SortManager.SortCriteria<>(
                            false,
                            VideoComparators.favouriteCountComparator
                    ) // Get the first video in the list, and then its title
            ).get(0).getTitle());
        }

        static List<String> search(List<Video> videos, Genre genre) {
            // Get all unwatched videos of the given genre
            videos = VideoSearch.getVideosOfGenre(videos, genre);

            // Check if the list is empty
            if (videos.isEmpty()) {
                return null;
            }

            // Sort the videos by rating, then by name
            return SortManager.sortByCriteria(
                    videos,
                    new SortManager.SortCriteria<>(
                            true,
                            VideoComparators.ratingComparator,
                            VideoComparators.nameComparator
                    )
            ).stream()
                    // Get the titles
                    .map(Video::getTitle)
                    .toList();
        }
    }

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

    public Recommendation(Integer id, Type type, String username, Genre genre) {
        super(id);
        this.type = type;
        this.username = username;
        this.genre = genre;
    }

    private void writeMessageBeginning(StringBuilder message, Type type, boolean isSuccessful) {
        message.append(
                // Get the string from the type
                switch(type) {
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

    private void writeResultToMessage(StringBuilder message, List<String> result, boolean isList) {
        if (isList) {
            message.append(result);
        } else {
            message.append(result.get(0));
        }
    }

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
