package com.movieapp.diditify.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.movieapp.diditify.models.FavMovie

@Dao
interface MovieInfoDAO {

    @Query("SELECT * FROM movie_info")
    suspend fun getAllFavMovies(): List<FavMovie>

    @Query("SELECT EXISTS (SELECT 1 FROM movie_info WHERE id = :id)")
    suspend fun checkFavMovieStatus(id: Int): Boolean

    @Insert
    suspend fun insertFavMovie(favMovie: FavMovie)

    @Delete
    suspend fun deleteFavMovie(favMovie: FavMovie)

    @Query("DELETE FROM movie_info")
    suspend fun clearFavourites()
}