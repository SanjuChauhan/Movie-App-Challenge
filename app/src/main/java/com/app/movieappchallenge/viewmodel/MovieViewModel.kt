package com.app.movieappchallenge.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.app.movieappchallenge.api.APIResponseListener
import com.app.movieappchallenge.constants.*
import com.app.movieappchallenge.model.MovieDetailResponse
import com.app.movieappchallenge.model.MovieSearchResponse
import com.app.movieappchallenge.model.MovieTitleData
import com.app.movieappchallenge.repository.MovieRepository.Companion.movieRepository
import kotlin.collections.set

class MovieViewModel(application: Application) : AndroidViewModel(application) {

    val strToastMessage = MutableLiveData<String>()
    val isShowProgressDialog = MutableLiveData<Boolean>()
    val strSearch = MutableLiveData<String>("")
    val movieTitlesListMutableData = MutableLiveData<List<MovieTitleData>>()
    val movieDetailMutableData = MutableLiveData<MovieDetailResponse>()

    /**
     * Call Search Movie API.
     */
    fun callSearchMovieAPI(strSearch: String, page: Int) {
        isShowProgressDialog.postValue(true)
        val params = HashMap<String, String>()
        params[PARAM_API_KEY] = API_KEY
        params[PARAM_SEARCH] = strSearch
        params[PARAM_TYPE] = TYPE
        params[PARAM_PAGE] = page.toString()

        movieRepository.getSearchMovie(params, object : APIResponseListener {
            override fun onSuccess(response: Any?) {
                val searchResponse = response as MovieSearchResponse
                isShowProgressDialog.postValue(false)
                movieTitlesListMutableData.postValue(searchResponse.search)
            }

            override fun onFail(message: String?) {
                strToastMessage.postValue(message)
                isShowProgressDialog.postValue(false)
            }
        })
    }

    /**
     * Call Movie Data By ID API.
     */
    fun callMovieDataByIdAPI(movieId: String) {
        isShowProgressDialog.postValue(true)
        val params = HashMap<String, String>()
        params[PARAM_API_KEY] = API_KEY
        params[PARAM_ID] = movieId

        movieRepository.getMovieDataById(params, object : APIResponseListener {
            override fun onSuccess(response: Any?) {
                val movieDetailResponse = response as MovieDetailResponse
                isShowProgressDialog.postValue(false)
                movieDetailMutableData.postValue(movieDetailResponse)
            }

            override fun onFail(message: String?) {
                strToastMessage.postValue(message)
                isShowProgressDialog.postValue(false)
            }
        })
    }
}