package com.tutorials.drinks.data

import com.tutorials.drinks.domain.DrinksRepository
import com.tutorials.drinks.db.DrinksDatabase
import com.tutorials.drinks.domain.model.Drink
import com.tutorials.drinks.domain.network.DrinksApiService
import com.tutorials.drinks.domain.util.Resource
import javax.inject.Inject

class DrinksRepositoryImpl @Inject constructor(private val api: DrinksApiService, val db:DrinksDatabase) :
    DrinksRepository {
    override suspend fun getAllDrinks(alcoholicOrNot: String) =
        try {
            val drinksResponse = api.getAllDrinks(alcoholicOrNot)
            val drinks = drinksResponse.body()

            drinks?.let{
                Resource.Successful(it)
            }?: Resource.Failure(drinksResponse.errorBody().toString())
        } catch (e: Exception) {
            Resource.Failure("Oops ... ${e.message}")
        }


    override suspend fun searchDrinks(name: String) =
        try {
            val drinksResponse = api.searchAllDrinks(name)
            val drinks = drinksResponse.body()
            drinks?.let{
                Resource.Successful(it)
            }?: Resource.Failure(drinksResponse.errorBody().toString())
        } catch (e: Exception) {
            Resource.Failure("Oops ... ${e.message}")
        }
    override suspend fun getDrinksById(id: String) =
        try {
            val drinksResponse = api.getDrinkById(id)
            val drinks = drinksResponse.body()
            if (drinksResponse.isSuccessful ) {
                Resource.Successful(drinks)
            } else {
                Resource.Failure(drinksResponse.errorBody().toString())
            }
        } catch (e: Exception) {
            Resource.Failure("Oops ... ${e.message}")
        }

    override suspend fun getDrinksCategory() =
        try {
            val drinksResponse = api.getAllCategories()
            val drinks = drinksResponse.body()
            drinks?.let{
                Resource.Successful(it)
            }?: Resource.Failure(drinksResponse.errorBody().toString())
        } catch (e: Exception) {
            Resource.Failure("Oops ... ${e.message}")
        }

    override suspend fun getDrinksByCategory(category:String) =
        try {
            val drinksResponse = api.getDrinksByCategory(category)
            val drinks = drinksResponse.body()
            drinks?.let{
                Resource.Successful(it)
            }?: Resource.Failure(drinksResponse.errorBody().toString())
        } catch (e: Exception) {
            Resource.Failure("Oops ... ${e.message}")
        }

    override suspend fun insertDrink(drink: Drink){
        db.drinksDao().insertDrink(drink)
    }

    override fun getAllDrinks()= db.drinksDao().getAllDrinks()

    override suspend fun deleteAllDrinks(){
        db.drinksDao().deleteAll()
    }
    override suspend fun deleteDrink(id:String){
        db.drinksDao().deleteDrink(id)
    }
    override suspend fun checkIfDrinkExists(id: String)= db.drinksDao().checkIfDrinkExists(id)
}