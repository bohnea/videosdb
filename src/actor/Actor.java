package actor;

import action.managers.search.VideoSearch;
import database.DatabaseTrackable;
import entertainment.Video;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Actor implements DatabaseTrackable {
    private String name;
    private String careerDescription;
    private HashSet<String> filmography;
    private HashMap<ActorsAwards, Integer> awards;

    public Actor(String name, String careerDescription, List<String> filmography, Map<ActorsAwards, Integer> awards) {
        // Set the basic information
        this.name = name;
        this.careerDescription = careerDescription;

        // Add all the elements in the filmography list to the hashset
        this.filmography = new HashSet<>();
        this.filmography.addAll(filmography);

        // Add all the key-value pairs in the awards map to the hashmap
        this.awards = new HashMap<>();
        this.awards.putAll(awards);
    }

    public String getName() { return name; }

    public Double getMeanCastInVideoRatings() {
        return filmography.stream()
                // Get each video and calculate the total rating
                .map(VideoSearch::getVideoByTitle)
                // Check for videos not in the database
                .filter(Objects::nonNull)
                // Get the videos' total ratings
                .mapToDouble(Video::getTotalRating)
                // Filter out the unrated videos
                .filter(doubleNumber -> doubleNumber != 0.0f)
                // Calculate the average
                .average()
                .orElse(0.0d);
    }

    public int getAwardCount() {
        // Get all the award counts and add them together
        return awards.values().stream()
                .reduce(0, Integer::sum);
    }

    public boolean hasAward(ActorsAwards award) {
        // Check for the award in the awards hashset
        return awards.containsKey(award);
    }

    public boolean hasKeyword(String word) {
        // Create a regex pattern to isolate the given word with word boundaries
        Pattern pattern = Pattern.compile("\\b" + word.toLowerCase() + "\\b", Pattern.CASE_INSENSITIVE);

        // Create a matcher to look for the given pattern in the career description
        Matcher matcher = pattern.matcher(careerDescription.toLowerCase());

        // Attempt to find the given pattern
        return matcher.find();
    }

    @Override
    public String getKey() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
