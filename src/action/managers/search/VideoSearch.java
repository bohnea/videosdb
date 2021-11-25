package action.managers.search;

import database.Database;
import database.DatabaseTrackable;
import entertainment.Genre;
import entertainment.Movie;
import entertainment.Show;
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

    public static List<Video> getAllMovies() {
        // Get all the videos in the database
        return getAllVideos().stream()
                // Check if the current video is an instance of a Movie
                .filter(video -> video instanceof Movie)
                .toList();
    }

    public static List<Video> getAllShows() {
        // Get all the videos in the database
        return getAllVideos().stream()
                // Check if the current video is an instance of a Show
                .filter(video -> video instanceof Show)
                .toList();
    }

    public static Video getVideoByTitle(String title) {
        // Retrieve the given video from the database
        DatabaseTrackable video = Database.getInstance().retrieveEntity(Video.class, title);

        // Check if it's the correct type, or it is null
        if (!(video instanceof Video)) {
            return null;
        }

        // Return the video
        return (Video) video;
    }

    public static long getFavouriteCount(Video video) {
        // Get all users
        List<User> users = UserSearch.getAllUsers();

        // Check if they've added the given video to their favourites
        return users.stream()
                // Remove the users that haven't added the given video to favourites
                .filter(user -> user.hasFavourite(video.getTitle()))
                // Count them
                .count();
    }

    public static int getViews(Video video) {
        // Get all users
        List<User> users = UserSearch.getAllUsers();

        // Add all the views (of the given video) of all the users
        return users.stream()
                // Get the view count for each user of the current video
                .map(user -> user.getViews(video.getTitle()))
                // Add all the views together
                .reduce(0, Integer::sum);
    }

    public static int getGenreViews(Genre genre) {
        // Get all videos of the given genre
        List<Video> videos = getVideosOfGenre(
                getAllVideos(),
                genre
        );

        // Get all the videos' views
        return videos.stream()
                // Get the current video's views
                .map(VideoSearch::getViews)
                // Add all the views together
                .reduce(0, Integer::sum);
    }

    public static List<Video> getUnwatchedVideos(List<Video> videos, User user) {
        return videos.stream() // Stream the videos
                .filter(video -> !user.hasWatched(video.getTitle())) // Get the unwatched videos
                .toList(); // Turn it into a list
    }

    public static List<Video> getVideosOfGenre(List<Video> videos, Genre genre) {
        return videos.stream()
                // Get the videos of the given genre from the list
                .filter(video -> video.isOfGenre(genre))
                .toList();
    }
}
