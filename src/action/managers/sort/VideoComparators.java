package action.managers.sort;

import action.managers.search.VideoSearch;
import entertainment.Genre;
import entertainment.Video;

import java.util.Comparator;

public final class VideoComparators {
    private VideoComparators() { }

    // Used for comparing usernames
    public static final Comparator<Video> NAME_COMPARATOR =
            Comparator.comparing(Video::getTitle);

    // Used for comparing ratings
    public static final Comparator<Video> RATING_COMPARATOR =
            Comparator.comparingDouble(Video::getTotalRating);

    // Used for comparing favourite counts
    public static final Comparator<Video> FAVOURITE_COUNT_COMPARATOR =
            Comparator.comparingLong(VideoSearch::getFavouriteCount);

    // Used for comparing durations
    public static final Comparator<Video> DURATION_COMPARATOR =
            Comparator.comparingInt(Video::getDuration);

    // Used for comparing views
    public static final Comparator<Video> VIEWS_COMPARATOR =
            Comparator.comparingInt(VideoSearch::getViews);

    // Used for comparing genres by views
    public static final Comparator<Genre> GENRE_VIEWS_COMPARATOR =
            Comparator.comparingInt(VideoSearch::getGenreViews);
}
