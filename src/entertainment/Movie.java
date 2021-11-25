package entertainment;

import user.User;
import common.ActionExceptions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Movie extends Video {
    private int duration;
    private List<Double> ratings;
    private HashSet<String> ratedUsers;

    public Movie(String title, int launchYear, int duration, List<Genre> genres, List<String> cast) {
        // Set the common video values
        super(title, launchYear, genres, cast);

        // Set the movie specific value
        this.duration = duration;

        // Initialize the collections
        ratings = new ArrayList<>();
        ratedUsers = new HashSet<>();
    }

    @Override
    public void addRating(double rating, int index, User user)
            throws ActionExceptions.EntryNotFoundException,
            ActionExceptions.NotWatchedException,
            ActionExceptions.AlreadyRatedException {
        // Check if the user exists
        if (user == null) {
            throw new ActionExceptions.EntryNotFoundException();
        }

        // Check if the movie has been watched by the user
        if (!user.hasWatched(getTitle())) {
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

    @Override
    public double getTotalRating() {
        // Return the mean of all the ratings
        return ratings.stream()
                .mapToDouble(number -> number)
                .average()
                .orElse(0.0d);
    }

    @Override
    public int getDuration() {
        return duration;
    }
}
