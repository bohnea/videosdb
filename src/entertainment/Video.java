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
    private final HashSet<String> cast;

    public String getTitle() { return title; }

    public int getLaunchYear() { return launchYear; }

    public Video(String title, int launchYear, List<Genre> genres, List<String> cast) {
        this.title = title;
        this.launchYear = launchYear;

        this.genres = new HashSet<>();
        this.genres.addAll(genres);

        this.cast = new HashSet<>();
        this.cast.addAll(cast);
    }

    public boolean isOfGenre(Genre genre) {
        return genres.contains(genre);
    }

    public abstract void addRating(double rating, int index, User user)
            throws ActionExceptions.EntryNotFoundException,
            ActionExceptions.NotWatchedException,
            ActionExceptions.AlreadyRatedException;

    public abstract double getTotalRating();

    public abstract int getDuration();

    public String getKey() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }
}
