package com.tutorials.drinks.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tutorials.drinks.domain.model.Drink

@Database(entities = [Drink::class], version = 1)
abstract class DrinksDatabase:RoomDatabase() {
    abstract fun drinksDao():DrinksDao
}