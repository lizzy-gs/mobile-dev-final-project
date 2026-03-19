package edu.oregonstate.cs492.finalProject.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import edu.oregonstate.cs492.finalProject.R
import edu.oregonstate.cs492.finalProject.data.Photo
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PhotoGalleryFragment : Fragment(R.layout.fragment_photo_gallery) {

    private val viewModel: PhotosViewModel by viewModels()
    private val photosAdapter = PhotosAdapter(::onDeleteClick)

    private lateinit var photosRV: RecyclerView
    private lateinit var emptyTV: TextView
    private lateinit var fabCamera: FloatingActionButton

    private var currentPhotoPath: String? = null

    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && currentPhotoPath != null) {
            val photo = Photo(imagePath = currentPhotoPath!!)
            viewModel.addPhoto(photo)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            launchCamera()
        } else {
            Toast.makeText(
                requireContext(),
                "Camera permission is required to take photos",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emptyTV = view.findViewById(R.id.tv_empty)
        fabCamera = view.findViewById(R.id.fab_camera)

        photosRV = view.findViewById(R.id.rv_photos)
        photosRV.layoutManager = GridLayoutManager(requireContext(), 2)
        photosRV.adapter = photosAdapter

        fabCamera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                launchCamera()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }

        viewModel.photos.observe(viewLifecycleOwner) { photos ->
            photosAdapter.updatePhotos(photos)
            emptyTV.visibility = if (photos.isEmpty()) View.VISIBLE else View.GONE
            photosRV.visibility = if (photos.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    private fun launchCamera() {
        val photoFile = createImageFile()
        currentPhotoPath = photoFile.absolutePath

        val photoUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            photoFile
        )

        takePictureLauncher.launch(photoUri)
    }

    private fun createImageFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("PHOTO_${timestamp}_", ".jpg", storageDir)
    }

    private fun onDeleteClick(photo: Photo) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.photo_delete_confirm))
            .setPositiveButton("Delete") { _, _ ->
                viewModel.removePhoto(photo)
                val file = File(photo.imagePath)
                if (file.exists()) {
                    file.delete()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
