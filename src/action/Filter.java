package action;

import actor.Actor;
import actor.ActorsAwards;
import entertainment.Genre;
import entertainment.Video;

import java.util.ArrayList;
import java.util.List;

public final class Filter {
    private int year;
    private boolean sortByGenre;
    private Genre genre;
    private final List<String> words;
    private final List<ActorsAwards> awards;

    /**
     * Constructor for an empty filter, sets default values.
     */
    public Filter() {
        year = -1;
        sortByGenre = false;
        genre = null;
        words = new ArrayList<>();
        awards = new ArrayList<>();
    }

    /**
     * Constructor for a Video filter.
     * @param year the launch year of the filtered videos
     * @param genre the genre of the filtered videos
     */
    public Filter(final int year, final boolean sortByGenre, final Genre genre) {
        this();
        this.year = year;
        this.sortByGenre = sortByGenre;
        this.genre = genre;
    }

    /**
     * Constructor for an Actor filter.
     * @param words the words in the career descriptions of the filtered actors
     * @param awards the awards of the filtered actors
     */
    public Filter(final List<String> words, final List<ActorsAwards> awards) {
        this();

        if (words != null) {
            this.words.addAll(words);
        }

        if (awards != null) {
            this.awards.addAll(awards);
        }
    }

    /**
     * Checks if the given video respects the filter conditions.
     * @param video the video to be filtered
     * @return true if the video respects the filter conditions
     */
    private boolean filterVideo(final Video video) {
        // If either filtering by year and years differ,
        // or filtering by genre and video is not of that genre,
        // return false (don't keep the video)
        return !((year != -1 && year != video.getLaunchYear())
                || (sortByGenre && !video.isOfGenre(genre)));
    }

    /**
     * Filters the given list of videos by the filter conditions.
     * @param videos the videos to be filtered
     * @return the filtered list of videos
     */
    public List<Video> filterVideos(final List<Video> videos) {
        return videos.stream()
                .filter(this::filterVideo)
                .toList();
    }

    /**
     * Checks if the given actor respects the filter conditions.
     * @param actor the actor to be filtered
     * @return true if the actor respects the filter conditions
     */
    private boolean filterActor(final Actor actor) {
        // For each filtering word
        for (String word : words) {
            // If it's not found in the actor's description, don't keep him
            if (!actor.hasKeyword(word)) {
                return false;
            }
        }

        // For each filtering award
        for (ActorsAwards award : awards) {
            // If it's not found on the actor, don't keep him
            if (!actor.hasAward(award)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Filters the given list of actors by the filter conditions.
     * @param actors the actors to be filtered
     * @return the filtered list of actors
     */
    public List<Actor> filterActors(final List<Actor> actors) {
        return actors.stream()
                .filter(this::filterActor)
                .toList();
    }
}
