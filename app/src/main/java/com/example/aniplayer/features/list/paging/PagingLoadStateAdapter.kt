package com.example.aniplayer.features.list.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aniplayer.R

class PagingLoadStateAdapter : LoadStateAdapter<PagingLoadStateAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_progress_bar_footer, parent, false)
        return LoadStateViewHolder(view)
    }

    inner class LoadStateViewHolder(
        view: View,
    ) : RecyclerView.ViewHolder(view) {
        private val progressBar = view.findViewById<ProgressBar>(R.id.progressCircularFooter)
        fun bind(loadState: LoadState) {
            progressBar.visibility = if (loadState is LoadState.Loading) View.VISIBLE else View.GONE
        }
    }
}
