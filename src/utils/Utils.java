package utils;

import action.actions.Command;
import action.actions.Query;
import action.actions.Recommendation;
import actor.ActorsAwards;
import common.Constants;
import entertainment.Genre;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * The class contains static methods that helps with parsing.
 *
 * We suggest you add your static methods here or in a similar class.
 */
public final class Utils {
    /**
     * for coding style
     */
    private Utils() {
    }

    /**
     * Transforms a string into an enum
     * @param genre of video
     * @return an Genre Enum
     */
    public static Genre stringToGenre(final String genre) {
        if (genre == null) {
            return null;
        }

        return switch (genre.toLowerCase()) {
            case "action" -> Genre.ACTION;
            case "adventure" -> Genre.ADVENTURE;
            case "drama" -> Genre.DRAMA;
            case "comedy" -> Genre.COMEDY;
            case "crime" -> Genre.CRIME;
            case "romance" -> Genre.ROMANCE;
            case "war" -> Genre.WAR;
            case "history" -> Genre.HISTORY;
            case "thriller" -> Genre.THRILLER;
            case "mystery" -> Genre.MYSTERY;
            case "family" -> Genre.FAMILY;
            case "horror" -> Genre.HORROR;
            case "fantasy" -> Genre.FANTASY;
            case "science fiction" -> Genre.SCIENCE_FICTION;
            case "action & adventure" -> Genre.ACTION_ADVENTURE;
            case "sci-fi & fantasy" -> Genre.SCI_FI_FANTASY;
            case "animation" -> Genre.ANIMATION;
            case "kids" -> Genre.KIDS;
            case "western" -> Genre.WESTERN;
            case "tv movie" -> Genre.TV_MOVIE;
            default -> null;
        };
    }

    /**
     * Transforms an entire list of strings to the corresponding genre.
     * @param genres the list of genres
     * @return the list of type Genre
     */
    public static List<Genre> stringListToGenreList(final List<String> genres) {
        // For each element in the list, map it to its Genre equivalent
        return genres.stream()
                .map(Utils::stringToGenre)
                .toList();
    }

    /**
     * Transforms a string into an enum
     * @param award for actors
     * @return an ActorsAwards Enum
     */
    public static ActorsAwards stringToAwards(final String award) {
        return switch (award) {
            case "BEST_SCREENPLAY" -> ActorsAwards.BEST_SCREENPLAY;
            case "BEST_SUPPORTING_ACTOR" -> ActorsAwards.BEST_SUPPORTING_ACTOR;
            case "BEST_DIRECTOR" -> ActorsAwards.BEST_DIRECTOR;
            case "BEST_PERFORMANCE" -> ActorsAwards.BEST_PERFORMANCE;
            case "PEOPLE_CHOICE_AWARD" -> ActorsAwards.PEOPLE_CHOICE_AWARD;
            default -> null;
        };
    }

    /**
     * Transforms a string into an enum
     * @param subscriptionType of user
     * @return a User.SubscriptionType Enum
     */
    public static User.SubscriptionType stringToSubscriptionType(final String subscriptionType) {
        return switch (subscriptionType) {
            case "BASIC" -> User.SubscriptionType.BASIC;
            case "PREMIUM" -> User.SubscriptionType.PREMIUM;
            default -> null;
        };
    }

    /**
     * Transforms a string into an enum
     * @param commandType of command
     * @return a Command.Type Enum
     */
    public static Command.Type stringToCommandType(final String commandType) {
        return switch (commandType) {
            case "favorite" -> Command.Type.FAVOURITE;
            case "view" -> Command.Type.VIEW;
            case "rating" -> Command.Type.RATING;
            default -> null;
        };
    }

    /**
     * Transforms a string into an enum
     * @param queryType of query
     * @return a Query.Type Enum
     */
    public static Query.Type stringToQueryType(final String queryType) {
        return switch (queryType) {
            case "average" -> Query.Type.AVERAGE;
            case "awards" -> Query.Type.AWARDS;
            case "filter_description" -> Query.Type.FILTER_DESCRIPTION;
            case "ratings" -> Query.Type.RATING;
            case "favorite" -> Query.Type.FAVOURITE;
            case "longest" -> Query.Type.LONGEST;
            case "most_viewed" -> Query.Type.MOST_VIEWED;
            case "num_ratings" -> Query.Type.NUMBER_OF_RATINGS;
            default -> null;
        };
    }

    /**
     * Transforms a string into an enum
     * @param querySortType of query sort method
     * @return a Query.SortType Enum
     */
    public static Query.SortType stringToQuerySortType(final String querySortType) {
        return switch (querySortType) {
            case "asc" -> Query.SortType.ASCENDING;
            case "desc" -> Query.SortType.DESCENDING;
            default -> null;
        };
    }

    /**
     * Transforms a string into an enum
     * @param recommendationType of recommendation
     * @return a Recommendation.Type Enum
     */
    public static Recommendation.Type stringToRecommendationType(final String recommendationType) {
        return switch (recommendationType) {
            case "standard" -> Recommendation.Type.STANDARD;
            case "best_unseen" -> Recommendation.Type.BEST_UNSEEN;
            case "popular" -> Recommendation.Type.POPULAR;
            case "favorite" -> Recommendation.Type.FAVOURITE;
            case "search" -> Recommendation.Type.SEARCH;
            default -> null;
        };
    }

    /**
     * Transforms an array of JSON's into an array of strings
     * @param array of JSONs
     * @return a list of strings
     */
    public static ArrayList<String> convertJSONArray(final JSONArray array) {
        if (array != null) {
            ArrayList<String> finalArray = new ArrayList<>();
            for (Object object : array) {
                finalArray.add((String) object);
            }
            return finalArray;
        } else {
            return null;
        }
    }

    /**
     * Transforms an array of JSON's into a map
     * @param jsonActors array of JSONs
     * @return a map with ActorsAwardsa as key and Integer as value
     */
    public static Map<ActorsAwards, Integer> convertAwards(final JSONArray jsonActors) {
        Map<ActorsAwards, Integer> awards = new LinkedHashMap<>();

        for (Object iterator : jsonActors) {
            awards.put(stringToAwards((String) ((JSONObject) iterator).get(Constants.AWARD_TYPE)),
                    Integer.parseInt(((JSONObject) iterator).get(Constants.NUMBER_OF_AWARDS)
                            .toString()));
        }

        return awards;
    }

    /**
     * Transforms an array of JSON's into a map
     * @param movies array of JSONs
     * @return a map with String as key and Integer as value
     */
    public static Map<String, Integer> watchedMovie(final JSONArray movies) {
        Map<String, Integer> mapVideos = new LinkedHashMap<>();

        if (movies != null) {
            for (Object movie : movies) {
                mapVideos.put((String) ((JSONObject) movie).get(Constants.NAME),
                        Integer.parseInt(((JSONObject) movie).get(Constants.NUMBER_VIEWS)
                                .toString()));
            }
        } else {
            System.out.println("NU ESTE VIZIONAT NICIUN FILM");
        }

        return mapVideos;
    }
}
