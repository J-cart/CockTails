package com.tutorials.drinks.data.dto

import com.google.gson.annotations.SerializedName
import com.tutorials.drinks.domain.model.DrinkDetail
import com.tutorials.drinks.domain.util.isNotNullOrEmptyAction

data class DrinkDetailDTO(
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
    @SerializedName("strIngredient1")
    val ingredient1: String?,
    @SerializedName("strIngredient2")
    val ingredient2: String?,
    @SerializedName("strIngredient3")
    val ingredient3: String?,
    @SerializedName("strIngredient4")
    val ingredient4: String?,
    @SerializedName("strIngredient5")
    val ingredient5: String?,
    @SerializedName("strIngredient6")
    val ingredient6: String?,
    @SerializedName("strIngredient7")
    val ingredient7: String?,
    @SerializedName("strIngredient8")
    val ingredient8: String?,
    @SerializedName("strIngredient9")
    val ingredient9: String?,
    @SerializedName("strIngredient10")
    val ingredient10: String?,
    @SerializedName("strIngredient11")
    val ingredient11: String?,
    @SerializedName("strIngredient12")
    val ingredient12: String?,
    @SerializedName("strIngredient13")
    val ingredient13: String?,
    @SerializedName("strIngredient14")
    val ingredient14: String?,
    @SerializedName("strIngredient15")
    val ingredient15: String?,
    @SerializedName("strMeasure1")
    val measure1: String?,
    @SerializedName("strMeasure2")
    val measure2: String?,
    @SerializedName("strMeasure3")
    val measure3: String?,
    @SerializedName("strMeasure4")
    val measure4: String?,
    @SerializedName("strMeasure5")
    val measure5: String?,
    @SerializedName("strMeasure6")
    val measure6: String?,
    @SerializedName("strMeasure7")
    val measure7: String?,
    @SerializedName("strMeasure8")
    val measure8: String?,
    @SerializedName("strMeasure9")
    val measure9: String?,
    @SerializedName("strMeasure10")
    val measure10: String?,
    @SerializedName("strMeasure11")
    val measure11: String?,
    @SerializedName("strMeasure12")
    val measure12: String?,
    @SerializedName("strMeasure13")
    val measure13: String?,
    @SerializedName("strMeasure14")
    val measure14: String?,
    @SerializedName("strMeasure15")
    val measure15: String?
){
    fun toDrinkDetail(): DrinkDetail {
        val stringBuilder = StringBuilder()

        ingredient1.isNotNullOrEmptyAction {
            stringBuilder.append(" 1. $it: $measure1")
        }
        ingredient2.isNotNullOrEmptyAction {
            stringBuilder.append("\n 2. $it: $measure2")
        }
        ingredient3.isNotNullOrEmptyAction {
            stringBuilder.append("\n 3. $it: $measure3")
        }
        ingredient4.isNotNullOrEmptyAction {
            stringBuilder.append("\n 4. $it: $measure4")
        }
        ingredient5.isNotNullOrEmptyAction {
            stringBuilder.append("\n 5. $it: $measure5")
        }
        ingredient6.isNotNullOrEmptyAction {
            stringBuilder.append("\n 6. $it: $measure6")
        }
        ingredient7.isNotNullOrEmptyAction {
            stringBuilder.append("\n 7. $it: $measure7")
        }
        ingredient8.isNotNullOrEmptyAction {
            stringBuilder.append("\n 8. $it: $measure8")
        }
        ingredient9.isNotNullOrEmptyAction {
            stringBuilder.append("\n 9. $it: $measure9")
        }
        ingredient10.isNotNullOrEmptyAction {
            stringBuilder.append("\n 10. $it: $measure10")
        }
        ingredient11.isNotNullOrEmptyAction {
            stringBuilder.append("\n 11. $it: $measure11")
        }
        ingredient12.isNotNullOrEmptyAction {
            stringBuilder.append("\n 12. $it: $measure12")
        }
        ingredient13.isNotNullOrEmptyAction {
            stringBuilder.append("\n 13. $it: $measure13")
        }
        ingredient14.isNotNullOrEmptyAction {
            stringBuilder.append("\n 14. $it: $measure14")
        }
        ingredient15.isNotNullOrEmptyAction {
            stringBuilder.append("\n 15. $it: $measure15")
        }
        return DrinkDetail(
            drinkId=drinkId,
            drinkName = drinkName,
            drinkImgUrl= drinkImgUrl,
            drinkType= drinkType,
            drinkCategory = drinkCategory,
            instructions = instructions,
            dateModified = dateModified,
            ingredients = stringBuilder.toString()
        )
    }
}