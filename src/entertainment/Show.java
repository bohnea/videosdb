package entertainment;

import java.util.ArrayList;
import java.util.List;

public class Show extends Video {
    private ArrayList<Season> seasons;

    public Show(String title, int launchYear, List<Genre> genres, List<String> cast, List<Season> seasons) {
        // Set the common video values
        super(title, launchYear, genres, cast);

        // Set the movie specific value
        this.seasons = new ArrayList<>();
        this.seasons.addAll(seasons);
    }

    @Override
    public void setRatings(List<Double> ratings) {

    }
}
