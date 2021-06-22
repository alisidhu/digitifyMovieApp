package com.movieapp.diditify.repos

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.movieapp.diditify.database.MovieInfoDAO
import com.movieapp.diditify.models.*
import com.movieapp.diditify.network.*
import com.movieapp.diditify.pagination.*
import com.movieapp.diditify.utils.PAGE_SIZE
import com.movieapp.diditify.utils.SECRET_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MoviesRepositoryImp @Inject constructor(
    private val service: Apis,
    private val moviesInfoDAO: MovieInfoDAO
) : MoviesRepository {

    override suspend fun saveFavMovie(favMovie: FavMovie) {
        moviesInfoDAO.insertFavMovie(favMovie)
    }

    override suspend fun removeFavMovie(favMovie: FavMovie) {
        moviesInfoDAO.deleteFavMovie(favMovie)
    }

    override suspend fun getAllFavMovies(): List<FavMovie> {
        return moviesInfoDAO.getAllFavMovies()
    }

    override  suspend fun checkFaveMovieStatus(id: Int): Boolean {
        return moviesInfoDAO.checkFavMovieStatus(id)
    }

    override  suspend fun clearFavourites() {
        moviesInfoDAO.clearFavourites()
    }

    override  fun getMovieList(category: String?, language: String?):
            Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MoviesListPaging(
                    service,
                    category,
                    language
                )
            }).flow
    }

    override  fun queryMovieList(query: String):
            Flow<PagingData<Movie>> {
        return Pager(config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false
        ),
            pagingSourceFactory = {
                SearchMoviePaging(
                    query,
                    service
                )
            }).flow
    }

    override  suspend fun getMovieGenres(): Result<List<MovieGenres>> {
        return withContext(Dispatchers.IO) {
            try {
                val movieGenres =
                    service.getMovieGenres(SECRET_KEY).genres
                Result.OnSuccess(movieGenres)

            } catch (exception: IOException) {
                Result.OnError(exception)
            } catch (exception: HttpException) {
                Result.OnError(exception)
            }
        }
    }

    override  suspend fun getMovieCast(movieId: Int): Result<List<MovieCast>> {
        return withContext(Dispatchers.IO) {
            try {
                val castMembers =
                    service.getMovieCredits(movieId, SECRET_KEY).cast
                Result.OnSuccess(castMembers)

            } catch (exception: IOException) {
                Result.OnError(exception)
            } catch (exception: HttpException) {
                Result.OnError(exception)
            }
        }
    }

    override  suspend fun loadMovieInfo(id: Int): Movie {

        return withContext(Dispatchers.IO){
            service.getMovie(id,SECRET_KEY)
        }

    }


}