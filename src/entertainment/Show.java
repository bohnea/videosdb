package entertainment;

import user.User;
import common.ActionExceptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public final class Show extends Video {
    private final ArrayList<Season> seasons;
    private final HashMap<Integer, HashSet<String>> ratedUsers;

    public Show(final String title, final int launchYear,
                final List<Genre> genres, final List<Season> seasons) {
        // Set the common video values
        super(title, launchYear, genres);

        // Create the seasons list and add all the given seasons
        this.seasons = new ArrayList<>();
        this.seasons.addAll(seasons);

        // Initialize the rated users hashset
        ratedUsers = new HashMap<>();
        for (int i = 1; i <= seasons.size(); ++i) {
            ratedUsers.put(i, new HashSet<>());
        }
    }

    /**
     * Adds a rating by the given user to the show's given season.
     * @param rating the rating to add to the video
     * @param index the index of the season
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

        // Check if the show has been watched by the user
        if (user.hasNotWatched(getTitle())) {
            throw new ActionExceptions.NotWatchedException();
        }

        // Check if the movie has already been rated by the given user
        if (ratedUsers.get(index).contains(user.getUsername())) {
            throw new ActionExceptions.AlreadyRatedException();
        }

        // Otherwise, get the ratings list of the current season and add the new one
        List<Double> ratings = seasons.get(index - 1).getRatings();
        ratings.add(rating);

        // Add the user to the rated users set
        ratedUsers.get(index).add(user.getUsername());
    }

    /**
     * Calculates the average rating of the given season.
     * @param season the season to get the average rating for
     * @return the average rating
     */
    private double getAverageSeasonRating(final Season season) {
        // If the season has no ratings, consider the rating 0
        if (season.getRatings().isEmpty()) {
            return 0.0d;
        }

        // Otherwise, get the mean of the ratings
        return season.getRatings().stream()
                .reduce(0.0d, Double::sum) / season.getRatings().size();
    }

    /**
     * Calculates the total rating of the show by getting the average of
     * all the seasons' ratings.
     * @return the average rating, or 0 if unrated
     */
    @Override
    public double getTotalRating() {
        // Return the mean of all the ratings of all the seasons
        return seasons.stream()
                .mapToDouble(this::getAverageSeasonRating)
                .average()
                .orElse(0.0d);
    }

    /**
     * Gets the duration of the show by adding the durations of the seasons.
     * @return the show's duration
     */
    @Override
    public int getDuration() {
        return seasons.stream()
                .map(Season::getDuration)
                .reduce(0, Integer::sum);
    }
}
