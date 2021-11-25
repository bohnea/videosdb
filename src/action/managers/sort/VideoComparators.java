package action.managers.sort;

import action.managers.search.VideoSearch;
import entertainment.Genre;
import entertainment.Video;

import java.util.Comparator;

public class VideoComparators {
    // Used for comparing usernames
    public static final Comparator<Video> nameComparator =
            Comparator.comparing(Video::getTitle);

    // Used for comparing ratings
    public static final Comparator<Video> ratingComparator =
            Comparator.comparingDouble(Video::getTotalRating);

    // Used for comparing favourite counts
    public static final Comparator<Video> favouriteCountComparator =
            Comparator.comparingLong(VideoSearch::getFavouriteCount);

    // Used for comparing durations
    public static final Comparator<Video> durationComparator =
            Comparator.comparingInt(Video::getDuration);

    // Used for comparing views
    public static final Comparator<Video> viewsComparator =
            Comparator.comparingInt(VideoSearch::getViews);

    // Used for comparing genres by views
    public static final Comparator<Genre> genreViewsComparator =
            Comparator.comparingInt(VideoSearch::getGenreViews);
}
