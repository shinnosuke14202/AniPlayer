package com.example.aniplayer.features.home.ui.sources

import android.widget.ListView
import com.example.aniplayer.databinding.FragmentAnimeSourceBinding

class AnimeSourceFragment :
    BaseSourceFragment<FragmentAnimeSourceBinding>(FragmentAnimeSourceBinding::inflate) {

    override val listView: ListView
        get() = binding.animeSection.lvSource

    override fun onSourceClick(categoryIndex: Int, siteIndex: Int) {
        // TODO
    }

    companion object {
        fun newInstance(index: Int): AnimeSourceFragment {
            return createSourceFragment(AnimeSourceFragment(), index)
        }

    }
}
