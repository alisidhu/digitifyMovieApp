package com.movieapp.diditify.fragments.movieDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.movieapp.diditify.R
import com.movieapp.diditify.adapter.cast.CastAdapter
import com.movieapp.diditify.adapter.cast.CastItemClickListener
import com.movieapp.diditify.databinding.FragmentMovieDetailBinding
import com.movieapp.diditify.models.Movie
import com.movieapp.diditify.models.MovieCast
import com.movieapp.diditify.utils.FavMovieUiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailBinding
    private lateinit var movie: Movie
    private val viewModel: MoviesDetailsViewModel by viewModels()
    private lateinit var castAdapter: CastAdapter
    private var isMovieFav = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        movie = MovieDetailFragmentArgs.fromBundle(requireArguments()).movie
        movie.title?.let { setupActionBar(it) }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this

        viewModel.favMovieUiState.observe(viewLifecycleOwner) {
            when (it) {
                FavMovieUiState.FavAdded -> favMovieAdded()
                is FavMovieUiState.FavStatus -> favMovieStatus(it.isFav)
                FavMovieUiState.FavRemoved -> favMovieRemoved()
            }
        }

        binding.fabFavorite.setOnClickListener {
            if (isMovieFav) {
                //removing movie as fav
                viewModel.removeFavMovie()
            } else {
                //adding movie as fav
                viewModel.addFavMovie()
            }
        }

        initAdapters()
        populateMoviesUI(movie)
        checkFavMovieStatus()
    }

    private fun favMovieStatus(fav: Boolean) {
        isMovieFav = fav
    }

    private fun favMovieAdded() {
        Toast.makeText(requireContext(), "Movie Saved as Favourite", Toast.LENGTH_SHORT)
            .show()
        isMovieFav = true
    }

    private fun favMovieRemoved() {
        Toast.makeText(requireContext(), "Movie Removed from Favourites", Toast.LENGTH_SHORT)
            .show()
        isMovieFav = false
    }

    private fun checkFavMovieStatus() {
        viewModel.getFavMovieStatus()
    }

    private fun populateMoviesUI(movie: Movie) {

        binding.movie = movie
        binding.executePendingBindings()

        viewModel.getMovieCast().observe(viewLifecycleOwner, {
            it?.let { castMembers ->
                if (castMembers.isNotEmpty()) {
                    castAdapter.submitMovieCastList(castMembers)
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.cast_error),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    binding.rvCast.visibility = View.GONE
                    binding.tvCastTitle.visibility = View.GONE
                }
            }
        })
    }

    private fun initAdapters() {
        binding.rvCast.hasFixedSize()

        binding.rvCast.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        castAdapter = CastAdapter(CastItemClickListener {
            Toast.makeText(requireContext(), (it as MovieCast).name, Toast.LENGTH_SHORT).show()
        })

        binding.rvCast.adapter = castAdapter
    }

    private fun setupActionBar(title: String) {
        ((activity as AppCompatActivity).supportActionBar)?.title = title
        setHasOptionsMenu(true)
    }

}