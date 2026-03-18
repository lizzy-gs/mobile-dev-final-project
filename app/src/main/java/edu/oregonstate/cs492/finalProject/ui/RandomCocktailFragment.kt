package edu.oregonstate.cs492.finalProject.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import edu.oregonstate.cs492.finalProject.R
import edu.oregonstate.cs492.finalProject.data.DetailedCocktail

class RandomCocktailFragment : Fragment(R.layout.fragment_random_cocktail) {
    private val viewModel: RandomCocktailViewModel by viewModels()

    private lateinit var contentView: View
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
    private lateinit var shuffleBtn: MaterialButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contentView = view.findViewById(R.id.random_cocktail_content)
        loadingErrorTV = view.findViewById(R.id.tv_random_loading_error)
        loadingIndicator = view.findViewById(R.id.random_loading_indicator)

        thumbnailIV = view.findViewById(R.id.iv_random_cocktail_image)
        nameTV = view.findViewById(R.id.tv_random_cocktail_name)
        categoryTV = view.findViewById(R.id.tv_random_cocktail_category)
        alcoholicTV = view.findViewById(R.id.tv_random_cocktail_alcoholic)
        glassTV = view.findViewById(R.id.tv_random_cocktail_glass)
        instructionsHeaderTV = view.findViewById(R.id.tv_random_instructions_header)
        instructionsTV = view.findViewById(R.id.tv_random_instructions)
        ingredientsHeaderTV = view.findViewById(R.id.tv_random_ingredients_header)
        ingredientsLL = view.findViewById(R.id.layout_random_ingredients)
        shuffleBtn = view.findViewById(R.id.btn_shuffle)

        shuffleBtn.setOnClickListener {
            viewModel.loadRandomCocktail()
        }

        viewModel.randomCocktail.observe(viewLifecycleOwner) { details ->
            if (details != null) {
                val drink = details.drinks.firstOrNull()
                if (drink != null) {
                    bind(drink)
                    contentView.visibility = View.VISIBLE
                    instructionsHeaderTV.text = getString(R.string.instructions_header)
                    ingredientsHeaderTV.text = getString(R.string.ingredients_header)
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                loadingErrorTV.text = getString(R.string.loading_error, error.message)
                loadingErrorTV.visibility = View.VISIBLE
                Log.e(tag, "Error fetching random cocktail: ${error.message}")
                error.printStackTrace()
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                loadingIndicator.visibility = View.VISIBLE
                loadingErrorTV.visibility = View.INVISIBLE
                contentView.visibility = View.INVISIBLE
            } else {
                loadingIndicator.visibility = View.INVISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()

        /*
         * Only load a random cocktail if we don't already have one displayed.
         * This prevents losing the current cocktail when navigating back.
         */
        if (viewModel.randomCocktail.value == null) {
            viewModel.loadRandomCocktail()
        }
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
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }
            ingredientsLL.addView(textView)
        }
    }
}
