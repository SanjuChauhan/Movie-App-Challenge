package com.app.movieappchallenge.model

import com.google.gson.annotations.SerializedName

class MovieSearchResponse {
    @SerializedName("Search")
    var search: List<MovieTitleData> = listOf()
    @SerializedName("totalResults")
    var totalResults: String = ""
    @SerializedName("Response")
    var response: String = ""
}