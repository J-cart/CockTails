package com.tutorials.drinks.presentation.arch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutorials.drinks.data.DrinksRepositoryImpl
import com.tutorials.drinks.domain.model.AllDrinksResponse
import com.tutorials.drinks.domain.model.Drink
import com.tutorials.drinks.domain.model.DrinkCategoryResponse
import com.tutorials.drinks.domain.model.DrinkDetailResponse
import com.tutorials.drinks.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrinksViewModel @Inject constructor(private val repository: DrinksRepositoryImpl) :
    ViewModel() {

    var allDrinksState = MutableStateFlow<Resource<AllDrinksResponse>>(Resource.Loading())
        private set

    var drinksByCategoryState = MutableStateFlow<Resource<AllDrinksResponse>>(Resource.Loading())
        private set


    var searchDrinksState =
        MutableStateFlow<Resource<AllDrinksResponse>>(Resource.Failure("Search Cocktail"))
        private set

    var drinksByIdState =
        MutableStateFlow<Resource<DrinkDetailResponse>>(Resource.Failure("No items available"))
        private set

    var drinkCategoryState =
        MutableStateFlow<Resource<DrinkCategoryResponse>>(Resource.Failure("No items available"))
        private set

    var favoriteState = MutableStateFlow(-1)
        private set

    var allFavoriteDrinksState = MutableStateFlow<Resource<List<Drink>>>(Resource.Loading())
        private set

    var homeFirstLaunch = MutableStateFlow(true)
        private set

    var categoriesFirstLaunch = MutableStateFlow(true)
        private set


    fun getAllDrinks(alcoholicOrNot: String) {
        allDrinksState.value = Resource.Loading()
        viewModelScope.launch {
            when (val drinksResponse = repository.getAllDrinks(alcoholicOrNot)) {
                is Resource.Successful -> {
                    allDrinksState.value = Resource.Successful(drinksResponse.data)
                }
                is Resource.Failure -> {
                    allDrinksState.value = Resource.Failure(drinksResponse.msg)
                }
                else -> Unit
            }
        }
    }


    fun searchDrinks(name: String) {
        searchDrinksState.value = Resource.Loading()
        viewModelScope.launch {
            when (val drinksResponse = repository.searchDrinks(name)) {
                is Resource.Successful -> {
                    searchDrinksState.value = Resource.Successful(drinksResponse.data)
                }
                is Resource.Failure -> {
                    searchDrinksState.value = Resource.Failure("Search Cocktail ")
//                    searchDrinksState.value = Resource.Failure(drinksResponse.msg)
                }
                else -> Unit
            }
        }
    }

    fun getDrinksById(id: String) {
        drinksByIdState.value = Resource.Loading()
        viewModelScope.launch {
            when (val drinksResponse = repository.getDrinksById(id)) {
                is Resource.Successful -> {
                    drinksByIdState.value = Resource.Successful(drinksResponse.data)
                }
                is Resource.Failure -> {
                    drinksByIdState.value = Resource.Failure(drinksResponse.msg)
                }
                else -> Unit
            }
        }
    }

    fun getAllCategory() {
        drinkCategoryState.value = Resource.Loading()
        viewModelScope.launch {
            when (val drinksResponse = repository.getDrinksCategory()) {
                is Resource.Successful -> {
                    drinkCategoryState.value = Resource.Successful(drinksResponse.data)
                }
                is Resource.Failure -> {
                    drinkCategoryState.value = Resource.Failure(drinksResponse.msg)
                }
                else -> Unit
            }
        }
    }

    fun getDrinksByCategory(categoryName: String) {
        drinksByCategoryState.value = Resource.Loading()
        viewModelScope.launch {
            when (val drinksResponse = repository.getDrinksByCategory(categoryName)) {
                is Resource.Successful -> {
                    drinksByCategoryState.value = Resource.Successful(drinksResponse.data)
                }
                is Resource.Failure -> {
                    drinksByCategoryState.value = Resource.Failure(drinksResponse.msg)
                }
                else -> Unit
            }
        }
    }

    fun toggleSearchDrinksState(state: Resource<AllDrinksResponse>) {
        searchDrinksState.value = state
    }


    fun toggleHomeFirstLaunch(value: Boolean) {
        homeFirstLaunch.value = value
    }

    fun toggleCategoriesFirstLaunch(value: Boolean) {
        categoriesFirstLaunch.value = value
    }

    //region DB operations

    fun getAllFavoriteDrinks() {
        viewModelScope.launch {
            repository.getAllDrinks().collect {
                if (it.isEmpty()) {
                    allFavoriteDrinksState.value = Resource.Failure("Your favorite cocktails will appear here ")
                    return@collect
                }
                allFavoriteDrinksState.value = Resource.Successful(it)
            }
        }
    }

    fun addToFavorites(drink: Drink) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertDrink(drink)
            checkIfFavorite(drink.drinkId)
        }
    }

    fun removeFromFavorites(drink: Drink) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteDrink(drink.drinkId)
            checkIfFavorite(drink.drinkId)
        }
    }

    fun removeAllFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllDrinks()
        }
    }

    fun checkIfFavorite(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteState.value = repository.checkIfDrinkExists(id)
        }
    }

    fun resetFavoriteState() {
        favoriteState.value = -1
    }


    //endregion


}