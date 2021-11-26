package entertainment;

import database.DatabaseTrackable;
import user.User;
import common.ActionExceptions;

import java.util.HashSet;
import java.util.List;

public abstract class Video implements DatabaseTrackable {
    private final String title;
    private final int launchYear;
    private final HashSet<Genre> genres;

    public final String getTitle() {
        return title;
    }

    public final int getLaunchYear() {
        return launchYear;
    }

    public Video(final String title, final int launchYear,
                 final List<Genre> genres) {
        this.title = title;
        this.launchYear = launchYear;

        this.genres = new HashSet<>();
        this.genres.addAll(genres);
    }

    /**
     * Checks if the video is of the given genre.
     * @param genre the genre to check for
     * @return true if the video is of the given genre
     */
    public final boolean isOfGenre(final Genre genre) {
        return genres.contains(genre);
    }

    /**
     * Adds a rating by the given user to the movie.
     * @param rating the rating to add to the video
     * @param index unused, used for Shows
     * @param user the user rating the video
     * @throws ActionExceptions.EntryNotFoundException if the user is not found in the database
     * @throws ActionExceptions.NotWatchedException if the video has not been watched
     * @throws ActionExceptions.AlreadyRatedException if the video has already been rated
     */
    public abstract void addRating(double rating, int index, User user)
            throws ActionExceptions.EntryNotFoundException,
            ActionExceptions.NotWatchedException,
            ActionExceptions.AlreadyRatedException;

    /**
     * Calculates the total rating of the video.
     * @return the rating
     */
    public abstract double getTotalRating();

    /**
     * Gets the duration of the video.
     * @return the duration
     */
    public abstract int getDuration();

    /**
     * Gets the primary key for the database, the title.
     * @return a string containing the title of the video
     */
    public String getKey() {
        return title;
    }

    /**
     * Prints the title of the video.
     * @return a string containing the title of the video
     */
    @Override
    public String toString() {
        return title;
    }
}
