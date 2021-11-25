package action.managers.search;

import actor.Actor;
import database.Database;

import java.util.List;

public class ActorSearch {
    public static List<Actor> getAllActors() {
        return Database.getInstance()
                .retrieveClassEntities(Actor.class) // Retrieve the Actor entries from the database
                .values().stream() // Get all the videos
                .map(databaseTrackable -> (Actor) databaseTrackable) // Cast them to Actor
                .toList(); // Make it into a list
    }
}
