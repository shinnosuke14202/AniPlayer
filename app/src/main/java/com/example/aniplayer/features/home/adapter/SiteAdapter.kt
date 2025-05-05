package com.example.aniplayer.features.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.aniplayer.databinding.ItemSourceBinding
import com.example.aniplayer.site.Site

class SiteAdapter(
    context: Context,
    private val sites: List<Site>
) : ArrayAdapter<Site>(context, 0, sites) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ItemSourceBinding
        val view: View

        if (convertView == null) {
            binding = ItemSourceBinding.inflate(LayoutInflater.from(context), parent, false)
            view = binding.root
            view.tag = binding
        } else {
            view = convertView
            binding = view.tag as ItemSourceBinding
        }

        val site = sites[position]
        site.source.image?.let { binding.ivSource.setImageResource(it) }
        binding.tvTitle.text = site.source.name

        return view
    }

}
