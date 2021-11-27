**Project by:** Mihnea Tudor \
**Group:** 321CAb

University Politehnica of Bucharest \
Faculty of Computer Science and Automatic Control

# VideosDB

## Project Overview
This project simulates a *database* which keeps track of *videos*, *actors*, *users* and *actions* that can be performed
on the database.
There are three types of actions: *command*, *query* and *recommendation*. Commands can modify the entities stored in
the database, while queries and recommendations can only retrieve information and process it.
Each individual component, along with the general design and the links between entities, will be detailed in the
following sections.


## The Database
The database is the most important part of this project, as it holds the information of all the entities that implement
the DatabaseTrackable interface.
The Database class follows the Singleton pattern, therefore there can only be a single instance of it at a time, and can
be accessed from anywhere in the project by calling ```Database.getInstance()```.
The main object is a private HashMap named 'database', with the key being a class that
extends DatabaseTrackable, and the value being a LinkedHashMap. The latter is another Map that ties the primary key of
an object, represented as a String, to the object itself.

The actions that can be done on the database are the following:
 * add
   * using runtime class - adds a list of objects to the runtime class of the first object
   * using given class - adds a list of objects to the given class

 * retrieve
   * class entities - retrieves all the entities from the database of the given class 
   * entity - retrieves the entity of the given class, by the given key

 * clear
   * clears the entire database


## Main Entities
The main entities of this project are the ones read from input, the ones on which Actions are performed; all main
entities implement the DatabaseTrackable interface.

Those classes are:

### User
 * holds information describing the user
 * some notable fields are:
   * a HashSet for storing the videos added to favourites
   * a HashMap with the key being the video's title, and the value being the number of times the user has watched 
 the video
   * an integer holding the user's rating count

### Actor
 * holds information describing the actor
 * some notable fields are:
    * a HashSet for storing the videos in which the actor has cast in
    * a HashMap with the key being an ActorsAwards enum, and the value being the amount of awards of that type

### Video
 * abstract class holding the information describing all videos
 * extended by:
   * Movie
     * adds additional information specific to movies:
       * a List of user ratings
       * a HashSet for keeping track of the users who've rated the movie
     
   * Show
     * adds additional information specific to shows:
       * an ArrayList of seasons
       * a HashMap, with the key being the season index, and the value being a HashSet of users who've rated that season

### Action
 * this class will be detailed in the next section


## Actions
Actions represent ways to modify the entities in the database, or to retrieve certain information from it. Each action
has a certain ID and are executed in the order they've been added to the database.

The Action class is abstract, containing only the common ID field for all actions, along with an extendable
```execute()``` command that must be implemented in all subclasses. Action is extended by three types of actions,
commands, queries and recommendations, and the execution is done with the help of a few searching and sorting managers.

### Action types
 * Command
   * the actions a user can take - favourite, view or rate a video
   * they can modify the internal structures of users and videos (for example, add videos to a user's history)

 * Query
   * used to retrieve information from the database, based on a filter and sort criteria
   * they do NOT modify the internal structures of entities
   * examples:
     * first N videos sorted by view count
     * actors with certain keywords in their career description
 
 * Recommendation
     * used to retrieve information from the database, more specifically to retrieve certain videos for a specified user
     * they do NOT modify the internal structures of entities
     * examples:
       * the highest rated unwatched video
       * all unwatched videos of a certain genre

### Managers
 * Search managers
   * utility classes that interact with the database and are used to search for information inside other entities
   * there are three search managers in total, one for users, one for actors and one for videos

 * Sort managers
   * the main SortManager class has a generic method that takes in a list of entities and criteria used for sorting
     (the criteria are saved in a SortCriteria object, described below)
     * the generic SortCriteria class, found as an internal class to SortManager, implements the Comparator interface
       and holds a list of comparators of the given generic type, alongside a flag deciding the sort order (ascending 
       / descending)
   * there are three more utility classes - one for users, one for actors and one for videos
     * they contain constant static references to different comparators used for sorting by the main sort manager


## Input / Output
### Input
All input test files can be found in the test_db/test_files folder. A test is a JSON file with information about all
the users, videos, actors and actions in the database. The data is parsed and saved in an Input object, after which
each individual component is created and added to the database.

For the creation of the Action objects, there is a specialized class called ActionFactory, which takes in the input of
a single Action and creates either a Command, a Query or a Recommendation, based on the requested type.

### Output
Each action creates a certain output message containing either the result of the query / recommendation or if the
command was successful. The output is formatted as a JSON object and added to an array of JSON objects, which is then
printed into a file in the result/ folder (or to out.txt, if calling ```main()``` from the Test class).


## Additional Remarks
A few java features used in the creation of this project were new to me, so I would love some feedback on their design
(alongside feedback on the general design of the project - package structure, dependencies, possible design patterns
etc.)

Some examples of java features include:
 * the complicated database object in the class with the same name (not even sure if that was a good idea, but it was 
a great challenge)
 * the exceptions used as error codes for the commands

Also, I have no idea how to avoid the two occurrences of 'instanceof' without creating two new separate lists of Movies
and Shows. I chose to keep 'instanceof' to save a little memory, because I don't believe this way of using it breaks
the rules of OOP too bad, but I'd love to know some alternatives.
