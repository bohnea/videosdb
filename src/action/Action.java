package action;

import database.DatabaseTrackable;

public abstract class Action implements DatabaseTrackable {
    private final Integer id;

    public Action(final Integer id) {
        this.id = id;
    }

    public final int getID() {
        return id;
    }

    /**
     * Abstract method to implement in derived classes. Executes the action's function.
     * @return the action execution result
     */
    public abstract String execute();

    /**
     * Gets the primary key of the Video, its ID.
     * @return the action ID
     */
    public String getKey() {
        return id.toString();
    }
}
