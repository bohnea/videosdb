package action.managers.search;

import database.Database;
import database.DatabaseTrackable;
import entertainment.Genre;
import entertainment.Movie;
import entertainment.Show;
import entertainment.Video;
import user.User;

import java.util.List;

public final class VideoSearch {
    private VideoSearch() { }

    /**
     * Retrieves all videos from the database.
     * @return a list of videos
     */
    public static List<Video> getAllVideos() {
        return Database.getInstance()
                .retrieveClassEntities(Video.class) // Retrieve the Video entries from the database
                .values().stream() // Get all the videos and stream them
                .map(databaseTrackable -> (Video) databaseTrackable) // Cast them to Video
                .toList(); // Make it into a list
    }

    /**
     * Retrieves all movies from the database.
     * @return a list of movies
     */
    public static List<Video> getAllMovies() {
        // Get all the videos in the database
        return getAllVideos().stream()
                // Check if the current video is an instance of a Movie
                .filter(video -> video instanceof Movie)
                .toList();
    }

    /**
     * Retrieves all shows from the database.
     * @return a list of shows
     */
    public static List<Video> getAllShows() {
        // Get all the videos in the database
        return getAllVideos().stream()
                // Check if the current video is an instance of a Show
                .filter(video -> video instanceof Show)
                .toList();
    }

    /**
     * Retrieves a video from the database, given the title.
     * @param title to video to retrieve
     * @return the retrieved video
     */
    public static Video getVideoByTitle(final String title) {
        // Retrieve the given video from the database
        DatabaseTrackable video = Database.getInstance().retrieveEntity(Video.class, title);

        // Check if it's the correct type, or it is null
        if (!(video instanceof Video)) {
            return null;
        }

        // Return the video
        return (Video) video;
    }

    /**
     * Gets all the users from the database and for each of them, checks if
     * the given video is found in their favourites list.
     * @param video the video to search for
     * @return the number of users with the given video in their favourites list
     */
    public static long getFavouriteCount(final Video video) {
        // Get all users
        List<User> users = UserSearch.getAllUsers();

        // Check if they've added the given video to their favourites
        return users.stream()
                // Remove the users that haven't added the given video to favourites
                .filter(user -> user.hasFavourite(video.getTitle()))
                // Count them
                .count();
    }

    /**
     * Gets all the users from the database and for each of them, counts how
     * many times the user has viewed the given video, if any.
     * @param video the video to search for
     * @return the number of views of the given video
     */
    public static int getViews(final Video video) {
        // Get all users
        List<User> users = UserSearch.getAllUsers();

        // Add all the views (of the given video) of all the users
        return users.stream()
                // Get the view count for each user of the current video
                .map(user -> user.getViews(video.getTitle()))
                // Add all the views together
                .reduce(0, Integer::sum);
    }

    /**
     * Gets all unwatched videos, found in the given videos list, by the given user.
     * @param videos the videos to search through
     * @param user the user to search for
     * @return a list of unwatched videos
     */
    public static List<Video> getUnwatchedVideos(final List<Video> videos, final User user) {
        return videos.stream() // Stream the videos
                .filter(video -> user.hasNotWatched(video.getTitle())) // Get the unwatched videos
                .toList(); // Turn it into a list
    }

    /**
     * Gets all videos, found in the given videos list, of the given genre.
     * @param videos the videos to search through
     * @param genre the genre of the videos
     * @return a list of videos of the given genre
     */
    public static List<Video> getVideosOfGenre(final List<Video> videos, final Genre genre) {
        return videos.stream()
                // Get the videos of the given genre from the list
                .filter(video -> video.isOfGenre(genre))
                .toList();
    }

    /**
     * Gets all videos of the given genre from the database, then adds all
     * the views of all the found videos.
     * @param genre the genre to search for
     * @return the total view count of the videos of the given genre
     */
    public static int getGenreViews(final Genre genre) {
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
}
