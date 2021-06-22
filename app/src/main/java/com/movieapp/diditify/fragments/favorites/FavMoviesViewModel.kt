package com.movieapp.diditify.fragments.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieapp.diditify.models.FavMovie
import com.movieapp.diditify.models.Movie
import com.movieapp.diditify.repos.MoviesRepositoryImp
import com.movieapp.diditify.utils.FavMovieListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavMoviesViewModel @Inject constructor(
    private val repository: MoviesRepositoryImp
) : ViewModel() {

    var favMovieListUiState = MutableLiveData<FavMovieListUiState>()

    fun getFavMoviesList() {
        favMovieListUiState.value = FavMovieListUiState.Loading
        viewModelScope.launch {

            val favMovies = repository.getAllFavMovies()
            favMovieListUiState.value = FavMovieListUiState.FavMovies(favMovies)
        }
    }

    fun loadFavMoviesData(movies: List<FavMovie>) {
        
        viewModelScope.launch { 
            favMovieListUiState.value = FavMovieListUiState.Loading
            val favMovies = mutableListOf<Movie>()
            for(favMovie in movies){
                val movieInfo = repository.loadMovieInfo(favMovie.id)
                favMovies.add(movieInfo)
            }
            favMovieListUiState.value = FavMovieListUiState.FavMoviesInfo(favMovies)
        }
        
        
    }

    fun clearFavourites() {

        viewModelScope.launch {
            favMovieListUiState.value = FavMovieListUiState.Loading
            repository.clearFavourites()
            favMovieListUiState.value = FavMovieListUiState.FavCleared
        }
    }
}