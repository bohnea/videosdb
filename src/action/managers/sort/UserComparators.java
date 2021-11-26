package action.managers.sort;

import user.User;

import java.util.Comparator;

public final class UserComparators {
    private UserComparators() { }

    // Used for comparing usernames
    public static final Comparator<User> NAME_COMPARATOR =
            Comparator.comparing(User::getUsername);

    // Used for comparing rating counts
    public static final Comparator<User> RATING_COUNT_COMPARATOR =
            Comparator.comparingInt(User::getRatingCount);
}
