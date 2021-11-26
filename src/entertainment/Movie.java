package entertainment;

import user.User;
import common.ActionExceptions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public final class Movie extends Video {
    private final int duration;
    private final List<Double> ratings;
    private final HashSet<String> ratedUsers;

    public Movie(final String title, final int launchYear,
                 final int duration, final List<Genre> genres) {
        // Set the common video values
        super(title, launchYear, genres);

        // Set the movie specific value
        this.duration = duration;

        // Initialize the collections
        ratings = new ArrayList<>();
        ratedUsers = new HashSet<>();
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
    @Override
    public void addRating(final double rating, final int index, final User user)
            throws ActionExceptions.EntryNotFoundException,
            ActionExceptions.NotWatchedException,
            ActionExceptions.AlreadyRatedException {
        // Check if the user exists
        if (user == null) {
            throw new ActionExceptions.EntryNotFoundException();
        }

        // Check if the movie has been watched by the user
        if (user.hasNotWatched(getTitle())) {
            throw new ActionExceptions.NotWatchedException();
        }

        // Check if the movie has already been rated by the given user
        if (ratedUsers.contains(user.getUsername())) {
            throw new ActionExceptions.AlreadyRatedException();
        }

        // Otherwise, rate the movie and add the user to the hashset
        ratings.add(rating);
        ratedUsers.add(user.getUsername());
    }

    /**
     * Calculates the total rating of the movie by getting the average of
     * all the numbers in the ratings collection.
     * @return the average rating, or 0 if unrated
     */
    @Override
    public double getTotalRating() {
        // Return the mean of all the ratings
        return ratings.stream()
                .mapToDouble(number -> number)
                .average()
                .orElse(0.0d);
    }

    /**
     * Gets the duration of the movie.
     * @return the movie's duration
     */
    @Override
    public int getDuration() {
        return duration;
    }
}
