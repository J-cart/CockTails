package com.tutorials.drinks.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tutorials.drinks.domain.model.Drink
import kotlinx.coroutines.flow.Flow

@Dao
interface DrinksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrink(drink: Drink)

    @Query("SELECT * FROM drinks_table")
    fun getAllDrinks(): Flow<List<Drink>>

    @Query("DELETE FROM drinks_table")
    suspend fun deleteAll()

    @Query("DELETE FROM drinks_table WHERE drinkId=:id ")
    suspend fun deleteDrink(id: String)

    @Query("SELECT COUNT() FROM drinks_table WHERE drinkId=:id")
    suspend fun checkIfDrinkExists(id: String): Int


    /**
     * get all
     * insert
     * delete
     * check if exists
     * ----------------------
     * delete all
     * */

}
