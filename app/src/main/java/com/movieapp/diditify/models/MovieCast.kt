package com.movieapp.diditify.models

import com.movieapp.diditify.utils.POSTER_URL


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
        get() = POSTER_URL.plus(profile_path)
}