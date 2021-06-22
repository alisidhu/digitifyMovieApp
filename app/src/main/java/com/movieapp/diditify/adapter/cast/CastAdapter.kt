package com.movieapp.diditify.adapter.cast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.movieapp.diditify.databinding.RvCastListItemBinding
import com.movieapp.diditify.models.MovieCast
import com.movieapp.diditify.utils.MOVIE_VIEW_TYPE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CastAdapter(private val itemClickListener: CastItemClickListener) :
    ListAdapter<CastDataItem, RecyclerView.ViewHolder>(
        CastDiffUtilCallback()
    ) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = RvCastListItemBinding.inflate(inflater, parent, false)
        return MovieCastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movieCast = getItem(position) as CastDataItem.MovieCastItemCast
        with((holder as MovieCastViewHolder)) {
            holder.binding.cast = movieCast.movieCast
            holder.binding.clicklistener = itemClickListener
            holder.binding.executePendingBindings()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is CastDataItem.MovieCastItemCast -> MOVIE_VIEW_TYPE
        }
    }

    fun submitMovieCastList(list: List<MovieCast>) {
        adapterScope.launch {

            val castMembers = list.map { CastDataItem.MovieCastItemCast(it) }

            withContext(Dispatchers.Main) {
                submitList(castMembers)
            }
        }
    }

    class MovieCastViewHolder(val binding: RvCastListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}

class CastItemClickListener(val clickListener: (cast: Any) -> Unit) {
    fun onClick(cast: Any) {
        clickListener(cast)
    }
}

sealed class CastDataItem {
    data class MovieCastItemCast(val movieCast: MovieCast) : CastDataItem() {
        override val id: Int
            get() = movieCast.id
    }

    abstract val id: Int
}

class CastDiffUtilCallback : DiffUtil.ItemCallback<CastDataItem>() {

    override fun areItemsTheSame(oldItemCast: CastDataItem, newItemCast: CastDataItem): Boolean {
        return oldItemCast.id == newItemCast.id
    }

    override fun areContentsTheSame(oldItemCast: CastDataItem, newItemCast: CastDataItem): Boolean {
        return oldItemCast == newItemCast
    }
}

