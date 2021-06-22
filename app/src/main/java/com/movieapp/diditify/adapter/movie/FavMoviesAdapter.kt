package com.movieapp.diditify.adapter.movie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.movieapp.diditify.databinding.RvMovieListItemBinding
import com.movieapp.diditify.models.Movie

class FavMoviesAdapter(
    private var moviesList: List<Movie>,
    private val itemClickListener: MovieItemClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MovieViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MovieViewHolder).bind(moviesList[position], itemClickListener)
    }

    override fun getItemCount() = moviesList.size


    class MovieViewHolder(private val binding: RvMovieListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Movie?, itemClickListener: MovieItemClickListener) {
            binding.clickListener = itemClickListener
            binding.movie = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MovieViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = RvMovieListItemBinding.inflate(inflater, parent, false)
                return MovieViewHolder(
                    binding
                )
            }
        }

    }
}