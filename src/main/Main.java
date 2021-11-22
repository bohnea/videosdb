package main;

import action.Action;
import actor.Actor;
import checker.Checkstyle;
import checker.Checker;
import common.Constants;
import database.Database;
import database.DatabaseTrackable;
import entertainment.Movie;
import entertainment.Show;
import entertainment.Video;
import fileio.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import user.User;
import utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * The entry point to this homework. It runs the checker that tests your implementation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * Call the main checker and the coding style checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }

    private static void readAndStoreInput(Input input) {
        // Read the actors
        List<ActorInputData> actorInput = input.getActors();
        ArrayList<Actor> actors = new ArrayList<>();

        // For each actor in the input list, create a new Actor and add them to the actors list
        actorInput.forEach(actor -> actors.add(new Actor(
                actor.getName(),
                actor.getCareerDescription(),
                actor.getFilmography(),
                actor.getAwards()
        )));

        // Read the users
        List<UserInputData> userInput = input.getUsers();
        ArrayList<User> users = new ArrayList<>();

        // For each user in the input list, create a new User and add them to the users list
        userInput.forEach(user -> users.add(new User(
                user.getUsername(),
                Utils.stringToSubscriptionType(user.getSubscriptionType()),
                user.getFavoriteMovies(),
                user.getHistory()
        )));

        // Read the videos
        ArrayList<Video> videos = new ArrayList<>();

        // Read the movies
        List<MovieInputData> movieInput = input.getMovies();

        // For each movie in the input list, create a new Movie and add it to the movies list
        movieInput.forEach(movie -> videos.add(new Movie(
                movie.getTitle(),
                movie.getYear(),
                movie.getDuration(),
                Utils.stringListToGenreList(movie.getGenres()),
                movie.getCast()
        )));

        // Read the shows
        List<SerialInputData> serialInput = input.getSerials();

        // For each show in the input list, create a new Show and add it to the shows list
        serialInput.forEach(show -> videos.add(new Show(
                show.getTitle(),
                show.getYear(),
                Utils.stringListToGenreList(show.getGenres()),
                show.getCast(),
                show.getSeasons()
        )));

        // Add everything to the database
        Database.getInstance().add(actors);
        Database.getInstance().add(users);
        Database.getInstance().add(videos, Video.class);
    }

    private static void readAndStoreActions(Input input) {
        // Read the actions
        List<ActionInputData> actionInputList = input.getCommands();

        // For each input action, create a new Action object with the help of the ActionFactory
        List<Action> actions = actionInputList.stream()
                .map(ActionFactory::createAction)
                .toList();

        // Store the actions in the database
        Database.getInstance().add(actions, Action.class);
    }

    private static void executeActionsAndWriteOutput(Writer fileWriter, JSONArray arrayResult) {
        // Retrieve the actions from the database
        LinkedHashMap<String, DatabaseTrackable> actions;
        actions = Database.getInstance().retrieveClassEntities(Action.class);

        // Iterate though each action and execute it
        for (DatabaseTrackable action : actions.values()) {
            // Safety instanceof, I don't think this is needed but just to be sure
            if (!(action instanceof Action)) {
                continue;
            }

            // Execute the action and keep the output
            String actionOutput = ((Action) action).execute();

            try {
                // Write the output to the file
                JSONObject jsonObj = fileWriter.writeFile(((Action) action).getID(), "", actionOutput);
                arrayResult.add(jsonObj);
            } catch(IOException e) {
                // Do nothing
            }
        }
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();

        // Read the input and store the information in the database
        readAndStoreInput(input);

        // Read the input actions and store them in the database
        readAndStoreActions(input);

        // Go through each action, in order, and execute it
        executeActionsAndWriteOutput(fileWriter, arrayResult);

        // Clear the database
        Database.getInstance().clear();

        fileWriter.closeJSON(arrayResult);
    }
}
