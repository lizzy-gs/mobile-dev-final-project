package edu.oregonstate.cs492.finalProject.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.oregonstate.cs492.finalProject.R
import edu.oregonstate.cs492.finalProject.data.DetailedCocktail
import edu.oregonstate.cs492.finalProject.data.ShortCocktail
import edu.oregonstate.cs492.finalProject.data.ShortCocktailList

class CocktailsAdapter(
    private val onCocktailClick: (ShortCocktail) -> Unit
) : RecyclerView.Adapter<CocktailsAdapter.ViewHolder>() {
    var cocktails: List<ShortCocktail> = listOf()

    /**
     * This method is used to update the five-day forecast data stored by this adapter class.
     */
    fun updateCocktails(cocktailsList: ShortCocktailList?) {
        notifyItemRangeRemoved(0, cocktails.size)
        cocktails = cocktailsList?.drinks ?: listOf()
        notifyItemRangeInserted(0, cocktails.size)
    }

    override fun getItemCount() = cocktails.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cocktail_list_item, parent, false)
        return ViewHolder(view, onCocktailClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cocktails[position])
    }

    class ViewHolder(
        itemView: View,
        val onCocktailClick: (ShortCocktail) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val nameTV: TextView = itemView.findViewById(R.id.tv_name)
        private val thumbnailIV: ImageView = itemView.findViewById(R.id.iv_thumbnail)
        private lateinit var currentCocktail: ShortCocktail

        init {
            itemView.setOnClickListener { currentCocktail.let(onCocktailClick) }
        }

        fun bind(cocktail: ShortCocktail) {
            currentCocktail = cocktail

            val ctx = itemView.context

            nameTV.text = cocktail.name

            /*
             * Load forecast icon into ImageView using Glide: https://bumptech.github.io/glide/
             */
            Glide.with(ctx)
                .load(cocktail.imageUrl)
                .into(thumbnailIV)
        }
    }
}