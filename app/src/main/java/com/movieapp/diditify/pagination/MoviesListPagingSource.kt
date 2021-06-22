package com.movieapp.diditify.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.movieapp.diditify.models.Movie
import com.movieapp.diditify.network.Apis
import com.movieapp.diditify.utils.CALENDAR_PATTERN
import com.movieapp.diditify.utils.MOVIES_LIST_STARTING_PAGE
import com.movieapp.diditify.utils.SECRET_KEY
import com.movieapp.diditify.utils.UPCOMING_CATEGORY
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException
import java.text.SimpleDateFormat
import java.util.*

class MoviesListPagingSource(
    private val service: Apis,
    private val category: String?,
    private val language: String?
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: MOVIES_LIST_STARTING_PAGE
        return try {
            val response = if (category != null && language != null) {
                service.getMovies(category, SECRET_KEY, language,page)
            } else {
                throw InvalidObjectException("Category should not be null!")
            }

            val movies = if (category == UPCOMING_CATEGORY) {
                response.results.filter {
                    it.release_date!! >= getCurrentDate()
                }
            } else {
                response.results
            }
            LoadResult.Page(
                data = movies,
                prevKey = if (page == MOVIES_LIST_STARTING_PAGE) null else page - 1,
                nextKey = if (movies.isEmpty()) null else page + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val date = calendar.time
        val sdf = SimpleDateFormat(CALENDAR_PATTERN, Locale.getDefault())
        return sdf.format(date)
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition
    }
}