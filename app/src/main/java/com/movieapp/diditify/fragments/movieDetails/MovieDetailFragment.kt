package com.movieapp.diditify.fragments.movieDetails

import android.animation.AnimatorSet
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
import com.movieapp.diditify.utils.setImage
import com.movieapp.diditify.databinding.FragmentMovieDetailBinding
import com.movieapp.diditify.models.Movie
import com.movieapp.diditify.models.MovieCast
import com.movieapp.diditify.utils.FavMovieUiState
import dagger.hilt.android.AndroidEntryPoint
import render.animations.*

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
        setFavoriteIcon(isMovieFav)

    }

    private fun favMovieAdded() {
        Toast.makeText(requireContext(), "Movie Saved as Favourite", Toast.LENGTH_SHORT)
            .show()
        isMovieFav = true

        setAnimation(Zoom().InUp(binding.fabFavorite))

        setFavoriteIcon(isMovieFav)
        setFavoriteIcon(isMovieFav)
    }


    private fun favMovieRemoved() {
        Toast.makeText(requireContext(), "Movie Removed from Favourites", Toast.LENGTH_SHORT)
            .show()
        isMovieFav = false
        setAnimation(Rotate().OutUpRight(binding.fabFavorite))
        setAnimation(Rotate().In(binding.fabFavorite))
        setFavoriteIcon(isMovieFav)

    }

    private fun setFavoriteIcon(isMovieFav: Boolean) {
        if (isMovieFav)
            binding.fabFavorite.setImage(R.drawable.ic_baseline_favorite_24)
        else
            binding.fabFavorite.setImage(R.drawable.ic_baseline_favorite_border_24)

    }
    private fun setAnimation(animatorSet: AnimatorSet){
        val render = Render(requireContext())

        render.setAnimation(animatorSet)
        render.start()
    }
    private fun checkFavMovieStatus() {
        viewModel.getFavMovieStatus()
    }

    private fun populateMoviesUI(movie: Movie) {

        binding.movie = movie
        binding.executePendingBindings()
        setAnimation(Fade().InDown(binding.fabFavorite))
        setAnimation(Zoom().InUp(binding.ivPosterPic))
        setAnimation(Slide().InUp(binding.ivPoster))
        setAnimation(Slide().InDown(binding.llMain))
        viewDetailClick()
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

    private fun viewDetailClick(){
        binding.btnViewDetail.setOnClickListener {
            binding.llCastView.visibility = View.GONE
            binding.llDetailView.visibility = View.VISIBLE

            binding.ivPoster.visibility = View.VISIBLE
            binding.ivPosterPic.visibility = View.VISIBLE
            binding.fabFavorite.visibility = View.VISIBLE
            setAnimation(Slide().InDown(binding.clMainView))

        }
    }
    private fun initAdapters() {
        binding.rvCast.hasFixedSize()

        binding.rvCast.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        castAdapter = CastAdapter(CastItemClickListener {
            binding.cast = it as MovieCast
            binding.llCastView.visibility = View.VISIBLE
            binding.llDetailView.visibility = View.GONE
            binding.ivPoster.visibility = View.GONE
            binding.ivPosterPic.visibility = View.GONE
            binding.fabFavorite.visibility = View.GONE
            setAnimation(Slide().InUp(binding.llCastView))

            // Toast.makeText(requireContext(), (it as MovieCast).name, Toast.LENGTH_SHORT).show()
        })

        binding.rvCast.adapter = castAdapter
    }

    private fun setupActionBar(title: String) {
        ((activity as AppCompatActivity).supportActionBar)?.title = title
        setHasOptionsMenu(true)
    }

}