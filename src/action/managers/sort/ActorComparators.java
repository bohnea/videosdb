package action.managers.sort;

import actor.Actor;

import java.util.Comparator;

public final class ActorComparators {
    private ActorComparators() { }

    // Used for comparing actor names
    public static final Comparator<Actor> NAME_COMPARATOR =
            Comparator.comparing(Actor::getName);

    // Used for comparing the mean rating of the videos an actor has cast in
    public static final Comparator<Actor> MEAN_RATING_COMPARATOR =
            Comparator.comparingDouble(Actor::getMeanCastInVideoRatings);

    // Used for comparing actor award counts
    public static final Comparator<Actor> AWARD_COUNT_COMPARATOR =
            Comparator.comparingInt(Actor::getAwardCount);
}
