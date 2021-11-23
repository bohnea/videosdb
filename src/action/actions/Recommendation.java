package action.actions;

import action.Action;
import action.managers.search.VideoSearch;
import database.Database;
import database.DatabaseTrackable;
import entertainment.Genre;
import entertainment.Video;
import user.User;

import java.util.List;
import java.util.function.Predicate;

public class Recommendation extends Action {
    private static class BasicRecommendation {
        static void standard(Recommendation recommendation, StringBuilder message, User user) {
            // Get all unwatched (by the user) videos from the database
            List<Video> videos = VideoSearch.getUnwatchedVideos(
                    VideoSearch.getAllVideos(),
                    user
            );

            // Check if the list is empty
            if (videos.isEmpty()) {
                message.append("StandardRecommendation cannot be applied!");
                return;
            }

            // Otherwise, create a success message
            message.append("StandardRecommendation result: ").append(videos.get(0).getTitle());
        }

        static void bestUnseen(Recommendation recommendation, StringBuilder message, User user) {
        }
    }

    private static class PremiumRecommendation {
        static void popular(Recommendation recommendation, StringBuilder message, User user) {
        }

        static void favourite(Recommendation recommendation, StringBuilder message, User user) {
        }

        static void search(Recommendation recommendation, StringBuilder message, User user) {
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

    @Override
    public String execute() {
        // Construct the return string
        StringBuilder message = new StringBuilder();

        // Get the user by the username from the database
        DatabaseTrackable user = Database.getInstance().retrieveEntity(User.class, username);

        // For safety, check if the user is of the correct type
        if (!(user instanceof User)) {
            throw new ClassCastException();
        }

        switch (type) {
            case STANDARD -> BasicRecommendation.standard(this, message, (User) user);
            case BEST_UNSEEN -> BasicRecommendation.bestUnseen(this, message, (User) user);
            case POPULAR -> PremiumRecommendation.popular(this, message, (User) user);
            case FAVOURITE -> PremiumRecommendation.favourite(this, message, (User) user);
            case SEARCH -> PremiumRecommendation.search(this, message, (User) user);
        }

        return message.toString();
    }
}
