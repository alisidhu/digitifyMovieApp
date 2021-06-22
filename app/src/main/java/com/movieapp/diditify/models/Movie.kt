package com.movieapp.diditify.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    var popularity: Double? = 0.0,
    var vote_count: Int? = 0,
    var video: Boolean? = false,
    @SerializedName("poster_path") var poster_path: String?,
    var id: Int? = 0,
    var adult: Boolean? = false,
    var backdrop_path: String? = "",
    var original_language: String? = "",
    var original_title: String? = "",
    var genre_ids: List<Int>,
    var title: String? = "",
    var vote_average: Double? = 0.0,
    var overview: String? = "",
    var release_date: String? = ""
) : Parcelable {
    val fullPosterPath: String
        get() = "http://image.tmdb.org/t/p/w342$poster_path"
    val fullBackDropPath: String
        get() = "http://image.tmdb.org/t/p/w780$backdrop_path"
}

@Entity(tableName = "movie_info")
data class FavMovie(
    @PrimaryKey
    var id: Int
)