package action.managers;

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

    public Filter(int year, Genre genre) {
        this.year = year;
        this.genre = genre;
    }

    public Filter(List<String> words, List<ActorsAwards> awards) {
        this.words = new ArrayList<>();
        if (words != null) {
            this.words.addAll(words);
        }

        this.awards = new ArrayList<>();
        if (awards != null) {
            this.awards.addAll(awards);
        }
    }

    private boolean filterVideo(Video video) {
        // If either filtering by year and years differ,
        // or filtering by genre and video is not of that genre
        return (year != -1 && year != video.getLaunchYear())
                || (genre != null && video.isOfGenre(genre));
    }

    public List<Video> filterVideos(List<Video> videos) {
        return videos.stream()
                .filter(this::filterVideo)
                .toList();
    }

    private boolean filterActor(Actor actor) {
        boolean toRemove = false;

        // For each filtering word
        for (String word : words) {
            // If it's not found in the actor's description, filter him out
            if (!actor.hasKeyword(word)) {
                toRemove = true;
            }
        }

        // For each filtering award
        for (ActorsAwards award : awards) {
            // If it's not found on the actor, filter him out
            if (!actor.hasAward(award)) {
                toRemove = true;
            }
        }

        return toRemove;
    }

    public List<Actor> filterActors(List<Actor> actors) {
        return actors.stream()
                .filter(this::filterActor)
                .toList();
    }
}
