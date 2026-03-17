package edu.oregonstate.cs492.finalProject.data

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson

data class Ingredient(
    val name: String,
    val measure: String?
)

data class DetailedCocktail(
    val id: String,
    val name: String,
    val category: String?,
    val alcoholic: String?,
    val glass: String?,
    val instructions: String?,
    val imageUrl: String?,
    val ingredients: List<Ingredient>
)

@JsonClass(generateAdapter = true)
data class DetailedCocktailJson(
    val idDrink: String,
    val strDrink: String,
    val strCategory: String?,
    val strAlcoholic: String?,
    val strGlass: String?,
    val strInstructions: String?,
    val strDrinkThumb: String?,

    val strIngredient1: String?,
    val strIngredient2: String?,
    val strIngredient3: String?,
    val strIngredient4: String?,
    val strIngredient5: String?,
    val strIngredient6: String?,
    val strIngredient7: String?,
    val strIngredient8: String?,
    val strIngredient9: String?,
    val strIngredient10: String?,
    val strIngredient11: String?,
    val strIngredient12: String?,
    val strIngredient13: String?,
    val strIngredient14: String?,
    val strIngredient15: String?,

    val strMeasure1: String?,
    val strMeasure2: String?,
    val strMeasure3: String?,
    val strMeasure4: String?,
    val strMeasure5: String?,
    val strMeasure6: String?,
    val strMeasure7: String?,
    val strMeasure8: String?,
    val strMeasure9: String?,
    val strMeasure10: String?,
    val strMeasure11: String?,
    val strMeasure12: String?,
    val strMeasure13: String?,
    val strMeasure14: String?,
    val strMeasure15: String?
)

class DetailedCocktailJsonAdapter {
    @FromJson
    fun cocktailFromJson(cocktailJson: DetailedCocktailJson): DetailedCocktail {
        val ingredientNames = listOf(
            cocktailJson.strIngredient1,
            cocktailJson.strIngredient2,
            cocktailJson.strIngredient3,
            cocktailJson.strIngredient4,
            cocktailJson.strIngredient5,
            cocktailJson.strIngredient6,
            cocktailJson.strIngredient7,
            cocktailJson.strIngredient8,
            cocktailJson.strIngredient9,
            cocktailJson.strIngredient10,
            cocktailJson.strIngredient11,
            cocktailJson.strIngredient12,
            cocktailJson.strIngredient13,
            cocktailJson.strIngredient14,
            cocktailJson.strIngredient15
        )

        val measures = listOf(
            cocktailJson.strMeasure1,
            cocktailJson.strMeasure2,
            cocktailJson.strMeasure3,
            cocktailJson.strMeasure4,
            cocktailJson.strMeasure5,
            cocktailJson.strMeasure6,
            cocktailJson.strMeasure7,
            cocktailJson.strMeasure8,
            cocktailJson.strMeasure9,
            cocktailJson.strMeasure10,
            cocktailJson.strMeasure11,
            cocktailJson.strMeasure12,
            cocktailJson.strMeasure13,
            cocktailJson.strMeasure14,
            cocktailJson.strMeasure15
        )

        val ingredients = mutableListOf<Ingredient>()

        for (i in ingredientNames.indices) {
            val name = ingredientNames[i]
            if (!name.isNullOrBlank()) {
                ingredients.add(
                    Ingredient(
                        name = name,
                        measure = measures[i]
                    )
                )
            }
        }

        return DetailedCocktail(
            id = cocktailJson.idDrink,
            name = cocktailJson.strDrink,
            category = cocktailJson.strCategory,
            alcoholic = cocktailJson.strAlcoholic,
            glass = cocktailJson.strGlass,
            instructions = cocktailJson.strInstructions,
            imageUrl = cocktailJson.strDrinkThumb,
            ingredients = ingredients
        )
    }

    @ToJson
    fun cocktailToJson(cocktail: DetailedCocktail): String {
        throw UnsupportedOperationException("Cannot convert DetailedCocktail to JSON")
    }
}