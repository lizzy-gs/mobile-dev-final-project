package edu.oregonstate.cs492.roomgithubsearch.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import edu.oregonstate.cs492.assignment4.data.AppDatabase
import edu.oregonstate.cs492.assignment4.data.ForecastCity
import edu.oregonstate.cs492.assignment4.data.SavedForecastCitiesRepository
import kotlinx.coroutines.launch

class SavedForecastCitiesViewModel(application: Application) :
    AndroidViewModel(application)
{
    private val repository = SavedForecastCitiesRepository(
        AppDatabase.getInstance(application).forecastCityDao()
    )

    val savedCities = repository.getAllSavedCities().asLiveData()

    fun addSavedCity(city: ForecastCity) {
        viewModelScope.launch {
            repository.insertSavedCity(city)
        }
    }

    fun removeSavedCity(city: ForecastCity) {
        viewModelScope.launch {
            repository.deleteSavedCity(city)
        }
    }
}