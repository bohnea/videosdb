package user;

import database.DatabaseTrackable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class User implements DatabaseTrackable {
    public enum SubscriptionType {
        BASIC,
        PREMIUM
    }

    private String username;
    private SubscriptionType subscriptionType;
    private HashSet<String> favourites;
    private HashMap<String, Integer> history;

    public String getUsername() { return username; }

    public User(String username, SubscriptionType subscriptionType, List<String> favourites, Map<String, Integer> history) {
        // Set the basic information
        this.username = username;
        this.subscriptionType = subscriptionType;

        // Add all the elements in the favourites list to the hashset
        this.favourites = new HashSet<>();
        this.favourites.addAll(favourites);

        // Add all the key-value pairs in the history map to the hashmap
        this.history = new HashMap<>();
        this.history.putAll(history);
    }

    @Override
    public String getKey() {
        return username;
    }
}
