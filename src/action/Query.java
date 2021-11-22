package action;

public class Query extends Action {
    public enum ObjectType {
        ACTORS,
        MOVIES,
        USERS
    }

    public enum Filter {
        YEAR,
        GENRE,
        WORDS,
        AWARDS
    }

    public enum SortType {
        ASCENDING,
        DESCENDING
    }

    public Query(Integer id) {
        super(id);
    }

    @Override
    public String execute() {
        // System.out.println("Executing Query: " + this);
        return "hi";
    }
}
