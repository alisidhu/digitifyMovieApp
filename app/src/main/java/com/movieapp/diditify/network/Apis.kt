package com.movieapp.diditify.network

import com.movieapp.diditify.utils.*
import com.movieapp.diditify.models.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Apis {

    @GET("search/movie")
    suspend fun searchMovie(
        @Query(MOVIE_SEARCH_QUERY) query: String,
        @Query(API_KEY) apiKey: String,
        @Query(CURRENT_PAGE) page: Int
    ): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovie(
        @Path(MOVIE_ID) movieId: Int,
        @Query(API_KEY) apiKey: String
    ): Movie

    @GET("movie/{category}")
    suspend fun getMovies(
        @Path(CATEGORY) category: String,
        @Query(API_KEY) apiKey: String,
        @Query(LANGUAGE) language: String,
        @Query(CURRENT_PAGE) page: Int
    ): MovieResponse

    @GET("genre/movie/list")
    suspend fun getMovieGenres(
        @Query(API_KEY) apiKey: String
    ): MovieGenreResponse

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path(MOVIE_ID) movieId: Int,
        @Query(API_KEY) apiKey: String
    ): MovieCreditsResponse
}