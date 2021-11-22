package action;

import database.Database;
import database.DatabaseTrackable;
import user.User;
import utils.ActionExceptions;

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

    private String favourite() {
        // Construct the return string
        StringBuilder message = new StringBuilder();

        // Get the user by the username from the database
        DatabaseTrackable user = Database.getInstance().retrieveEntity(User.class, username);

        // For safety, check if the user is of the correct type
        if (!(user instanceof User)) {
            throw new ClassCastException();
        }

        try {
            // Cast the result to User and add the video to favourites
            ((User) user).addFavourite(title);

            // If successful, create the success message
            message.append("success -> ").append(title).append(" was added as favourite");
        } catch(ActionExceptions.EntryNotFoundException e) {
            message.append("error -> ").append(title).append(" not found in the database");
        } catch(ActionExceptions.AlreadyExistsException e) {
            message.append("error -> ").append(title).append(" is already in favourite list");
        } catch (ActionExceptions.NotWatchedException e) {
            message.append("error -> ").append(title).append(" is not seen");
        }

        return message.toString();
    }

    private String view() {
        return "hi";
    }

    private String rating() {
        return "hi";
    }

    @Override
    public String execute() {
        return switch (type) {
            case FAVOURITE -> favourite();
            case VIEW -> view();
            case RATING -> rating();
        };
    }
}