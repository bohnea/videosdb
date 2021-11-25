package action;

import actor.Actor;
import actor.ActorsAwards;
import entertainment.Genre;
import entertainment.Video;

import java.util.ArrayList;
import java.util.List;

public class Filter {
    private int year;
    private Genre genre;
    private List<String> words;
    private List<ActorsAwards> awards;

    public Filter() {
        year = -1;
        genre = null;
        words = new ArrayList<>();
        awards = new ArrayList<>();
    }

    public Filter(int year, Genre genre) {
        this();
        this.year = year;
        this.genre = genre;
    }

    public Filter(List<String> words, List<ActorsAwards> awards) {
        this();

        if (words != null) {
            this.words.addAll(words);
        }

        if (awards != null) {
            this.awards.addAll(awards);
        }
    }

    private boolean filterVideo(Video video) {
        // If either filtering by year and years differ,
        // or filtering by genre and video is not of that genre,
        // return false (don't keep the video)
        return !((year != -1 && year != video.getLaunchYear())
                || (genre != null && !video.isOfGenre(genre)));
    }

    public List<Video> filterVideos(List<Video> videos) {
        return videos.stream()
                .filter(this::filterVideo)
                .toList();
    }

    private boolean filterActor(Actor actor) {
        // Initially, the actor should be kept
        boolean toKeep = true;

        // For each filtering word
        for (String word : words) {
            // If it's not found in the actor's description, don't keep him
            if (!actor.hasKeyword(word)) {
                toKeep = false;
            }
        }

        // For each filtering award
        for (ActorsAwards award : awards) {
            // If it's not found on the actor, don't keep him
            if (!actor.hasAward(award)) {
                toKeep = false;
            }
        }

        return toKeep;
    }

    public List<Actor> filterActors(List<Actor> actors) {
        return actors.stream()
                .filter(this::filterActor)
                .toList();
    }
}
