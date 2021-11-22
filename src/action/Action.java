package action;

import database.DatabaseTrackable;

public abstract class Action implements DatabaseTrackable {
    private Integer id;

    public Action(Integer id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public abstract String execute();

    public String getKey() {
        return id.toString();
    }
}
