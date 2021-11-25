package action.managers.sort;

import actor.Actor;

import java.util.Comparator;

public class ActorComparators {
    // Used for comparing actor names
    public static final Comparator<Actor> nameComparator =
            Comparator.comparing(Actor::getName);

    // Used for comparing the mean rating of the videos an actor has cast in
    public static final Comparator<Actor> meanRatingComparator =
            Comparator.comparingDouble(Actor::getMeanCastInVideoRatings);

    // Used for comparing actor award counts
    public static final Comparator<Actor> awardCountComparator =
            Comparator.comparingInt(Actor::getAwardCount);
}
