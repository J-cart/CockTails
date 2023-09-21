package com.tutorials.drinks.domain.model

import com.tutorials.drinks.data.dto.DrinkDetailDTO

data class DrinkDetailResponse(
    val drinks: List<DrinkDetailDTO>
)