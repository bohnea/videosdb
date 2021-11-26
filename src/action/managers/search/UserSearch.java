package action.managers.search;

import database.Database;
import database.DatabaseTrackable;
import user.User;

import java.util.List;

public final class UserSearch {
    private UserSearch() { }

    /**
     * Retrieves all users from the database.
     * @return the list of users
     */
    public static List<User> getAllUsers() {
        return Database.getInstance()
                .retrieveClassEntities(User.class) // Retrieve the User entries from the database
                .values().stream() // Get all the users and stream them
                .map(databaseTrackable -> (User) databaseTrackable) // Cast them to User
                .toList(); // Make it into a list
    }

    /**
     * Retrieves a user from the database, given the username.
     * @param username the user to retrieve
     * @return the retrieved user
     */
    public static User getUserByUsername(final String username) {
        // Retrieve the given user from the database
        DatabaseTrackable user = Database.getInstance().retrieveEntity(User.class, username);

        // Check if it's the correct type
        if (!(user instanceof User)) {
            return null;
        }

        // Return the user
        return (User) user;
    }
}
