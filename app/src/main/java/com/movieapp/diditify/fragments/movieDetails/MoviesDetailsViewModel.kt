package com.movieapp.diditify.fragments.movieDetails

import androidx.lifecycle.*
import com.movieapp.diditify.models.FavMovie
import com.movieapp.diditify.models.Movie
import com.movieapp.diditify.models.MovieCast
import com.movieapp.diditify.models.MovieGenres
import com.movieapp.diditify.repos.MoviesRepositoryImp
import com.movieapp.diditify.utils.FavMovieUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import com.movieapp.diditify.network.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesDetailsViewModel @Inject
constructor(
    private val repository: MoviesRepositoryImp,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val movieId = savedStateHandle.get<Movie>("movie")?.id!!
    private val movie = savedStateHandle.get<Movie>("movie")

    private var currentMovieCastResult: MutableLiveData<List<MovieCast>>? = null
    private var currentMovieGenreResult: MutableLiveData<List<MovieGenres>>? = null
    private var currentMovieId: Int? = null
    var favMovieUiState = MutableLiveData<FavMovieUiState>()

    fun addFavMovie(){
        viewModelScope.launch {
            if (movie != null) {
                repository.saveFavMovie(FavMovie(movieId))
                favMovieUiState.value = FavMovieUiState.FavAdded
            }
        }
    }

    fun removeFavMovie(){
        viewModelScope.launch {
            if (movie != null) {
                repository.removeFavMovie(FavMovie(movieId))
                favMovieUiState.value = FavMovieUiState.FavRemoved
            }
        }
    }

    fun getFavMovieStatus(){
        viewModelScope.launch {
            if (movie != null) {
                val favStatus = repository.checkFaveMovieStatus(id = movieId)
                favMovieUiState.value = FavMovieUiState.FavStatus(favStatus)
            }
        }
    }

    fun getMovieGenre(): LiveData<List<MovieGenres>> {
        val genreList = MutableLiveData<List<MovieGenres>>()

        val lastResult = currentMovieGenreResult
        if (lastResult != null) {
            return lastResult
        }
        uiScope.launch {
            genreList.value = when (val movieGenres = repository.getMovieGenres()) {
                is Result.OnSuccess -> movieGenres.data
                is Result.OnError -> null
            }
            currentMovieGenreResult = genreList
        }
        return genreList
    }

    fun getMovieCast(): LiveData<List<MovieCast>> {
        val castMembers = MutableLiveData<List<MovieCast>>()

        val lastResult = currentMovieCastResult
        if (movieId == currentMovieId && lastResult != null) {
            return lastResult
        }

        currentMovieId = movieId
        uiScope.launch {
            castMembers.value = when (val result = repository.getMovieCast(movieId)) {
                is Result.OnSuccess -> result.data.filter { it.profile_path != null }
                is Result.OnError -> null
            }
            currentMovieCastResult = castMembers
        }
        return castMembers
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}