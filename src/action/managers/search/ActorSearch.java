package action.managers.search;

import actor.Actor;
import database.Database;

import java.util.List;

public final class ActorSearch {
    private ActorSearch() { }

    /**
     * Retrieves all actors from the database.
     * @return the list of actors
     */
    public static List<Actor> getAllActors() {
        return Database.getInstance()
                .retrieveClassEntities(Actor.class) // Retrieve the Actor entries from the database
                .values().stream() // Get all the actors and stream them
                .map(databaseTrackable -> (Actor) databaseTrackable) // Cast them to Actor
                .toList(); // Make it into a list
    }
}
