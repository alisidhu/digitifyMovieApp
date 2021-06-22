package com.movieapp.diditify.adapter.movie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.movieapp.diditify.databinding.RvMovieListItemBinding
import com.movieapp.diditify.models.Movie
import com.movieapp.diditify.utils.DATA_VIEW_TYPE
import com.movieapp.diditify.utils.LOAD_STATE_VIEW_TYPE

class MovieListAdapter(private val itemClickListener: MovieItemClickListener) :
    PagingDataAdapter<Movie, RecyclerView.ViewHolder>(
        MovieDiffUtilCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvMovieListItemBinding.inflate(inflater, parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        with((holder as MovieViewHolder)) {
            binding.clickListener = itemClickListener
            binding.movie = getItem(position)
            binding.executePendingBindings()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) {
            DATA_VIEW_TYPE
        } else {
            LOAD_STATE_VIEW_TYPE
        }
    }


    class MovieViewHolder(val binding: RvMovieListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    class MovieDiffUtilCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
}

class MovieItemClickListener(val clickListener: (movie: Movie) -> Unit) {

    fun onClick(movie: Movie) = clickListener(movie)
}

