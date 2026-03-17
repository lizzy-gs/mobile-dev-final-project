package edu.oregonstate.cs492.finalProject.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DetailedCocktailList(
    val drinks: List<DetailedCocktail>
)

@JsonClass(generateAdapter = true)
data class ShortCocktailList(
    val drinks: List<ShortCocktail>
)