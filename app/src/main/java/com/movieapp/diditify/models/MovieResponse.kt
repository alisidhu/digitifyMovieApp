package com.movieapp.diditify.models


data class MovieResponse(

    val page: Int? =0,
     val total_results: Int? = 0,
    val total_pages: Int? = 0,
    val results: List<Movie>
)