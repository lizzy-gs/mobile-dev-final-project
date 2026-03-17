package edu.oregonstate.cs492.finalProject.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.google.android.material.progressindicator.CircularProgressIndicator
import edu.oregonstate.cs492.finalProject.R
import edu.oregonstate.cs492.finalProject.data.DetailedCocktail

class CocktailDetailsFragment : Fragment(R.layout.fragment_cocktail_details) {
    private val viewModel: CocktailDetailsViewModel by viewModels()
    private val args: CocktailDetailsFragmentArgs by navArgs()
    private var currentCocktail: DetailedCocktail? = null
    private lateinit var cocktailDetailsView: View
    private lateinit var loadingErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator
    private lateinit var thumbnailIV: ImageView
    private lateinit var nameTV: TextView
    private lateinit var categoryTV: TextView
    private lateinit var alcoholicTV: TextView
    private lateinit var glassTV: TextView
    private lateinit var instructionsHeaderTV: TextView
    private lateinit var instructionsTV: TextView
    private lateinit var ingredientsHeaderTV: TextView
    private lateinit var ingredientsLL: LinearLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cocktailDetailsView = view.findViewById(R.id.cocktail_details)
        loadingErrorTV = view.findViewById(R.id.tv_loading_error_details)
        loadingIndicator = view.findViewById(R.id.loading_indicator_details)

        thumbnailIV = view.findViewById(R.id.iv_cocktail_image)
        nameTV = view.findViewById(R.id.tv_cocktail_name)
        categoryTV = view.findViewById(R.id.tv_cocktail_category)
        alcoholicTV = view.findViewById(R.id.tv_cocktail_alcoholic)
        glassTV = view.findViewById(R.id.tv_cocktail_glass)
        instructionsHeaderTV = view.findViewById(R.id.tv_instructions_header)
        instructionsTV = view.findViewById(R.id.tv_instructions)
        ingredientsHeaderTV = view.findViewById(R.id.tv_ingredients_header)
        ingredientsLL = view.findViewById(R.id.layout_ingredients)

        /*
         * Set up an observer on the fetched weather data.  When new data arrives, bind it into
         * the UI.
         */
        viewModel.cocktailDetails.observe(viewLifecycleOwner) { details ->
            if (details != null) {
                val drink = details.drinks.firstOrNull()
                if (drink != null) {
                    bind(drink)
                    currentCocktail = drink
                    cocktailDetailsView.visibility = View.VISIBLE
                    instructionsHeaderTV.text = getString(R.string.instructions_header)
                    ingredientsHeaderTV.text = getString(R.string.ingredients_header)
                } else {
                    loadingErrorTV.text = getString(R.string.loading_error, "Error fetching cocktail")
                    loadingErrorTV.visibility = View.VISIBLE
                }
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
                Log.e(tag, "Error fetching cocktail: ${error.message}")
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
                cocktailDetailsView.visibility = View.INVISIBLE
            } else {
                loadingIndicator.visibility = View.INVISIBLE
            }
        }

        /*
         * Set up a MenuProvider to provide and handle app bar actions for this fragment.
         */
//        val menuHost = requireActivity() as MenuHost
//        menuHost.addMenuProvider(
//            object : MenuProvider {
//                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//                    menuInflater.inflate(R.menu.current_weather_menu, menu)
//                }
//
//                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
//                    return when (menuItem.itemId) {
//                        R.id.action_five_day_forecast -> {
//                            val directions = CocktailDetails.navigateToFiveDayForecast()
//                            findNavController().navigate(directions)
//                            true
//                        }
//                        R.id.action_share -> {
//                            if (currentWeather != null) {
//                                share(currentWeather!!)
//                            }
//                            true
//                        }
//                        else -> false
//                    }
//                }
//
//            },
//            viewLifecycleOwner,
//            Lifecycle.State.STARTED
//        )
    }

    override fun onResume() {
        super.onResume()

        viewModel.loadCocktailDetails(args.cocktail.id)
    }

    private fun bind(cocktail: DetailedCocktail) {
        Glide.with(this)
            .load(cocktail.imageUrl)
            .into(thumbnailIV)

        nameTV.text = cocktail.name

        categoryTV.text = cocktail.category

        alcoholicTV.text = cocktail.alcoholic

        glassTV.text = cocktail.glass

        instructionsTV.text = cocktail.instructions

        ingredientsLL.removeAllViews()
        cocktail.ingredients.forEach { ingredient ->
            val textView = TextView(requireContext()).apply {
                setText(getString(R.string.ingredient, ingredient.name, ingredient.measure))
                textSize = 18f
                // Set layout parameters
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }
            ingredientsLL.addView(textView)
        }
    }

    /*
     * Share the current weather using the Android Sharesheet.
     */
    private fun share(cocktail: DetailedCocktail) {
        val shareText = "Sharing"
        val intent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(intent, null))
    }
}