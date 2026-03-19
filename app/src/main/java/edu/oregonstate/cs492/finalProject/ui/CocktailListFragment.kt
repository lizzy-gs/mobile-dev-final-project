package edu.oregonstate.cs492.finalProject.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import edu.oregonstate.cs492.finalProject.R
import edu.oregonstate.cs492.finalProject.data.ShortCocktail

class CocktailListFragment: Fragment(R.layout.fragment_cocktail_list) {
    private val viewModel: CocktailListViewModel by viewModels()
    private val cocktailsAdapter = CocktailsAdapter(::onCocktailClick)
    private lateinit var prefs: SharedPreferences
    private lateinit var cocktailListRV: RecyclerView
    private lateinit var categoryTV: TextView
    private lateinit var loadingErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingErrorTV = view.findViewById(R.id.tv_loading_error)
        loadingIndicator = view.findViewById(R.id.loading_indicator)

        categoryTV = view.findViewById(R.id.tv_category)

        /*
        * Set up top app bar
         */
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
                menu.findItem(R.id.action_share)?.isVisible = false
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_settings -> {
                        val directions = CocktailListFragmentDirections.navigateToSettings()
                        findNavController().navigate(directions)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.STARTED)

        /*
         * Set up RecyclerView.
         */
        cocktailListRV = view.findViewById(R.id.rv_cocktail_list)
        cocktailListRV.layoutManager = LinearLayoutManager(requireContext())
        cocktailListRV.setHasFixedSize(true)
        cocktailListRV.adapter = cocktailsAdapter

        /*
         * Set up an observer on the current forecast data.  If the forecast is not null, display
         * it in the UI.
         */

        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        viewModel.cocktails.observe(viewLifecycleOwner) { cocktails ->
            if (cocktails != null) {
                categoryTV.text = prefs.getString(getString(R.string.pref_category_key), "Unknown")
                cocktailsAdapter.updateCocktails(cocktails)
                cocktailListRV.visibility = View.VISIBLE
                cocktailListRV.scrollToPosition(0)
            }
        }

        /*
         * Set up an observer on the error associated with the current API call.  If the error is
         * not null, display the error that occurred in the UI.
         */
        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                loadingErrorTV.text = getString(R.string.loading_error, error.message)
                loadingErrorTV.visibility = View.VISIBLE
                Log.e(tag, "Error fetching forecast: ${error.message}")
                error.printStackTrace()
            }
        }

        /*
         * Set up an observer on the loading status of the API query.  Display the correct UI
         * elements based on the current loading status.
         */
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                loadingIndicator.visibility = View.VISIBLE
                loadingErrorTV.visibility = View.INVISIBLE
                cocktailListRV.visibility = View.INVISIBLE
            } else {
                loadingIndicator.visibility = View.INVISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()

        /*
         * Trigger loading the forecast data as soon as the fragment resumes.  Doing this in
         * onResume() allows us to potentially refresh the forecast if the user navigates back
         * to the app after being away (e.g. if they updated the settings).
         *
         * Here, the OpenWeather API key is taken from the app's string resources.  See the
         * comment at the top of the main activity class to see how to make this work correctly.
         */
        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val category = prefs.getString(
            getString(R.string.pref_category_key),
            getString(R.string.pref_category_default_value)
        )
        viewModel.loadCocktailList(category ?: getString(R.string.pref_category_default_value))
    }

    private fun onCocktailClick(cocktail: ShortCocktail) {
        val directions = CocktailListFragmentDirections.navigateToCocktailDetails(cocktail)
        findNavController().navigate(directions)
    }
}