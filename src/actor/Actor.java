package actor;

import database.DatabaseTrackable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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

    @Override
    public String getKey() {
        return name;
    }
}
