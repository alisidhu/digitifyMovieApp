package com.movieapp.diditify.fragments.favorites

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.movieapp.diditify.R
import com.movieapp.diditify.adapter.movie.FavMoviesAdapter
import com.movieapp.diditify.adapter.movie.MovieItemClickListener
import com.movieapp.diditify.databinding.FragmentFavoriteMoviesBinding
import com.movieapp.diditify.models.FavMovie
import com.movieapp.diditify.models.Movie
import com.movieapp.diditify.utils.FavMovieListUiState
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class FavoriteMoviesFragment : Fragment() {

    private val viewModel: FavMoviesViewModel by viewModels()
    private lateinit var binding: FragmentFavoriteMoviesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteMoviesBinding.inflate(inflater, container, false)
        initRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel.favMovieListUiState.observe(viewLifecycleOwner) {
            when (it) {
                FavMovieListUiState.Loading -> loading()
                is FavMovieListUiState.FavMovies -> favMoviesLoaded(it.favMovies)
                is FavMovieListUiState.FavMoviesInfo -> showFavMoviesInfo(it.favMoviesInfo)
                FavMovieListUiState.FavCleared -> favCleared()
            }
        }

        viewModel.getFavMoviesList()

    }

    private fun favCleared() {
        Toast.makeText(requireContext(), "All Favourite Movies cleared", Toast.LENGTH_SHORT).show()
        binding.pbMovies.visibility = View.GONE
        binding.rvMoviesList.adapter = null
    }

    private fun initRecyclerView() {
        Timber.d("rv init")
        binding.rvMoviesList.hasFixedSize()
        val layoutManager = GridLayoutManager(activity, resources.getInteger(R.integer.span_count))
        binding.rvMoviesList.layoutManager = layoutManager
    }

    private fun showFavMoviesInfo(favMoviesInfo: List<Movie>) {
        //hide loading bar
        binding.pbMovies.visibility = View.GONE

        val adapter = FavMoviesAdapter(favMoviesInfo, MovieItemClickListener {
            findNavController().navigate(
                FavoriteMoviesFragmentDirections.actionFavoriteMoviesFragmentToMovieDetailFragment(
                    it
                )
            )
        })
        binding.rvMoviesList.adapter = adapter

    }

    private fun favMoviesLoaded(favMovies: List<FavMovie>) {

        if (favMovies.isEmpty()) {
            //hide loading bar
            binding.pbMovies.visibility = View.GONE
            Toast.makeText(requireContext(), "No fav movies yet...", Toast.LENGTH_SHORT).show()
            return
        }
        //download fav movies data and show in rv
        viewModel.loadFavMoviesData(favMovies)
        Timber.d("Fav Movies: $favMovies")
    }

    private fun loading() {
        //show progress bar
        binding.pbMovies.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.fav_menu, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return if (item.itemId == R.id.item_delete_favourites) {

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Clear Favourites")
                .setMessage("Do you want to clear all Favourite Movies?")
                .setPositiveButton("Yes") { _, _ ->
                    viewModel.clearFavourites()
                }
                .setNegativeButton("No", null)
                .show()
            true
        } else
            super.onOptionsItemSelected(item)
    }

}