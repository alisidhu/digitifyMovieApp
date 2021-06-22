package com.movieapp.diditify.fragments.movies

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.IBinder
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.movieapp.diditify.R
import com.movieapp.diditify.adapter.loadstate.LoadStateAdapter
import com.movieapp.diditify.adapter.movie.MovieItemClickListener
import com.movieapp.diditify.adapter.movie.MovieListAdapter
import com.movieapp.diditify.base.BaseFragment
import com.movieapp.diditify.databinding.FragmentMoviesListBinding
import com.movieapp.diditify.models.Movie
import com.movieapp.diditify.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MoviesListFragment : BaseFragment<FragmentMoviesListBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMoviesListBinding
        get() = FragmentMoviesListBinding::inflate

    private val viewModel: MoviesListViewModel by viewModels()
    private lateinit var listAdapter: MovieListAdapter
    private lateinit var searchCategory: String
    private var lastResult: PagingData<Movie>? = null
    @Inject
    lateinit var preferences: SharedPreferences



    override fun initViews() {
        searchCategory = preferences.getString(MOVIE_CATEGORY_KEY, MOVIE_CATEGORY_DEFAULT)!!
        setupActionBarTitle(searchCategory)
    }

    private fun setupActionBarTitle(searchCategory: String) {
        val titleText = when (searchCategory) {
            POPULAR_CATEGORY -> getString(R.string.popular)
            NOW_PLAYING_CATEGORY -> getString(R.string.now_playing)
            UPCOMING_CATEGORY -> getString(R.string.upcoming)
            TOP_RATED_CATEGORY -> getString(R.string.top_rated)
            else -> getString(R.string.movies)
        }
        ((activity as AppCompatActivity).supportActionBar)?.title = titleText
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponents()

        if (lastResult == null) {
            fetchMovies(searchCategory)
        } else {
            lifecycleScope.launch {
                listAdapter.submitData(lastResult!!)
            }
        }
    }

    private fun fetchMovies(category: String) {
        lifecycleScope.launch {
            viewModel.getMoviesList(category).collectLatest {
                lastResult = it
                listAdapter.submitData(it)
            }
        }
    }

    private fun initComponents() {

        binding.lifecycleOwner = viewLifecycleOwner

        initChipGroup()
        initSearch()
        initAdapter()
        binding.btnRetry.setOnClickListener { listAdapter.retry() }
    }

    private fun initSearch() {
        binding.txtSearchMovies.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                hideSoftKeyboard(v.windowToken)

                val searchString = binding.txtSearchMovies.text.toString().trim()

                if (searchString.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.empty_movie_name),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@OnEditorActionListener true
                }

                queryMovieList(searchString)
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun hideSoftKeyboard(windowToken: IBinder) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun initChipGroup() {
        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->

            when(checkedId){
                R.id.chip_popular -> {
                    fetchMovies(POPULAR_CATEGORY)
                    setupActionBarTitle(POPULAR_CATEGORY)
                }
                R.id.chip_now_playing -> {
                    fetchMovies(NOW_PLAYING_CATEGORY)
                    setupActionBarTitle(NOW_PLAYING_CATEGORY)
                }
                R.id.chip_top_rated -> {
                    fetchMovies(TOP_RATED_CATEGORY)
                    setupActionBarTitle(POPULAR_CATEGORY)
                }
                R.id.chip_upcoming -> {
                    fetchMovies(UPCOMING_CATEGORY)
                    setupActionBarTitle(UPCOMING_CATEGORY)
                }
            }
        }
    }

    private fun initAdapter() {

        listAdapter = MovieListAdapter(MovieItemClickListener {

            findNavController().navigate(
                MoviesListFragmentDirections.actionMoviesFragmentToMovieDetailFragment(
                    it
                )
            )
        }).apply {
            addLoadStateListener { loadState ->
                // If list has items. Show
                binding.rvMoviesList.isVisible = loadState.source.refresh is LoadState.NotLoading
                // If loading or refreshing show spinner
                binding.pbMovies.isVisible = loadState.source.refresh is LoadState.Loading
                // If initial load fails show Retry button and text
                binding.btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                binding.tvMoviesError.isVisible = loadState.source.refresh is LoadState.Error
                binding.btnRetry.isVisible = loadState.source.refresh is LoadState.Error
            }
        }

        binding.rvMoviesList.adapter = listAdapter.withLoadStateHeaderAndFooter(
            header = LoadStateAdapter { listAdapter.retry() },
            footer = LoadStateAdapter { listAdapter.retry() }
        )

        // RecyclerView
        binding.rvMoviesList.hasFixedSize()
        val layoutManager = GridLayoutManager(activity, resources.getInteger(R.integer.span_count))
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = listAdapter.getItemViewType(position)
                return if (viewType == LOAD_STATE_VIEW_TYPE) 1
                else resources.getInteger(R.integer.span_count)
            }
        }
        binding.rvMoviesList.layoutManager = layoutManager

    }

    private fun queryMovieList(movieQuery: String) {
        lifecycleScope.launch {
            viewModel.queryMovieList(movieQuery).collectLatest {
                lastResult = it
                listAdapter.submitData(it)
            }
        }
    }


}