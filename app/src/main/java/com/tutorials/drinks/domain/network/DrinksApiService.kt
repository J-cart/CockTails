package com.tutorials.drinks.domain.network

import com.tutorials.drinks.domain.model.AllDrinksResponse
import com.tutorials.drinks.domain.model.DrinkCategoryResponse
import com.tutorials.drinks.domain.model.DrinkDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DrinksApiService {
    @GET("filter.php")
    suspend fun getAllDrinks(@Query("a") alcoholicOrNot: String): Response<AllDrinksResponse>

    @GET("search.php")
    suspend fun searchAllDrinks(@Query("s") name: String): Response<AllDrinksResponse>

    @GET("lookup.php")
    suspend fun getDrinkById(@Query("i") id: String): Response<DrinkDetailResponse>

    @GET("list.php")
    suspend fun getAllCategories(@Query("c") list: String = "list"): Response<DrinkCategoryResponse>

    @GET("filter.php")
    suspend fun getDrinksByCategory(@Query("c") list: String): Response<AllDrinksResponse>


}