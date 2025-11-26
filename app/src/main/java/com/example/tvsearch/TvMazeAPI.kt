package com.example.tvsearch

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TvMazeApi {

    @GET("search/shows")
    fun searchShows(
        @Query("q") query: String
    ): Call<List<SearchResult>>
}
