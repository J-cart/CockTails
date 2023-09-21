package com.tutorials.drinks.domain.model

import com.google.gson.annotations.SerializedName

data class DrinkDetail(
    /*val idDrink: String,
    val strDrink: String,
    val strDrinkThumb: String,
    val strAlcoholic: String,
    val strCategory: String,
    val strInstructions: String,
    val dateModified: String*/

    @SerializedName("idDrink")
    val drinkId: String,
    @SerializedName("strDrink")
    val drinkName: String,
    @SerializedName("strDrinkThumb")
    val drinkImgUrl: String,
    @SerializedName("strAlcoholic")
    val drinkType: String,
    @SerializedName("strCategory")
    val drinkCategory: String,
    @SerializedName("strInstructions")
    val instructions: String,
    @SerializedName("dateModified")
    val dateModified: String,
    val ingredients:String
)