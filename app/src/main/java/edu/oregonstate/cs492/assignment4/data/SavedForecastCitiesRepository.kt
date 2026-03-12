package edu.oregonstate.cs492.assignment4.data

class SavedForecastCitiesRepository (
    private val dao: ForecastCityDao
) {
    suspend fun insertSavedCity(city: ForecastCity) =
        dao.insert(city)

    suspend fun deleteSavedCity(city: ForecastCity) =
        dao.delete(city)

    fun getAllSavedCities() = dao.getAllForecastCities()
}