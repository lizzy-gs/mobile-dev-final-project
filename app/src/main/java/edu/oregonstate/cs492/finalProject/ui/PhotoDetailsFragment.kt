package edu.oregonstate.cs492.finalProject.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import edu.oregonstate.cs492.finalProject.R
import edu.oregonstate.cs492.finalProject.data.Photo
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PhotoDetailsFragment : Fragment(R.layout.fragment_photo_details) {

    private val args: PhotoDetailsFragmentArgs by navArgs()

    private lateinit var photoIV: ImageView
    private lateinit var captionTV: TextView
    private lateinit var descriptionTV: TextView
    private lateinit var timestampTV: TextView
    private lateinit var ingredientsHeaderTV: TextView
    private lateinit var ingredientsLL: LinearLayout
    private lateinit var instructionsHeaderTV: TextView
    private lateinit var instructionsTV: TextView

    private val dateFormat = SimpleDateFormat("MMM dd, yyyy h:mm a", Locale.getDefault())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        photoIV = view.findViewById(R.id.iv_photo_image)
        captionTV = view.findViewById(R.id.tv_photo_caption)
        descriptionTV = view.findViewById(R.id.tv_photo_description)
        timestampTV = view.findViewById(R.id.tv_photo_timestamp)
        ingredientsHeaderTV = view.findViewById(R.id.tv_ingredients_header)
        ingredientsLL = view.findViewById(R.id.layout_ingredients)
        instructionsHeaderTV = view.findViewById(R.id.tv_instructions_header)
        instructionsTV = view.findViewById(R.id.tv_instructions)

        bind(args.photo)
    }

    private fun bind(photo: Photo) {
        Glide.with(this)
            .load(File(photo.imagePath))
            .centerCrop()
            .into(photoIV)

        captionTV.text = photo.caption ?: getString(R.string.photo_no_caption)
        descriptionTV.text = photo.description ?: ""
        descriptionTV.visibility = if (photo.description.isNullOrBlank()) View.GONE else View.VISIBLE

        timestampTV.text = dateFormat.format(Date(photo.timestamp))

        if (!photo.ingredients.isNullOrBlank()) {
            ingredientsHeaderTV.text = getString(R.string.ingredients_header)
            ingredientsLL.removeAllViews()
            photo.ingredients.split("\n").forEach { line ->
                if (line.isNotBlank()) {
                    val textView = TextView(requireContext()).apply {
                        text = line.trim()
                        textSize = 18f
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                    }
                    ingredientsLL.addView(textView)
                }
            }
        } else {
            ingredientsHeaderTV.visibility = View.GONE
            ingredientsLL.visibility = View.GONE
        }

        if (!photo.instructions.isNullOrBlank()) {
            instructionsHeaderTV.text = getString(R.string.instructions_header)
            instructionsTV.text = photo.instructions
        } else {
            instructionsHeaderTV.visibility = View.GONE
            instructionsTV.visibility = View.GONE
        }
    }
}
