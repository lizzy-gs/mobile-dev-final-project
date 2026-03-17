package edu.oregonstate.cs492.finalProject.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.oregonstate.cs492.finalProject.data.CocktailRepository
import edu.oregonstate.cs492.finalProject.data.CocktailService
import edu.oregonstate.cs492.finalProject.data.DetailedCocktailList
import kotlinx.coroutines.launch

/**
 * This is a ViewModel class that holds current weather data for the UI.
 */
class CocktailDetailsViewModel: ViewModel() {
    private val repository = CocktailRepository(CocktailService.create())

    /*
     * The most recent response from the OpenWeather current weather API are stored in this
     * private property.  These results are exposed to the outside world in immutable form via the
     * public `forecast` property below.
     */
    private val _cocktailDetails = MutableLiveData<DetailedCocktailList?>(null)

    /**
     * This value provides the most recent response from the OpenWeather current weather API.
     * It is null if there are no current results (e.g. in the case of an error).
     */
    val cocktailDetails: LiveData<DetailedCocktailList?> = _cocktailDetails

    /*
     * The current error for the most recent API query is stored in this private property.  This
     * error is exposed to the outside world in immutable form via the public `error` property
     * below.
     */
    private val _error = MutableLiveData<Throwable?>(null)

    /**
     * This property provides the error associated with the most recent API query, if there is
     * one.  If there was no error associated with the most recent API query, it will be null.
     */
    val error: LiveData<Throwable?> = _error

    /*
     * The current loading state is stored in this private property.  This loading state is exposed
     * to the outside world in immutable form via the public `loading` property below.
     */
    private val _loading = MutableLiveData<Boolean>(false)

    /**
     * This property indicates the current loading state of an API query.  It is `true` if an
     * API query is currently being executed or `false` otherwise.
     */
    val loading: LiveData<Boolean> = _loading

    fun loadCocktailDetails(id: String) {
        /*
         * Launch a new coroutine in which to execute the API call.  The coroutine is tied to the
         * lifecycle of this ViewModel by using `viewModelScope`.
         */
        viewModelScope.launch {
            _loading.value = true
            val result = repository.getCocktailDetails(id)
            _loading.value = false
            _error.value = result.exceptionOrNull()
            _cocktailDetails.value = result.getOrNull()
        }
    }
}