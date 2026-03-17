package edu.oregonstate.cs492.finalProject.ui

import android.os.Bundle
import android.view.Menu
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import edu.oregonstate.cs492.finalProject.R

/*
 * Often, we'll have sensitive values associated with our code, like API keys, that we'll want to
 * keep out of our git repo, so random GitHub users with permission to view our repo can't see them.
 * The OpenWeather API key is like this.  We can keep our API key out of source control using the
 * technique described below.  Note that values configured in this way can still be seen in the
 * app bundle installed on the user's device, so this isn't a safe way to store values that need
 * to be kept secret at all costs.  This will only keep them off of GitHub.
 *
 * The Gradle scripts for this app are set up to read your API key from a special Gradle file
 * that lives *outside* your project directory.  This file called `gradle.properties`, and it
 * should live in your GRADLE_USER_HOME directory (this will usually be `$HOME/.gradle/` in
 * MacOS/Linux and `$USER_HOME/.gradle/` in Windows).  To store your API key in `gradle.properties`,
 * make sure that file exists in the correct location, and then add the following line:
 *
 *   OPENWEATHER_API_KEY="<put_your_own_OpenWeather_API_key_here>"
 *
 * If your API key is stored in that way, the Gradle build for this app will grab it and write it
 * into the string resources for the app with the resource name "openweather_api_key".  You'll be
 * able to access your key in the app's Kotlin code the same way you'd access any other string
 * resource, e.g. `getString(R.string.openweather_api_key)`.  This is what's done in the code below
 * when the OpenWeather API key is needed.
 *
 * If you don't mind putting your OpenWeather API key on GitHub, then feel free to just hard-code
 * it in the app. 🤷‍
 */

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfig: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment
        val navController = navHostFragment.navController

        appBarConfig = AppBarConfiguration(navController.graph, drawerLayout)

        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setupWithNavController(navController)

//        val subMenu = navView.menu.findItem(R.id.saved_cities).subMenu
//        subMenu?.clear()
//
//        savedCitiesViewModel.savedCities.observe(this) { cities ->
//            subMenu?.clear()
//            val prefsEditor = PreferenceManager.getDefaultSharedPreferences(this).edit()
//
//            var id: Int = 0
//            for (city in cities) {
//                val menuItem = subMenu?.add(
//                    Menu.NONE,
//                    id,
//                    Menu.NONE,
//                    "${city.name} (${city.lat}, ${city.lon})")
//                id++
//
//                menuItem?.setOnMenuItemClickListener {
//                    prefsEditor.putString(getString(R.string.pref_lat_key), city.lat.toString())
//                    prefsEditor.putString(getString(R.string.pref_lon_key), city.lon.toString())
//                    prefsEditor.apply()
//
//                    navController.navigate(R.id.current_weather)
//
//                    drawerLayout.closeDrawers()
//                    true
//                }
//            }
//        }


        val appBar: MaterialToolbar = findViewById(R.id.top_app_bar)
        setSupportActionBar(appBar)
        setupActionBarWithNavController(navController, appBarConfig)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }
}