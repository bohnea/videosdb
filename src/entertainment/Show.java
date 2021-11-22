package entertainment;

import database.Database;
import user.User;
import utils.ActionExceptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Show extends Video {
    private ArrayList<Season> seasons;
    private HashMap<Integer, HashSet<String>> ratedUsers;

    public Show(String title, int launchYear, List<Genre> genres, List<String> cast, List<Season> seasons) {
        // Set the common video values
        super(title, launchYear, genres, cast);

        // Create the seasons list and add all the given seasons
        this.seasons = new ArrayList<>();
        this.seasons.addAll(seasons);

        // Initialize the rated users hashset
        ratedUsers = new HashMap<>();
        for (int i = 1; i <= seasons.size(); ++i) {
            ratedUsers.put(i, new HashSet<>());
        }
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

        // Check if the show has been watched by the user
        if (!user.hasWatched(getTitle())) {
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
}
