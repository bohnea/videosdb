package action;

import action.actions.Command;
import action.actions.Query;
import action.actions.Recommendation;
import common.Constants;
import fileio.ActionInputData;
import utils.Utils;

final class CommandFactory {
    private CommandFactory() { }

    /**
     * Creates a command-type action from an action input.
     * @param actionInput the action read from the input
     * @return an object of type Command
     */
    public static Command createCommand(final ActionInputData actionInput) {
        return new Command(
                actionInput.getActionId(),
                Utils.stringToCommandType(actionInput.getType()),
                actionInput.getUsername(),
                actionInput.getTitle(),
                actionInput.getGrade(),
                actionInput.getSeasonNumber()
        );
    }
}

final class QueryFactory {
    private QueryFactory() { }

    /**
     * Creates a query-type action from an action input.
     * @param actionInput the action read from the input
     * @return an object of type Query
     */
    public static Query createQuery(final ActionInputData actionInput) {
        return new Query(
                actionInput.getActionId(),
                Utils.stringToQueryType(actionInput.getCriteria()),
                Utils.stringToObjectType(actionInput.getObjectType()),
                actionInput.getNumber(),
                actionInput.getFilters(),
                Utils.stringToQuerySortType(actionInput.getSortType())
        );
    }
}

final class RecommendationFactory {
    private RecommendationFactory() { }

    /**
     * Creates a recommendation-type action from an action input.
     * @param actionInput the action read from the input
     * @return an object of type Recommendation
     */
    public static Recommendation createRecommendation(final ActionInputData actionInput) {
        return new Recommendation(
                actionInput.getActionId(),
                Utils.stringToRecommendationType(actionInput.getType()),
                actionInput.getUsername(),
                Utils.stringToGenre(actionInput.getGenre())
        );
    }
}

public final class ActionFactory {
    private ActionFactory() { }

    /**
     * Creates a subclass of action from an action input.
     * @param actionInput the action read from the input
     * @return an object of type Command, Query or Recommendation
     */
    public static Action createAction(final ActionInputData actionInput) {
        return switch (actionInput.getActionType()) {
            case Constants.COMMAND -> CommandFactory.createCommand(actionInput);
            case Constants.QUERY -> QueryFactory.createQuery(actionInput);
            case Constants.RECOMMENDATION ->
                    RecommendationFactory.createRecommendation(actionInput);
            default -> null;
        };
    }
}
