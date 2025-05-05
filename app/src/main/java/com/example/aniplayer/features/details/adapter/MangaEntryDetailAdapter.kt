package com.example.aniplayer.features.details.adapter

import com.example.aniplayer.databinding.ItemEntryInDetailBinding
import com.example.aniplayer.model.manga.MangaChapter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MangaEntryDetailAdapter(
    entryList: List<MangaChapter>,
    onItemClick: (MangaChapter) -> Unit
) : BaseEntryDetailAdapter<MangaChapter>(entryList, onItemClick) {

    override fun createEntryDetailViewHolder(binding: ItemEntryInDetailBinding): EntryDetailViewHolder {
        return MangaEntryDetailViewHolder(binding)
    }

    inner class MangaEntryDetailViewHolder(private val binding: ItemEntryInDetailBinding) :
        BaseEntryDetailAdapter<MangaChapter>.EntryDetailViewHolder(
            binding
        ) {
        override fun bind(entry: MangaChapter) {
            binding.tvTitle.text = entry.title
            binding.tvDate.text =
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(entry.uploadDate))
        }
    }
}
