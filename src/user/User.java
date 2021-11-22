package user;

import database.Database;
import database.DatabaseTrackable;
import entertainment.Video;
import utils.ActionExceptions;

import java.util.*;

public class User implements DatabaseTrackable {
    public enum SubscriptionType {
        BASIC,
        PREMIUM
    }

    private String username;
    private SubscriptionType subscriptionType;
    private HashSet<String> favourites;
    private LinkedHashMap<String, Integer> history;

    public String getUsername() { return username; }

    public void addFavourite(String videoName)
            throws ActionExceptions.EntryNotFoundException,
            ActionExceptions.AlreadyExistsException,
            ActionExceptions.NotWatchedException {
        // Check the video's existence within the database
        if (Database.getInstance().retrieveEntity(Video.class, videoName) == null) {
            throw new ActionExceptions.EntryNotFoundException();
        }

        // Check if the video has been watched
        if (!history.containsKey(videoName)) {
            throw new ActionExceptions.NotWatchedException();
        }

        // Check if the video has already been added to favourites
        if (favourites.contains(videoName)) {
            throw new ActionExceptions.AlreadyExistsException();
        }

        // If it exists and has been watched, add it to the favourites list
        favourites.add(videoName);
    }

    public void addView(String videoName)
            throws ActionExceptions.EntryNotFoundException {
        // Check the video's existence within the database
        if (Database.getInstance().retrieveEntity(Video.class, videoName) == null) {
            throw new ActionExceptions.EntryNotFoundException();
        }

        // Get the video's views (0 if it has not been watched)
        int views = history.getOrDefault(videoName, 0);

        // Add / Update the view count of the video
        history.put(videoName, views + 1);
    }

    public void addRating() {

    }

    public User(String username, SubscriptionType subscriptionType, List<String> favourites, Map<String, Integer> history) {
        // Set the basic information
        this.username = username;
        this.subscriptionType = subscriptionType;

        // Add all the elements in the favourites list to the hashset
        this.favourites = new HashSet<>();
        this.favourites.addAll(favourites);

        // Add all the key-value pairs in the history map to the hashmap
        this.history = new LinkedHashMap<>();
        this.history.putAll(history);
    }

    @Override
    public String getKey() {
        return username;
    }
}
