package action.managers.sort;

import user.User;

import java.util.Comparator;

public class UserComparators {
    // Used for comparing usernames
    public static final Comparator<User> nameComparator =
            Comparator.comparing(User::getUsername);

    // Used for comparing rating counts
    public static final Comparator<User> ratingCountComparator =
            Comparator.comparingInt(User::getRatingCount);
}
