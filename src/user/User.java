package user;

import database.Database;
import database.DatabaseTrackable;
import entertainment.Video;
import common.ActionExceptions;

import java.util.*;

public class User implements DatabaseTrackable {
    public enum SubscriptionType {
        BASIC,
        PREMIUM
    }

    private final String username;
    private final SubscriptionType subscriptionType;
    private final HashSet<String> favourites;
    private final LinkedHashMap<String, Integer> watchedVideos;

    private int ratingCount;

    public User(String username, SubscriptionType subscriptionType, List<String> favourites, Map<String, Integer> watchedVideos) {
        // Set the basic information
        this.username = username;
        this.subscriptionType = subscriptionType;

        // Add all the elements in the favourites list to the hashset
        this.favourites = new HashSet<>();
        this.favourites.addAll(favourites);

        // Add all the key-value pairs in the history map to the hashmap
        this.watchedVideos = new LinkedHashMap<>();
        this.watchedVideos.putAll(watchedVideos);
    }

    public String getUsername() { return username; }

    public SubscriptionType getSubscriptionType() { return subscriptionType; }

    public int getRatingCount() { return ratingCount; }

    public boolean hasWatched(String title) {
        return watchedVideos.containsKey(title);
    }

    public boolean hasFavourite(String videoTitle) {
        return favourites.contains(videoTitle);
    }

    public int getViews(String videoTitle) {
        return watchedVideos.getOrDefault(videoTitle, 0);
    }

    public void addFavourite(String videoTitle)
            throws ActionExceptions.EntryNotFoundException,
            ActionExceptions.AlreadyFavouriteException,
            ActionExceptions.NotWatchedException {
        // Check the video's existence within the database
        if (Database.getInstance().retrieveEntity(Video.class, videoTitle) == null) {
            throw new ActionExceptions.EntryNotFoundException();
        }

        // Check if the video has been watched
        if (!hasWatched(videoTitle)) {
            throw new ActionExceptions.NotWatchedException();
        }

        // Check if the video has already been added to favourites
        if (favourites.contains(videoTitle)) {
            throw new ActionExceptions.AlreadyFavouriteException();
        }

        // If it exists and has been watched, add it to the favourites list
        favourites.add(videoTitle);
    }

    public int addView(String videoTitle)
            throws ActionExceptions.EntryNotFoundException {
        // Check the video's existence within the database
        if (Database.getInstance().retrieveEntity(Video.class, videoTitle) == null) {
            throw new ActionExceptions.EntryNotFoundException();
        }

        // Get the video's views (0 if it has not been watched)
        int views = watchedVideos.getOrDefault(videoTitle, 0);

        // Add / Update the view count of the video
        watchedVideos.put(videoTitle, views + 1);

        // Return the new view count
        return views + 1;
    }

    public void incrementRatingCount() {
        ++ratingCount;
    }

    @Override
    public String getKey() {
        return username;
    }

    @Override
    public String toString() {
        return username;
    }
}
