package com.example.aniplayer.features.list.adapter

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import coil3.load
import coil3.request.crossfade
import coil3.request.error
import coil3.request.placeholder
import com.example.aniplayer.R
import com.example.aniplayer.databinding.ItemInSourceBinding
import com.example.aniplayer.model.manga.Manga

class MangaAdapter(
    private val spanCount: Int,
    onMangaClick: (Manga) -> Unit,
) : BaseGridAdapter<Manga>(diffCallback = object : DiffUtil.ItemCallback<Manga>() {
    override fun areItemsTheSame(oldItem: Manga, newItem: Manga): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Manga, newItem: Manga): Boolean {
        return oldItem == newItem
    }
}, spanCount, onMangaClick) {

    inner class MangaViewHolder(private val binding: ItemInSourceBinding) :
        ItemViewHolder(binding) {
        override fun bind(item: Manga) {
            if (spanCount == 2) {
                val params =
                    binding.shapeableImageView.layoutParams as ConstraintLayout.LayoutParams
                params.matchConstraintPercentHeight = 0.8f
                binding.shapeableImageView.layoutParams = params
            }
            binding.shapeableImageView.load(item.coverUrl) {
                crossfade(true)
                placeholder(R.drawable.item_loading)
                error(R.drawable.item_error)
            }
            binding.tvTitle.text = item.title
        }
    }

    override fun createViewHolder(binding: ItemInSourceBinding): ItemViewHolder {
        return MangaViewHolder(binding)
    }
}
