package com.movieapp.diditify.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.movieapp.diditify.models.Movie
import com.movieapp.diditify.network.Apis
import com.movieapp.diditify.utils.MOVIES_LIST_STARTING_PAGE
import com.movieapp.diditify.utils.SECRET_KEY
import retrofit2.HttpException
import java.io.IOException

class MovieSearchPagingSource(
    private val query: String,
    private val service: Apis
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: MOVIES_LIST_STARTING_PAGE
        return try {
            val response = service.searchMovie(query, SECRET_KEY, page)

            val movies = response.results
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

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition
    }
}