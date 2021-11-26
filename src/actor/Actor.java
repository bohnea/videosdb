package actor;

import action.managers.search.VideoSearch;
import database.DatabaseTrackable;
import entertainment.Video;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Actor implements DatabaseTrackable {
    private final String name;
    private final String careerDescription;
    private final HashSet<String> filmography;
    private final HashMap<ActorsAwards, Integer> awards;

    public Actor(final String name, final String careerDescription,
                 final List<String> filmography, final Map<ActorsAwards, Integer> awards) {
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

    public String getName() {
        return name;
    }

    /**
     * Gets the ratings of all the videos the actor has cast in and calculates
     * the average of the rated videos' ratings.
     * @return the average of all the videos' ratings
     */
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

    /**
     * Gets the amount of awards the actor has received.
     * @return the total award count
     */
    public int getAwardCount() {
        // Get all the award counts and add them together
        return awards.values().stream()
                .reduce(0, Integer::sum);
    }

    /**
     * Checks if the actor has received the given award.
     * @param award the award to check for
     * @return true if the actor has received the award
     */
    public boolean hasAward(final ActorsAwards award) {
        // Check for the award in the awards hashset
        return awards.containsKey(award);
    }

    /**
     * Checks if the actor's career description contains the given word.
     * @param word the word to search for
     * @return true if the word is found
     */
    public boolean hasKeyword(final String word) {
        // Create a regex pattern to isolate the given word with word boundaries
        Pattern pattern = Pattern.compile(
                "\\b" + word.toLowerCase() + "\\b",
                Pattern.CASE_INSENSITIVE
        );

        // Create a matcher to look for the given pattern in the career description
        Matcher matcher = pattern.matcher(careerDescription.toLowerCase());

        // Attempt to find the given pattern
        return matcher.find();
    }

    /**
     * Gets the primary key for the database, the actor's name.
     * @return the name of the actor
     */
    @Override
    public String getKey() {
        return name;
    }

    /**
     * Prints the name of the actor.
     * @return a string containing the name of the actor
     */
    @Override
    public String toString() {
        return name;
    }
}
