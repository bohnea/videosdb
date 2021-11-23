package action;

import action.actions.Command;
import action.actions.Query;
import action.actions.Recommendation;
import common.Constants;
import fileio.ActionInputData;
import utils.Utils;

class CommandFactory {
    public static Command createCommand(ActionInputData actionInput) {
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

class QueryFactory {
    public static Query createQuery(ActionInputData actionInput) {
        return new Query(
                actionInput.getActionId(),
                Utils.stringToQueryType(actionInput.getCriteria()),
                actionInput.getNumber(),
                actionInput.getFilters(),
                Utils.stringToQuerySortType(actionInput.getSortType())
        );
    }
}

class RecommendationFactory {
    public static Recommendation createRecommendation(ActionInputData actionInput) {
        return new Recommendation(
                actionInput.getActionId(),
                Utils.stringToRecommendationType(actionInput.getType()),
                actionInput.getUsername(),
                Utils.stringToGenre(actionInput.getGenre())
        );
    }
}

public class ActionFactory {
    public static Action createAction(ActionInputData actionInput) {
        return switch (actionInput.getActionType()) {
            case Constants.COMMAND -> CommandFactory.createCommand(actionInput);
            case Constants.QUERY -> QueryFactory.createQuery(actionInput);
            case Constants.RECOMMENDATION -> RecommendationFactory.createRecommendation(actionInput);
            default -> null;
        };
    }
}
