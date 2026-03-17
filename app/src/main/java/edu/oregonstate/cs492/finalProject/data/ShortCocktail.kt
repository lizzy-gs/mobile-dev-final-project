package edu.oregonstate.cs492.finalProject.data

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson
import java.io.Serializable

data class ShortCocktail(
    val id: String,
    val name: String,
    val imageUrl: String?
) : Serializable

@JsonClass(generateAdapter = true)
data class ShortCocktailJson(
    val idDrink: String,
    val strDrink: String,
    val strDrinkThumb: String?
)

class ShortCocktailJsonAdapter {
    @FromJson
    fun cocktailFromJson(cocktailJson: ShortCocktailJson): ShortCocktail {
        return ShortCocktail(
            id = cocktailJson.idDrink,
            name = cocktailJson.strDrink,
            imageUrl = cocktailJson.strDrinkThumb
        )
    }

    @ToJson
    fun cocktailToJson(cocktail: ShortCocktail): String {
        throw UnsupportedOperationException("Cannot convert ShortCocktail to JSON")
    }
}