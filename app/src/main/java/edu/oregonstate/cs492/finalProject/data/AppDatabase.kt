package edu.oregonstate.cs492.finalProject.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Photo::class], version = 2)
abstract class AppDatabase: RoomDatabase() {

    abstract fun photoDao(): PhotoDao
    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "photo-db"
            )
            .fallbackToDestructiveMigration()
            .build()

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }
    }
}