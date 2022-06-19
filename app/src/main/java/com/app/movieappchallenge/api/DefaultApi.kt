package com.app.movieappchallenge.api

import com.app.movieappchallenge.model.MovieDetailResponse
import com.app.movieappchallenge.model.MovieSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface DefaultApi {

    /**
     * get Search Movie
     */
    @GET("/")
    fun getSearchMovie(
        @QueryMap params: Map<String, String>
    ): Call<MovieSearchResponse>

    /**
     * get Movie Detail By Id
     */
    @GET("/")
    fun getMovieDataById(
        @QueryMap params: Map<String, String>
    ): Call<MovieDetailResponse>
}