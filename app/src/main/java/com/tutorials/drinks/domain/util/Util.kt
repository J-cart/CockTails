package com.tutorials.drinks.domain.util

const val BASE_URL = "https://www.thecocktaildb.com/api/json/v1/1/"
const val DATABASE_NAME = "DRINKS_DATABASE"
const val TABLE_NAME = "drinks_table"
const val ALCOHOLIC = "Alcoholic"
const val NON_ALCOHOLIC = "Non_Alcoholic"


fun String?.isNotNullOrEmptyAction(action: (String) -> Unit) {
    this?.let {
        if (it.isNotEmpty()) {
            action(it)
        }
    }
}