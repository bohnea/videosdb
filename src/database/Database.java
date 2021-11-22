package database;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class Database {
    /**
     * The Singleton instance.
     */
    private static Database instance = null;

    private Database() { }

    /**
     * Gets the instance of the Database Singleton, if it exists, or creates a new one.
     * @return the Singleton instance
     */
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }

        return instance;
    }

    /**
     * The database containing, for each class that extends DatabaseTrackable, a HashMap storing the entities
     * of the respective class.
     */
    private final HashMap<Class<? extends DatabaseTrackable>,
            LinkedHashMap<String, DatabaseTrackable>> database = new HashMap<>();

    /**
     * Retrieves the HashMap of the given class from the database, or creates a new one if necessary.
     * @param key the class to retrieve from the database
     * @return the HashMap storing the requested values
     */
    private LinkedHashMap<String, DatabaseTrackable> retrieveClassEntities(Class<? extends DatabaseTrackable> key) {
        if (!database.containsKey(key)) {
            database.put(key, new LinkedHashMap<>());
        }

        return database.get(key);
    }

    /**
     * Adds the given entities to the given class in the database, storing them in the appropriate HashMap.
     * Useful when you have multiple subclasses of a class extending DatabaseTrackable and want to put
     * all of them in a single database entry.
     * @param entities the list of entities to be stored in the database
     * @param cls the class where to store the entities in the database
     */
    public void add(List<? extends DatabaseTrackable> entities, Class<? extends DatabaseTrackable> cls) {
        // If the entities list is empty, do nothing
        if (entities.isEmpty()) {
            return;
        }

        // Get the database map of the given class
        LinkedHashMap<String, DatabaseTrackable> entityMap = retrieveClassEntities(cls);

        // Add the entities to the appropriate hashmap
        entities.forEach(entity -> entityMap.putIfAbsent(entity.getKey(), entity));
    }

    /**
     * Adds the given entities to the corresponding runtime class in the database, storing them in
     * the appropriate HashMap.
     * @param entities the list of entities to be stored in the database
     */
    public void add(List<? extends DatabaseTrackable> entities) {
        // If the entities list is empty, do nothing
        if (entities.isEmpty()) {
            return;
        }

        // Call the add method with the runtime class of the given entities
        add(entities, entities.get(0).getClass());
    }

    public void printAllKeys() {
        System.out.println("Print all keys:");
        System.out.println(database.keySet());
    }

    public void printAllKeysAndValues() {
        System.out.println("Print all keys and values:");
        for (Class<? extends DatabaseTrackable> key : database.keySet()) {
            System.out.println();
            System.out.println(key);

            LinkedHashMap<String, DatabaseTrackable> entityMap = retrieveClassEntities(key);
            for (String str : entityMap.keySet()) {
                System.out.println(str + ": " + entityMap.get(str));
            }
        }
    }
}
