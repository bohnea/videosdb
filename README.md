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
 * retrieve
 * clear


## Database Trackables

The database trackable objects are the ones that can be stored in the database. Those objects are of type User, Video
(and its subclasses), Actor and Action (and its subclasses).


## Actions
### Actions
### Managers


## Additional Remarks

A few java features used in the creation of this project were new to me, so I would love some feedback on their design
(alongside feedback on the general design of the project - package structure, dependencies, possible design patterns
etc.)

Some examples of java features include:
 * the complicated database object in the class with the same name (not even sure if that was a good idea, but it was 
a great challenge)
 * the exceptions used as error codes for the commands
