package com.example.aniplayer.features.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.aniplayer.databinding.ItemInSourceBinding
import com.example.aniplayer.utils.view.ViewUtils

abstract class BaseGridAdapter<T : Any>(
    diffCallback: DiffUtil.ItemCallback<T>,
    private val spanCount: Int,
    private val onItemClick: (T) -> Unit,
) :
    PagingDataAdapter<T, BaseGridAdapter<T>.ItemViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemInSourceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return createViewHolder(binding)
    }

    protected abstract fun createViewHolder(binding: ItemInSourceBinding): ItemViewHolder

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(item)
        }

        val (itemWidth, itemHeight) = ViewUtils.calculateCardSize(
            holder.itemView.context,
            spanCount,
        )
        holder.itemView.layoutParams = RecyclerView.LayoutParams(itemWidth, itemHeight)
    }

    abstract inner class ItemViewHolder(binding: ItemInSourceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(item: T)
    }
}
