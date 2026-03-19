package edu.oregonstate.cs492.finalProject.data

class PhotoRepository(
    private val dao: PhotoDao
) {
    suspend fun insertPhoto(photo: Photo) =
        dao.insert(photo)

    suspend fun deletePhoto(photo: Photo) =
        dao.delete(photo)

    fun getAllPhotos() = dao.getAllPhotos()
}