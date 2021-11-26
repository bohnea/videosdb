package user;

import database.Database;
import database.DatabaseTrackable;
import entertainment.Video;
import common.ActionExceptions;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class User implements DatabaseTrackable {
    /**
     * Possible membership types.
     */
    public enum SubscriptionType {
        BASIC,
        PREMIUM
    }

    private final String username;
    private final SubscriptionType subscriptionType;
    private final HashSet<String> favourites;
    private final LinkedHashMap<String, Integer> watchedVideos;

    /**
     * The number of ratings the user has given.
     */
    private int ratingCount;

    public User(final String username, final SubscriptionType subscriptionType,
                final List<String> favourites, final Map<String, Integer> watchedVideos) {
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

    public String getUsername() {
        return username;
    }

    public SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    /**
     * Checks if the user has watched the given video.
     * @param videoTitle the title of the video
     * @return true if the user has NOT watched the video
     */
    public boolean hasNotWatched(final String videoTitle) {
        return !watchedVideos.containsKey(videoTitle);
    }

    /**
     * Checks if the user has added the given video to favourites.
     * @param videoTitle the title of the video
     * @return true if the video is in the favourites collection
     */
    public boolean hasFavourite(final String videoTitle) {
        return favourites.contains(videoTitle);
    }

    /**
     * Gets the user's view count of the given video.
     * @param videoTitle the title of the video
     * @return the view count
     */
    public int getViews(final String videoTitle) {
        return watchedVideos.getOrDefault(videoTitle, 0);
    }

    /**
     * Adds the given video to favourites.
     * @param videoTitle the title of the video
     * @throws ActionExceptions.EntryNotFoundException if the video is not found in the database
     * @throws ActionExceptions.AlreadyFavouriteException if the video is already a favourite
     * @throws ActionExceptions.NotWatchedException if the user hasn't watched the video
     */
    public void addFavourite(final String videoTitle)
            throws ActionExceptions.EntryNotFoundException,
            ActionExceptions.AlreadyFavouriteException,
            ActionExceptions.NotWatchedException {
        // Check the video's existence within the database
        if (Database.getInstance().retrieveEntity(Video.class, videoTitle) == null) {
            throw new ActionExceptions.EntryNotFoundException();
        }

        // Check if the video has been watched
        if (hasNotWatched(videoTitle)) {
            throw new ActionExceptions.NotWatchedException();
        }

        // Check if the video has already been added to favourites
        if (favourites.contains(videoTitle)) {
            throw new ActionExceptions.AlreadyFavouriteException();
        }

        // If it exists and has been watched, add it to the favourites list
        favourites.add(videoTitle);
    }

    /**
     * Increments the view count for the given video.
     * @param videoTitle the title of the video
     * @return the new view count
     * @throws ActionExceptions.EntryNotFoundException if the video is not found in the database
     */
    public int addView(final String videoTitle)
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

    /**
     * Adds another rating to the given ratings counter.
     */
    public void incrementRatingCount() {
        ++ratingCount;
    }

    /**
     * Gets the primary key for the database, the username.
     * @return a string containing the username
     */
    @Override
    public String getKey() {
        return username;
    }

    /**
     * Prints the username.
     * @return a string containing the username
     */
    @Override
    public String toString() {
        return username;
    }
}
