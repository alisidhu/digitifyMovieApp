package com.movieapp.diditify.models

data class MovieCreditsResponse(

val id: Int,
 val cast: List<MovieCast>,
val crew: List<MovieCrew>
)