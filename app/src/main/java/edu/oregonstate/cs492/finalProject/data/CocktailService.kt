package edu.oregonstate.cs492.finalProject.data

import com.squareup.moshi.Moshi
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailService {
    @GET("lookup.php")
    suspend fun getCocktailById(
        @Query("i") id: String
    ) : Response<DetailedCocktailList>

    @GET("filter.php")
    suspend fun getCocktailsByCategory(
        @Query("c") category: String
    ) : Response<ShortCocktailList>

    companion object {
        private const val BASE_URL = "https://www.thecocktaildb.com/api/json/v1/1/"

        fun create() : CocktailService {
            val moshi = Moshi.Builder()
                .add(ShortCocktailJsonAdapter())
                .add(DetailedCocktailJsonAdapter())
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(CocktailService::class.java)
        }
    }
}