# Assignment 4

**→ Preferred deadline: 11:59pm on Monday, 3/9/2026** <br/>
**→ Extension deadline: 11:59pm on Wednesday, 3/11/2026 (no submissions allowed after this)** <br/>
**→ Grading demo due: 11:59pm on Friday 3/20/2026**

In this assignment, we'll put the finishing touches on our weather app by using the [Room persistence library](https://developer.android.com/training/data-storage/room) to incorporate an SQLite database into the app to save the locations for which the user requests a forecast.  You'll also add a navigation drawer that will display a list of previously requested cities so the user can more easily get the forecasts for the cities that are most improtant to them.  There are a few different tasks associated with this assignment, described below.

The complete starter code for this assignment (which will be fully released after the extension deadline for the previous assignment) implements a weather app with three screens (a "current weather" screen, a "five-day forecast" screen, and a settings screen) tied together using the Jetpack Navigation Component.  Feel free to use this starter code as the basis upon which to write your code for this assignment.  Alternatively, if you liked your own implementation from assignment 3, you can start with that instead of the provided starter code.

## 1. Use the Room persistence library to save forecast locations

Your first task is to use the Room persistence library to incorporate an SQLite database into your app.  Whenever a forecast is fetched for a new city, you'll save a representation of that city in your database.  You'll want to store a few different pieces of data for each city in the database:

  * The name of the city (e.g. "Corvallis").  You'll use this to list the city in the navigation drawer.

  * The city's latitude and longitude.  You'll need these to be able to fetch up-to-date forecast data for the city when the user selects it in the navigation drawer.

  * A timestamp indicating when the user last viewed a forecast of this city (we'll see in a minute how this is used).  You can represent this as a `Long` value and, when the time comes, set it using Java's [`System.currentTimeMillis()`](https://docs.oracle.com/javase/8/docs/api/java/lang/System.html#currentTimeMillis--).

To make this work with the Room persistence library, you'll need to define an Entity, a Data Access Object (DAO), and a Database class.  When defining the Entity, it may be possible to repurpose one of the existing data classes from the JSON parsing setup.

> Hint: For later parts of the assignment, it will be helpful if cities are uniquely identified by the combination of latitude and longitude.  A [composite primary key](https://developer.android.com/training/data-storage/room/defining-data#composite-key) may be useful for this.

## 2. Save the cities for which the user requests a forecast

Next, modify your app so that whenever the forecast is fetched for a new city, that new city is saved into the database you just created.  A given city should be stored at most once in the database.  In other words, your database should not contain duplicate cities.

> Hint: If you're using a composite primary key as suggested above, setting [an `OnConflictStragety` in your `@Insert` query](https://developer.android.com/reference/kotlin/androidx/room/Insert#onconflict) can make it easy to avoid duplicate database entries.

One thing you'll want to think about is when to actually store the new city in the database.  You'll need to do this when you have all the required information available, including the city's name.  Can you find a spot in the starter code that works well for this?

Once you have your app hooked up to save cities in the database, use [Android Studio's Database Inspector](https://developer.android.com/studio/inspect/database) to verify that the data is being stored correctly.

## 3. Add a navigation drawer to the app

Next, use [the `NavigationUI`](https://developer.android.com/guide/navigation/navigation-ui) to provide [a navigation drawer](https://developer.android.com/guide/navigation/navigation-ui#add_a_navigation_drawer) the user can use to navigate between destinations in your app.  Initially, the navigation drawer only needs to contain working links to the "current weather" screen, the "five-day forecast" screen, and the settings screen.

## 4. Add a list of saved cities to the navigation drawer

The next task represents somewhat of a challenge that goes a little beyond what we covered in class.  Here, you'll modify your navigation drawer to list the names of all of the cities saved in the database (in addition to displaying links to your app's different screens).  Importantly, you should display the names of the cities *ordered from most-recently viewed to least-recently viewed* based on the timestamps stored in the database.

You can incorporate this list of city names directly into a `NavigationView` if that's what you're using to provide content for your navigation drawer.  The reason this task is somewhat of a challenge is because you'll have to do this a little differently than the way we specified `NavigationView` content in lecture.  Specifically, in class, we specified the static `NavigationView` content using an XML menu file.  For this app, you'll need to provide dynamic `NavigationView` content based on the cities stored in your database.

Luckily, you can use a `NavigationView`'s [`getMenu()` method](https://developer.android.com/reference/com/google/android/material/navigation/NavigationView#getMenu()) to programmatically access and manipulate the `Menu` specifying the `NavigationView` content.  You should use the [`Menu` class's documentation](https://developer.android.com/reference/kotlin/android/view/Menu) to figure out how to programatically add an item to the `NavigationView` for each city stored in your database.  This will likely be easiest if all of the cities in the `NavigationView` live within their own [`SubMenu`](https://developer.android.com/reference/kotlin/android/view/SubMenu).

## 5. Update the forecast when the user clicks on a city in the navigation drawer

Finally, hook your app up so that when the user clicks on an city in your navigation drawer, the following four actions occur:
  * The navigation drawer closes.
  * The app navigates to the "current weather" screen and fetches and displays the current weather for the location corresponding to the item that was clicked.  For example, if the user clicks "Philadelphia" in the navigation drawer, the app should display the current weather for Philadelphia.
  * The "last viewed" timestamp is updated to "now" in the database for the location corresponding to the item that was clicked.
  * The latitude and longitude settings are updated to the coordinates of the city that was clicked.  This will ensure that the forecast for the correct city is displayed if the user navigates to the "five-day forecast" screen.  In other words, if the user clicks "Philadelphia" in the navigation drawer and then navigates to the "five-day forecast" screen, they should see the forecast for Philadelphia.

There are various ways you could accomplish this.  Again, try to use the [`Menu` class's documentation](https://developer.android.com/reference/kotlin/android/view/Menu) to figure out how to respond when a city item is clicked.

> Hint: the most straightforward way to update the "city" setting is use a [`SharedPreferences.Editor`](https://developer.android.com/reference/kotlin/android/content/SharedPreferences#edit) (making sure to [apply the changes](https://developer.android.com/reference/kotlin/android/content/SharedPreferences.Editor#apply)).

## Extra credit #1: A better way to enter a new city

The flow we implemented above works alright to allow the user to add cities to the database and select them to view their weather forecasts, but the user's experience of having to enter a city in the app's settings in order to be able to select that city in the navigation drawer is a poor one, since the city's geocoordinates must be entered on a different screen than the one where the weather forecast is displayed.

For extra credit, you can implement a better user experience by adding more UI components for fetching the weather for a new city that allows them to enter a city's geocoordinates without navigating to a different screen.  There are a few possibilities here:

  * Add an element (e.g. a clickable entry with a "+" sign) to the navigation drawer that the user can click to [open a dialog](https://developer.android.com/guide/topics/ui/dialogs) to enter the geocoordinates for a new city to fetch the weather for.

  * Add a [floating action button](https://developer.android.com/guide/topics/ui/floating-action-button) the user can use to [open a dialog](https://developer.android.com/guide/topics/ui/dialogs) enter the geocoordinates for a new city to fetch the weather for.

  * Use the [Material Design `SearchBar` component](https://github.com/material-components/material-components-android/blob/master/docs/components/Search.md) to turn the top app bar into a search box.  This will be somewhat complicated, since you can only enter one string in the search bar, though you need to collect two values from the user (i.e. latitude and longitude).  You can use some basic string parsing to accomplish this.  You can earn an extra pat on the back (but no extra points) if you are able use the `SearchView` component to display search suggestions representing recently-searched cities.

## Extra credit #2: Add location awareness to the app

As a second extra credit challenge, you can make your app location-aware by using [Android's location APIs](https://developer.android.com/develop/sensors-and-location/location) to allow the user to get the weather for their current location, based on the device's current GPS reading.

You should implement this by adding a "current location" link to the top of the cities list in your navigation drawer.  When the user clicks this link, it should behave like one of the normal nav drawer city links (i.e. navigating to the "current weather" screen), except it should always fetch weather data based on the device's current GPS location.  If the user selects "current location" and then navigates to the "five-day forecast" screen, it should display the forecast for the current location, just like when the user selects a named city from the nav drawer.

## Withheld starter code

Because this assignment is being released before the extension deadline for the previous assignment, some of the starter code is being temporarily withheld, since it reveals the solution to one or more parts of the previous assignment.  Specifically, the starter code for the app will initially contain the same initial starter code as the previous assignment.

An updated version of the starter code containing a compelte solution to the previous assignment will be released after the extension deadline for the previous assignment has passed.  Instructions about how to incorporate the updated code into your assignment repo will be provided at that point.  If you'd like to begin work on this assignment before the complete version of the starter code is provided, feel free to use your own code from the previous assignment.

## Submission

We'll be using GitHub Classroom for this assignment, and you will submit your assignment via GitHub.  Make sure your completed files are committed and pushed by the assignment's deadline to the main branch of the GitHub repo that was created for you by GitHub Classroom.  A good way to check whether your files are safely submitted is to look at the main branch your assignment repo on the github.com website (i.e. https://github.com/osu-cs492-w26/assignment-4-YourGitHubUsername/). If your changes show up there, you can consider your files submitted.

## Grading criteria

The assignment is worth 100 total points, broken down as follows:

* Room Persistence Library components: 10 points
  * 2 points: A Room Entity class is correctly defined to represent city entries in the database.
  * 5 points: A Room DAO is correctly defined to represent the database queries the app needs to perform.
  * 3 points: A Room Database class is correctly defined to represent the database itself.
    * The Database class must implement the singleton pattern.

* Database storage: 10 points
  * 10 points: New entries are successfully added into the database when the user fetches weather data for new cities.

* Navigation drawer: 30 points
  * 8 points: The app has a navigation drawer that can be used to navigate to all of the app's screens.
  * 8 points: The navigation drawer contains a list of the names of all cities stored in the app's database.
  * 3 points: The cities in the navigation drawer are ordered from most-recently viewed to least-recently viewed.
  * 5 points: When a city in the navigation drawer is selected, the app successfully displays the "current weather" screen for that city.
  * 3 points: The navigation drawer closes when a city is selected from the drawer.
  * 3 points: When the user selects a city from the drawer and then navigates to the "five-day forecast" screen, the forecast for that city is successfully displayed.

* Understanding: 50 points
  * To earn these points, you must demonstrate a strong understanding of all aspects of the assignment and of your own implementation

In addition, you can earn up to 10 points of extra credit by satisfying the following criteria:
  * 5 points: The app has an improved mechanism for entering a new city, as described above.
  * 5 points: The navigation drawer includes a "current location" link that, when selected, displays the weather for the device's current GPS location.
    * Selecting "current location" from the nav drawer should behave similarly to when a named city is selected, i.e. the app should navigate to the "current weather" screen, and if the user navigates to the "five-day forecast" screen, it should display the forecast for the current location.
