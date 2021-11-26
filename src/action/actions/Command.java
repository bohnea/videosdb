package action.actions;

import action.Action;
import action.managers.search.UserSearch;
import action.managers.search.VideoSearch;
import entertainment.Video;
import user.User;
import common.ActionExceptions;

public class Command extends Action {
    /**
     * Enum containing possible command types.
     */
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

    public Command(final int id, final Type type, final String username,
                   final String title, final double grade, final int seasonNumber) {
        super(id);
        this.type = type;
        this.username = username;
        this.title = title;
        this.grade = grade;
        this.seasonNumber = seasonNumber;
    }

    /**
     * Attempts to add the video stored in the command to the given user's favourites list.
     * Writes a success message if the video is added correctly.
     * @param message the message to add text to
     * @param user the user to add the video to favourites
     * @throws ActionExceptions.EntryNotFoundException if the video is not in the database
     * @throws ActionExceptions.AlreadyFavouriteException if it's already in the favourites list
     * @throws ActionExceptions.NotWatchedException if the video hasn't been watched by the user
     */
    private void favourite(final StringBuilder message, final User user)
            throws ActionExceptions.EntryNotFoundException,
            ActionExceptions.AlreadyFavouriteException,
            ActionExceptions.NotWatchedException {
        // Add the video to favourites
        user.addFavourite(title);

        // If successful, create the success message
        message.append("success -> ").append(title).append(" was added as favourite");
    }

    /**
     * Attempts to add a view from the given user to the video stored in the command.
     * Writes a success message if the video is viewed.
     * @param message the message to add text to
     * @param user the user to view the video
     * @throws ActionExceptions.EntryNotFoundException if the video is not in the database
     */
    private void view(final StringBuilder message, final User user)
            throws ActionExceptions.EntryNotFoundException {
        // View the video
        int views = user.addView(title);

        // If successful, create the success message
        message.append("success -> ").append(title)
                .append(" was viewed with total views of ").append(views);
    }

    /**
     * Attempts to rate the video stored in the command by the given user.
     * Writes a success message if the video is rated correctly.
     * @param message the message to add text to
     * @param user the user to rate the video
     * @param video the video to be rated
     * @throws ActionExceptions.EntryNotFoundException if the video is not in the database
     * @throws ActionExceptions.NotWatchedException if the video hasn't been watched by the user
     * @throws ActionExceptions.AlreadyRatedException if the video has already been rated by user
     */
    private void rating(final StringBuilder message, final User user, final Video video)
            throws ActionExceptions.EntryNotFoundException,
            ActionExceptions.NotWatchedException,
            ActionExceptions.AlreadyRatedException {
        // Rate the video
        video.addRating(grade, seasonNumber, user);

        // Increase user rating count
        user.incrementRatingCount();

        // If successful, create the success message
        message.append("success -> ").append(title).append(" was rated with ")
                .append(grade).append(" by ").append(username);
    }

    /**
     * Executes the command.
     * @return a message with the result of the action execution
     */
    @Override
    public String execute() {
        // Construct the return string
        StringBuilder message = new StringBuilder();

        // Get the user/video by the username/title from the database
        User user = UserSearch.getUserByUsername(username);
        Video video = VideoSearch.getVideoByTitle(title);
        assert user != null;
        assert video != null;

        try {
            // Do the desired action
            switch (type) {
                case FAVOURITE -> favourite(message, user);
                case VIEW -> view(message, user);
                case RATING -> rating(message, user, video);
                default -> { }
            }
        } catch (ActionExceptions.EntryNotFoundException e) {
            message.append("error -> ").append(username).append(" not found in the database");
        } catch (ActionExceptions.AlreadyFavouriteException e) {
            message.append("error -> ").append(title).append(" is already in favourite list");
        } catch (ActionExceptions.AlreadyRatedException e) {
            message.append("error -> ").append(title).append(" has been already rated");
        } catch (ActionExceptions.NotWatchedException e) {
            message.append("error -> ").append(title).append(" is not seen");
        }

        return message.toString();
    }
}
