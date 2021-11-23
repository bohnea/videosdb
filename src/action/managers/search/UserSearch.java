package action.managers.search;

import database.Database;
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
}
