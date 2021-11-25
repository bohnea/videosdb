package action.actions;

import action.Action;
import action.managers.search.UserSearch;
import action.managers.search.VideoSearch;
import database.Database;
import database.DatabaseTrackable;
import entertainment.Video;
import user.User;
import common.ActionExceptions;

public class Command extends Action {
    public enum Type {
        FAVOURITE,
        VIEW,
        RATING
    }

    private final Type type;
    private final String username;
    private final String title;
    private final double grade;
    private final int seasonNumber;

    public Command(int id, Type type, String username, String title, double grade, int seasonNumber) {
        super(id);
        this.type = type;
        this.username = username;
        this.title = title;
        this.grade = grade;
        this.seasonNumber = seasonNumber;
    }

    private void favourite(StringBuilder message, User user)
            throws ActionExceptions.EntryNotFoundException,
            ActionExceptions.AlreadyFavouriteException,
            ActionExceptions.NotWatchedException {
        // Add the video to favourites
        user.addFavourite(title);

        // If successful, create the success message
        message.append("success -> ").append(title).append(" was added as favourite");
    }

    private void view(StringBuilder message, User user)
            throws ActionExceptions.EntryNotFoundException {
        // View the video
        int views = user.addView(title);

        // If successful, create the success message
        message.append("success -> ").append(title).append(" was viewed with total views of ").append(views);
    }

    private void rating(StringBuilder message, User user, Video video)
            throws ActionExceptions.EntryNotFoundException,
            ActionExceptions.NotWatchedException,
            ActionExceptions.AlreadyRatedException {
        // Rate the video
        video.addRating(grade, seasonNumber, (User) user);

        // Increase user rating count
        user.incrementRatingCount();

        // If successful, create the success message
        message.append("success -> ").append(title).append(" was rated with ")
                .append(grade).append(" by ").append(username);
    }

    @Override
    public String execute() {
        // Construct the return string
        StringBuilder message = new StringBuilder();

        // Get the user/video by the username/title from the database
        User user = UserSearch.getUserByUsername(username);
        Video video = VideoSearch.getVideoByTitle(title);

        try {
            // Do the desired action
            switch (type) {
                case FAVOURITE -> favourite(message, user);
                case VIEW -> view(message, user);
                case RATING -> rating(message, user, video);
            }
        } catch(ActionExceptions.EntryNotFoundException e) {
            message.append("error -> ").append(username).append(" not found in the database");
        } catch(ActionExceptions.AlreadyFavouriteException e) {
            message.append("error -> ").append(title).append(" is already in favourite list");
        } catch(ActionExceptions.AlreadyRatedException e) {
            message.append("error -> ").append(title).append(" has been already rated");
        } catch (ActionExceptions.NotWatchedException e) {
            message.append("error -> ").append(title).append(" is not seen");
        }

        return message.toString();
    }
}