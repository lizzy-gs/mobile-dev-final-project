package edu.oregonstate.cs492.finalProject.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.oregonstate.cs492.finalProject.data.CocktailRepository
import edu.oregonstate.cs492.finalProject.data.CocktailService
import edu.oregonstate.cs492.finalProject.data.DetailedCocktailList
import kotlinx.coroutines.launch

class RandomCocktailViewModel : ViewModel() {
    private val repository = CocktailRepository(CocktailService.create())

    private val _randomCocktail = MutableLiveData<DetailedCocktailList?>(null)
    val randomCocktail: LiveData<DetailedCocktailList?> = _randomCocktail

    private val _error = MutableLiveData<Throwable?>(null)
    val error: LiveData<Throwable?> = _error

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    fun loadRandomCocktail() {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.getRandomCocktail()
            _loading.value = false
            _error.value = result.exceptionOrNull()
            _randomCocktail.value = result.getOrNull()
        }
    }
}
