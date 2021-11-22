package entertainment;

import java.util.List;

public class Movie extends Video {
    private int duration;
    private double rating;

    public Movie(String title, int launchYear, int duration, List<Genre> genres, List<String> cast) {
        // Set the common video values
        super(title, launchYear, genres, cast);

        // Set the movie specific value
        this.duration = duration;
    }

    @Override
    public void setRatings(List<Double> ratings) {

    }
}
