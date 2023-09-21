package com.tutorials.drinks.domain.model

import com.google.gson.annotations.SerializedName

data class DrinkCategoryResponse(
    @SerializedName("drinks")
    val categories: List<DrinkCategory>
)