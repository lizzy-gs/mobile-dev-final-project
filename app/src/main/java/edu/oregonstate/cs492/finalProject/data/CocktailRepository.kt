package edu.oregonstate.cs492.finalProject.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.minutes
import kotlin.time.TimeSource

class CocktailRepository (
    private val service: CocktailService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private var currentId: String? = null
    private var cachedCocktailDetails: DetailedCocktailList? = null
    private var currentCategory: String? = null
    private var cachedCocktails: ShortCocktailList? = null

    /*
     * These values are used to help measure the age of the cached forecast.  See the Kotlin
     * documentation on time measurement for details:
     *
     * https://kotlinlang.org/docs/time-measurement.html
     */
    private val cacheMaxAge = 5.minutes
    private val timeSource = TimeSource.Monotonic
    private var timeStamp = timeSource.markNow()

    suspend fun getCocktailsByCategory(
        category: String
    ) : Result<ShortCocktailList?> {
        /*
         * If we can do so, return the cached cocktails without making a network call.  Otherwise,
         * make an API call to fetch the forecast and cache it.
         */
        return if (shouldFetchList(category)) {
            withContext(ioDispatcher) {
                try {
                    val response = service.getCocktailsByCategory(category)
                    if (response.isSuccessful) {
                        cachedCocktails = response.body()
                        timeStamp = timeSource.markNow()
                        currentCategory = category
                        Result.success(cachedCocktails)
                    } else {
                        Result.failure(Exception(response.errorBody()?.string()))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Result.failure(e)
                }
            }
        } else {
            Result.success(cachedCocktails!!)
        }
    }

    suspend fun getCocktailDetails(
        id: String
    ): Result<DetailedCocktailList?> {
        return if (shouldFetchDetails(id)) {
            withContext(ioDispatcher) {
                try {
                    val response = service.getCocktailById(id)
                    if (response.isSuccessful) {
                        cachedCocktailDetails = response.body()
                        timeStamp = timeSource.markNow()
                        currentId = id
                        Result.success(cachedCocktailDetails)
                    } else {
                        Result.failure(Exception(response.errorBody()?.string()))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Result.failure(e)
                }
            }
        } else {
            Result.success(cachedCocktailDetails!!)
        }
    }

    private fun shouldFetchList(category: String): Boolean =
        cachedCocktails == null
                || category != currentCategory
                || (timeStamp + cacheMaxAge).hasPassedNow()

    private fun shouldFetchDetails(id: String): Boolean =
        cachedCocktailDetails == null
                || id != currentId
                || (timeStamp + cacheMaxAge).hasPassedNow()
}