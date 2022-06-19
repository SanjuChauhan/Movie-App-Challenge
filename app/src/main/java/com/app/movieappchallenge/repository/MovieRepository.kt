package com.app.movieappchallenge.repository

import com.app.movieappchallenge.api.APIManager
import com.app.movieappchallenge.api.APIResponseListener

open class MovieRepository {

    /**
     * Get Movie List
     */
    fun getSearchMovie(params: Map<String, String>, listener: APIResponseListener) {
        val call = APIManager.instance?.defaultAPI?.getSearchMovie(params)
        APIManager.instance?.callAPI(call, listener)
    }

    /**
     * Get Movie Data By Id
     */
    fun getMovieDataById(
        params: Map<String, String>,
        listener: APIResponseListener
    ) {
        val call = APIManager.instance?.defaultAPI?.getMovieDataById(params)
        APIManager.instance?.callAPI(call, listener)
    }

    companion object {
        @JvmStatic
        val movieRepository: MovieRepository
            get() {
                return MovieRepository()
            }
    }
}
