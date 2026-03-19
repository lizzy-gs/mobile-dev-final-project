package edu.oregonstate.cs492.finalProject.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imagePath: String,
    val caption: String? = null,
    val description: String? = null,
    val instructions: String? = null,
    val ingredients: String? = null,
    val timestamp: Long = System.currentTimeMillis()
) : Serializable