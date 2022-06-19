package com.app.movieappchallenge.api

interface APIResponseListener {
    fun onSuccess(response: Any?)
    fun onFail(message: String?)
}