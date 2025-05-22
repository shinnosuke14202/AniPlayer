package com.example.aniplayer.features.reader.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil3.load
import coil3.request.crossfade
import coil3.request.error
import coil3.request.placeholder
import com.example.aniplayer.R
import com.example.aniplayer.databinding.ItemImageBinding
import com.example.aniplayer.model.manga.MangaPage

class ReaderAdapter(private val onItemClick: () -> Unit) :
    PagingDataAdapter<MangaPage, ReaderAdapter.ReaderViewHolder>(diffCallback = object :
        DiffUtil.ItemCallback<MangaPage>() {
        override fun areItemsTheSame(oldItem: MangaPage, newItem: MangaPage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MangaPage, newItem: MangaPage): Boolean {
            return oldItem == newItem
        }
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReaderViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReaderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReaderViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)
    }

    inner class ReaderViewHolder(private val binding: ItemImageBinding) : ViewHolder(binding.root) {
        fun bind(page: MangaPage) {
            binding.pv.load(page.url) {
                crossfade(false)
                placeholder(R.drawable.item_loading)
                error(R.drawable.item_error)
            }
            binding.pv.setOnPhotoTapListener(listener = { _, _, _ ->
                onItemClick()
            })
        }
    }
}
