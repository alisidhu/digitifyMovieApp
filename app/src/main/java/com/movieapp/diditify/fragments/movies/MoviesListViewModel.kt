package com.movieapp.diditify.fragments.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.movieapp.diditify.models.Movie
import com.movieapp.diditify.repos.MoviesRepositoryImp
import com.movieapp.diditify.utils.DEFAULT_LANGUAGE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MoviesListViewModel @Inject constructor(
    private val repository: MoviesRepositoryImp,

) :
    ViewModel() {
    private var currentCategory: String? = null
    private var currentQuery: String? = null
    private var currentSearchResult: Flow<PagingData<Movie>>? = null
    private var currentQueryResult: Flow<PagingData<Movie>>? = null

    fun getMoviesList(category: String): Flow<PagingData<Movie>> {
        val lastResult = currentSearchResult
        if (category == currentCategory &&
            lastResult != null
        ) {
            return lastResult
        }

        currentCategory = category
        val newResult = repository.getMovieList(category,DEFAULT_LANGUAGE)
            .cachedIn(viewModelScope)
        currentSearchResult = newResult

        return newResult

    }

    fun queryMovieList(query: String): Flow<PagingData<Movie>> {
        val lastResult = currentQueryResult
        if (query == currentQuery &&
            lastResult != null
        ) {
            return lastResult
        }

        currentQuery = query
        val newResult = repository.queryMovieList(query)
            .cachedIn(viewModelScope)
        currentQueryResult = newResult

        return newResult

    }


}