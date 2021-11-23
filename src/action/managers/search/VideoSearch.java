package action.managers.search;

import database.Database;
import entertainment.Video;
import user.User;

import java.util.List;
import java.util.function.Predicate;

public class VideoSearch {
    public static List<Video> getAllVideos() {
        return Database.getInstance()
                .retrieveClassEntities(Video.class) // Retrieve the Video entries from the database
                .values().stream() // Get all the videos
                .map(databaseTrackable -> (Video) databaseTrackable) // Cast them to Video
                .toList(); // Make it into a list
    }

    public static List<Video> getUnwatchedVideos(List<Video> videos, User user) {
        return videos.stream() // Stream the videos
                .filter(video -> !user.hasWatched(video.getTitle())) // Get the unwatched videos
                .toList(); // Turn it into a list
    }
}
