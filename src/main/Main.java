package main;

import actor.Actor;
import checker.Checkstyle;
import checker.Checker;
import common.Constants;
import database.Database;
import entertainment.Movie;
import entertainment.Show;
import entertainment.Video;
import fileio.*;
import org.json.simple.JSONArray;
import user.User;
import utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        // Read the movies
        List<MovieInputData> movieInput = input.getMovies();
        ArrayList<Movie> movies = new ArrayList<>();

        // For each movie in the input list, create a new Movie and add it to the movies list
        movieInput.forEach(movie -> movies.add(new Movie(
                movie.getTitle(),
                movie.getYear(),
                movie.getDuration(),
                Utils.stringListToGenreList(movie.getGenres()),
                movie.getCast()
        )));

        // Read the shows
        List<SerialInputData> serialInput = input.getSerials();
        ArrayList<Show> shows = new ArrayList<>();

        // For each show in the input list, create a new Show and add it to the shows list
        serialInput.forEach(show -> shows.add(new Show(
                show.getTitle(),
                show.getYear(),
                Utils.stringListToGenreList(show.getGenres()),
                show.getCast(),
                show.getSeasons()
        )));

        // Add everything to the database
        Database.getInstance().add(actors);
        Database.getInstance().add(users);
        Database.getInstance().add(movies);
        Database.getInstance().add(shows);
    }

    private static void readAndStoreActions(Input input) {
        // Read the actions
        List<ActionInputData> actionInput = input.getCommands();
        actionInput.forEach(action -> action.)
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

        // Debugging :)
        Database.getInstance().printAllKeys();
        System.out.println();
        Database.getInstance().printAllKeysAndValues();



        fileWriter.closeJSON(arrayResult);
    }
}
