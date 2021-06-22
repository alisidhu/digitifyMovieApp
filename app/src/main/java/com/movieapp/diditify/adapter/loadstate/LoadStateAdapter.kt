package com.movieapp.diditify.adapter.loadstate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.movieapp.diditify.databinding.LayoutLoadStateBinding

class LoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<com.movieapp.diditify.adapter.loadstate.LoadStateAdapter.LoadStateViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadStateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutLoadStateBinding.inflate(inflater, parent, false)

        return LoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {

        with(holder) {
            binding.lsButton.setOnClickListener { retry.invoke() }
            binding.lsProgress.isVisible = loadState is LoadState.Loading
            binding.lsButton.isVisible = loadState !is LoadState.Loading
            binding.lsError.isVisible = loadState !is LoadState.Loading
            binding.executePendingBindings()
        }

    }

    class LoadStateViewHolder(val binding: LayoutLoadStateBinding) :
        RecyclerView.ViewHolder(binding.root)

}
