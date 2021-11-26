package common;

public final class ActionExceptions {
    /**
     * Thrown if an entity is not found in the database.
     */
    public static class EntryNotFoundException extends Exception {
        public EntryNotFoundException() { }
    }

    /**
     * Thrown if a video has already been added to the favourites list.
     */
    public static class AlreadyFavouriteException extends Exception {
        public AlreadyFavouriteException() { }
    }

    /**
     * Thrown if a video has already been rated.
     */
    public static class AlreadyRatedException extends Exception {
        public AlreadyRatedException() { }
    }

    /**
     * Thrown if the video has not been watched.
     */
    public static class NotWatchedException extends Exception {
        public NotWatchedException() { }
    }
}
