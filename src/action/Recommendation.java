package action;

public class Recommendation extends Action {
    public Recommendation(Integer id) {
        super(id);
    }

    @Override
    public String execute() {
        // System.out.println("Executing Recommendation: " + this);
        return "hi";
    }
}
