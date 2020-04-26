package com.kdz.topstories.api.endpoints

import com.kdz.topstories.api.API
import com.kdz.topstories.api.responses.GetArticlesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface NYTimesEndpoint {

    companion object {
        const val baseUrl = "topstories/v2/"

    }

    /**
     * Retrieves Top Stories by category. We will only be using the "Home" section for this example.
     */
    @GET("$baseUrl{section}.json?api-key=${API.APIKey}")
    fun getTopStories(@Path("section") section: String): Single<GetArticlesResponse>

}