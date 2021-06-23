package com.movieapp.diditify.repos

import androidx.paging.PagingData
import com.movieapp.diditify.models.FavMovie
import com.movieapp.diditify.models.Movie
import com.movieapp.diditify.models.MovieCast
import com.movieapp.diditify.models.MovieGenres
import com.movieapp.diditify.network.Result
import kotlinx.coroutines.flow.Flow


interface MoviesRepository {
    fun getMovieList(category: String?, language: String?):
            Flow<PagingData<Movie>>

    fun queryMovieList(query: String):
            Flow<PagingData<Movie>>

    suspend fun saveFavMovie(favMovie: FavMovie)
    suspend fun removeFavMovie(favMovie: FavMovie)
    suspend fun getAllFavMovies(): List<FavMovie>
    suspend fun checkFaveMovieStatus(id: Int): Boolean
    suspend fun clearFavourites()


    suspend fun getMovieGenres(): Result<List<MovieGenres>>
    suspend fun getMovieCast(movieId: Int): Result<List<MovieCast>>

    suspend fun loadMovieInfo(id: Int): Movie
}