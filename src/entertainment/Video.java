package entertainment;

import database.DatabaseTrackable;
import user.User;
import utils.ActionExceptions;

import java.util.HashSet;
import java.util.List;

public abstract class Video implements DatabaseTrackable {
    private String title;
    private int launchYear;
    private HashSet<Genre> genres;
    private HashSet<String> cast;

    public String getTitle() { return title; }

    public Video(String title, int launchYear, List<Genre> genres, List<String> cast) {
        this.title = title;
        this.launchYear = launchYear;

        this.genres = new HashSet<>();
        this.genres.addAll(genres);

        this.cast = new HashSet<>();
        this.cast.addAll(cast);
    }

    public abstract void addRating(double rating, int index, User user)
            throws ActionExceptions.EntryNotFoundException,
            ActionExceptions.NotWatchedException,
            ActionExceptions.AlreadyRatedException;

    public String getKey() {
        return title;
    }
}
