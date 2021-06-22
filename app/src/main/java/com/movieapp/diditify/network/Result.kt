package com.movieapp.diditify.network

sealed class Result<out T : Any> {
    data class OnSuccess<out T : Any>(val data: T) : Result<T>()
    data class OnError(val exception: Exception) : Result<Nothing>()
}