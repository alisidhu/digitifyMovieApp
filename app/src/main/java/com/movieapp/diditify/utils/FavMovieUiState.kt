package com.movieapp.diditify.utils

import com.movieapp.diditify.models.FavMovie
import com.movieapp.diditify.models.Movie

sealed class FavMovieUiState {
    object FavAdded : FavMovieUiState()
    data class FavStatus(val isFav: Boolean) : FavMovieUiState()
    object FavRemoved : FavMovieUiState()
}

sealed class FavMovieListUiState{
    object Loading : FavMovieListUiState()
    object FavCleared : FavMovieListUiState()
    data class FavMovies(val favMovies: List<FavMovie>) : FavMovieListUiState()
    data class FavMoviesInfo(val favMoviesInfo : List<Movie>) : FavMovieListUiState()
}