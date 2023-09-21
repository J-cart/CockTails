package com.tutorials.drinks.domain

import com.tutorials.drinks.domain.model.AllDrinksResponse
import com.tutorials.drinks.domain.model.Drink
import com.tutorials.drinks.domain.model.DrinkCategoryResponse
import com.tutorials.drinks.domain.model.DrinkDetailResponse
import com.tutorials.drinks.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface DrinksRepository {

    suspend fun getAllDrinks(alcoholicOrNot:String): Resource<AllDrinksResponse>

    suspend fun searchDrinks(name:String): Resource<AllDrinksResponse>

    suspend fun getDrinksById(id:String): Resource<DrinkDetailResponse>

    suspend fun getDrinksCategory(): Resource<DrinkCategoryResponse>

    suspend fun getDrinksByCategory(category:String): Resource<AllDrinksResponse>

    suspend fun insertDrink(drink: Drink)

    fun getAllDrinks():Flow<List<Drink>>

    suspend fun deleteAllDrinks()

    suspend fun deleteDrink(id:String)

    suspend fun checkIfDrinkExists(id: String):Int
}