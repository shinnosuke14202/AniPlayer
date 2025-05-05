package com.example.aniplayer.features.details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aniplayer.databinding.ItemEntryInDetailBinding

abstract class BaseEntryDetailAdapter<T>(
    private val entryList: List<T>,
    private val onItemClick: (T) -> Unit,
) : RecyclerView.Adapter<BaseEntryDetailAdapter<T>.EntryDetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryDetailViewHolder {
        val binding =
            ItemEntryInDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return createEntryDetailViewHolder(binding)
    }

    abstract fun createEntryDetailViewHolder(binding: ItemEntryInDetailBinding): EntryDetailViewHolder

    override fun getItemCount(): Int {
        return entryList.size
    }

    override fun onBindViewHolder(holder: EntryDetailViewHolder, position: Int) {
        val entry = entryList[position]
        holder.bind(entry)
        holder.itemView.setOnClickListener {
            onItemClick.invoke(entry)
        }
    }

    abstract inner class EntryDetailViewHolder(private val binding: ItemEntryInDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(entry: T)
    }
}
