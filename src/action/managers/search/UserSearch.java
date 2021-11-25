package action.managers.search;

import database.Database;
import database.DatabaseTrackable;
import user.User;

import java.util.List;

public class UserSearch {
    public static List<User> getAllUsers() {
        return Database.getInstance()
                .retrieveClassEntities(User.class) // Retrieve the User entries from the database
                .values().stream() // Get all the videos
                .map(databaseTrackable -> (User) databaseTrackable) // Cast them to User
                .toList(); // Make it into a list
    }

    public static User getUserByUsername(String username) {
        // Retrieve the given video from the database
        DatabaseTrackable user = Database.getInstance().retrieveEntity(User.class, username);

        // Check if it's the correct type
        if (!(user instanceof User)) {
            return null;
        }

        // Return the video
        return (User) user;
    }
}
