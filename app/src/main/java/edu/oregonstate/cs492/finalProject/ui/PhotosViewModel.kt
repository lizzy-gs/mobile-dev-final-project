package edu.oregonstate.cs492.finalProject.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import edu.oregonstate.cs492.finalProject.data.AppDatabase
import edu.oregonstate.cs492.finalProject.data.Photo
import edu.oregonstate.cs492.finalProject.data.PhotoRepository
import kotlinx.coroutines.launch

class PhotosViewModel(application: Application) :
    AndroidViewModel(application)
{
    private val repository = PhotoRepository(
        AppDatabase.getInstance(application).photoDao()
    )

    val photos = repository.getAllPhotos().asLiveData()

    fun addPhoto(photo: Photo) {
        viewModelScope.launch {
            repository.insertPhoto(photo)
        }
    }

    fun removePhoto(photo: Photo) {
        viewModelScope.launch {
            repository.deletePhoto(photo)
        }
    }
}