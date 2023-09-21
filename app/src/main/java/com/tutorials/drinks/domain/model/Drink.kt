package com.tutorials.drinks.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.tutorials.drinks.domain.util.TABLE_NAME
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = TABLE_NAME)
data class Drink(
    @PrimaryKey(autoGenerate = true)
    val tableId:Int =0,
    @SerializedName("idDrink")
    val drinkId: String,
    @SerializedName("strDrink")
    val drinkName: String,
    @SerializedName("strDrinkThumb")
    val drinkImgUrl: String,
):Parcelable