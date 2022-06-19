package com.app.movieappchallenge.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.core.text.bold
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.movieappchallenge.R
import com.app.movieappchallenge.constants.INTENT_EXTRA_ID
import com.app.movieappchallenge.databinding.ActivityMovieDetailBinding
import com.app.movieappchallenge.model.MovieDetailResponse
import com.app.movieappchallenge.viewmodel.MovieViewModel
import com.bumptech.glide.Glide
import timber.log.Timber

class MovieDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityMovieDetailBinding
    private lateinit var movieViewModel: MovieViewModel
    private var movieId: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail)
        binding.movieDetailActivity = this
        binding.lifecycleOwner = this

        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)

        initObserver()
        manageIntentData()
    }

    /***
     * This method for manage intent data
     */
    private fun manageIntentData() {
        val intent = intent
        if (intent.extras != null) {
            movieId = intent.getStringExtra(INTENT_EXTRA_ID)
            getMovieDetails(movieId!!)
        }
    }

    /***
     * This method is use to initialize observer
     */
    private fun initObserver() {
        movieViewModel.strToastMessage.observe(
            this,
            Observer { message -> showToast(message) })
        movieViewModel.isShowProgressDialog.observe(this, Observer {
            if (it) {
                showProgressDialog("Please Wait!")
            } else {
                dismissProgressDialog()
            }
        })
        movieViewModel.movieDetailMutableData.observe(this, Observer {
            Timber.e("Title %s", it.title)
            loadData(it)
        })
    }

    @SuppressLint("SetTextI18n")
    private fun loadData(data: MovieDetailResponse) {
        Timber.e("Poster %s", data.poster)
        Glide.with(this)
            .load(data.poster)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_placeholder)
            .centerCrop()
            .into(binding.ivImage)

        binding.tvMovieTitle.text = data.title
        binding.tvMovieRating.text = data.imdbRating
        binding.tvMovieYear.text = getFormattedString("Released on: ", data.released)
        binding.tvMovieLength.text = getFormattedString("RunTime: ", data.runtime)
        binding.tvMovieGenre.text = getFormattedString("Genre: ", data.genre)
        binding.tvMovieDirector.text = getFormattedString("Director: ", data.director)
        binding.tvMovieWriter.text = getFormattedString("Writer: ", data.writer)
        binding.tvMovieActors.text = getFormattedString("Actors: ", data.actors)
        binding.tvMovieLanguage.text = getFormattedString("Languages: ", data.language)
        binding.tvMovieCountry.text = getFormattedString("Country: ", data.country)
        binding.tvMovieAwards.text = getFormattedString("Awards: ", data.awards)
        binding.tvMoviePlot.text = getFormattedString("Plot: ", data.plot)
    }

    private fun getFormattedString(
        boldString: String,
        normalString: String
    ): SpannableStringBuilder {
        return SpannableStringBuilder()
            .bold { append(boldString) }
            .append(normalString)
    }

    /**
     * Get Movie Details
     */
    private fun getMovieDetails(movieId: String = "tt1375666") {
        if (checkNetworkState()) {
            movieViewModel.callMovieDataByIdAPI(movieId)
        } else {
            movieViewModel.strToastMessage.postValue(getString(R.string.msg_no_internet))
        }
    }

    fun closeScreen() {
        finish()
    }
}