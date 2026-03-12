package edu.oregonstate.cs492.assignment4.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ForecastCityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city: ForecastCity)

    @Delete
    suspend fun delete(city: ForecastCity)

    @Query("SELECT * FROM ForecastCity ORDER BY timeLastViewed DESC")
    fun getAllForecastCities(): Flow<List<ForecastCity>>
}