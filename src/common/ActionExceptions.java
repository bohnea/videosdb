package common;

public final class ActionExceptions {
    public static class EntryNotFoundException extends Exception {
        public EntryNotFoundException() { }
    }

    public static class AlreadyFavouriteException extends Exception {
        public AlreadyFavouriteException() { }
    }

    public static class AlreadyRatedException extends Exception {
        public AlreadyRatedException() { }
    }

    public static class NotWatchedException extends Exception {
        public NotWatchedException() { }
    }
}
