package com.app.movieappchallenge.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.movieappchallenge.R
import com.app.movieappchallenge.constants.INTENT_EXTRA_ID
import com.app.movieappchallenge.databinding.ActivityMovieListBinding
import com.app.movieappchallenge.model.MovieTitleData
import com.app.movieappchallenge.ui.adapter.MovieDataRvAdapter
import com.app.movieappchallenge.viewmodel.MovieViewModel
import timber.log.Timber

class MovieListActivity : BaseActivity() {

    private lateinit var binding: ActivityMovieListBinding
    private lateinit var movieViewModel: MovieViewModel
    private var movieDataRvAdapter: MovieDataRvAdapter? = null
    var isLoadMoreFeedPeopleListData: Boolean = true
    var isFromSearch: Boolean = false
    private var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_list)
        binding.lifecycleOwner = this

        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        binding.viewModel = movieViewModel

        initObserver()
        initAdapter()
        getSearchedMovie()
    }

    /***
     * This method is use to initialize observer
     */
    private fun initObserver() {
        movieViewModel.strToastMessage.observe(
            this,
            Observer { message -> showToast(message) })
        movieViewModel.movieTitlesListMutableData.observe(this, Observer { data ->
            Timber.e("List Size : %s", data.size)
            if (isFromSearch) {
                movieDataRvAdapter?.clear()
            }
            if (data.isEmpty() && isFromSearch) {
                binding.tvEmpty.visibility = View.VISIBLE

                isLoadMoreFeedPeopleListData = false
            } else {
                binding.tvEmpty.visibility = View.GONE
                movieDataRvAdapter?.addAll(data)
                isLoadMoreFeedPeopleListData = true
            }
        })
        movieViewModel.strSearch.observe(this, Observer { str ->
            Timber.e("Search %s", str)
            if (str.isNotEmpty()) {
                isFromSearch = true
                getSearchedMovie(str)
            } else {
                isFromSearch = true
                getSearchedMovie()
            }
        })
    }

    private fun initAdapter() {
        val linearLayoutManager = LinearLayoutManager(this)
        val gridLayoutManager = GridLayoutManager(this,2)
        binding.rvMovie.layoutManager = gridLayoutManager

//        val itemDecorator = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
//        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider)!!)
//        binding.rvMovie.addItemDecoration(itemDecorator)

        movieDataRvAdapter = MovieDataRvAdapter(this)
        binding.rvMovie.adapter = movieDataRvAdapter
        movieDataRvAdapter?.setItemClickListener(object :
            MovieDataRvAdapter.OnItemClickListener {
            override fun onItemClick(data: MovieTitleData, position: Int) {
                onNavigateToMovieDetails(data)
            }
        })
        setUpPaginationScroll(gridLayoutManager)
    }

    private fun setUpPaginationScroll(gridLayoutManager: GridLayoutManager) {
        binding.rvMovie.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount: Int = gridLayoutManager.childCount
                val totalItemCount: Int = gridLayoutManager.itemCount
                val firstVisibleItemPosition: Int =
                    gridLayoutManager.findFirstVisibleItemPosition()

                if (isLoadMoreFeedPeopleListData && visibleItemCount + firstVisibleItemPosition >= totalItemCount) {
                    Timber.e("Call api for new data")
                    isLoadMoreFeedPeopleListData = false
                    isFromSearch = false
                    currentPage += 1
                    if(movieViewModel.strSearch.value!!.isEmpty()){
                        getSearchedMovie(page = currentPage)
                    } else {
                        getSearchedMovie(movieViewModel.strSearch.value!!, currentPage)
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    private fun onNavigateToMovieDetails(data: MovieTitleData) {
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra(INTENT_EXTRA_ID, data.imdbID)
        startActivity(intent)
    }

    /**
     * Get Searched Movie
     */
    private fun getSearchedMovie(strSearch: String= "Marvel", page: Int = 1) {
        if (checkNetworkState()) {
            movieViewModel.callSearchMovieAPI(strSearch,page)
        } else {
            movieViewModel.strToastMessage.postValue(getString(R.string.msg_no_internet))
        }
    }
}