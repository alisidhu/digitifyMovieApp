package com.movieapp.diditify.models


data class MovieCast(

    val cast_id: Int,
    val character: String,
    val credit_id: String,
    val gender: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val profile_path: String?
) {
    val fullProfilePath: String
        get() = "http://image.tmdb.org/t/p/w342$profile_path"
}