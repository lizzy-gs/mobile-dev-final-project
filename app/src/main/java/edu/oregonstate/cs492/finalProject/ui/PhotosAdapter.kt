package edu.oregonstate.cs492.finalProject.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.oregonstate.cs492.finalProject.R
import edu.oregonstate.cs492.finalProject.data.Photo
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PhotosAdapter(
    private val onPhotoClick: (Photo) -> Unit,
    private val onDeleteClick: (Photo) -> Unit
) : RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {

    private var photos: List<Photo> = listOf()

    fun updatePhotos(newPhotos: List<Photo>) {
        photos = newPhotos
        notifyDataSetChanged()
    }

    override fun getItemCount() = photos.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.photo_list_item, parent, false)
        return ViewHolder(view, onPhotoClick, onDeleteClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(photos[position])
    }

    class ViewHolder(
        itemView: View,
        private val onPhotoClick: (Photo) -> Unit,
        private val onDeleteClick: (Photo) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val photoIV: ImageView = itemView.findViewById(R.id.iv_photo)
        private val captionTV: TextView = itemView.findViewById(R.id.tv_caption)
        private val timestampTV: TextView = itemView.findViewById(R.id.tv_timestamp)
        private val deleteBtn: ImageButton = itemView.findViewById(R.id.btn_delete)

        private val dateFormat = SimpleDateFormat("MMM dd, yyyy h:mm a", Locale.getDefault())

        fun bind(photo: Photo) {
            captionTV.text = photo.caption
                ?: itemView.context.getString(R.string.photo_no_caption)

            timestampTV.text = dateFormat.format(Date(photo.timestamp))

            Glide.with(itemView.context)
                .load(File(photo.imagePath))
                .centerCrop()
                .into(photoIV)

            itemView.setOnClickListener {
                onPhotoClick(photo)
            }

            deleteBtn.setOnClickListener {
                onDeleteClick(photo)
            }
        }
    }
}

