package com.example.aniplayer.features.home.ui.sources

import android.widget.ListView
import com.example.aniplayer.MainActivity
import com.example.aniplayer.R
import com.example.aniplayer.databinding.FragmentMangaSourceBinding
import com.example.aniplayer.features.list.ui.MangaGridFragment
import com.example.aniplayer.utils.fragment.replaceFragment

class MangaSourceFragment :
    BaseSourceFragment<FragmentMangaSourceBinding>(FragmentMangaSourceBinding::inflate) {

    override val listView: ListView get() = binding.mangaSection.lvSource

    override fun onSourceClick(categoryIndex: Int, siteIndex: Int) {
        (activity as? MainActivity)?.toggleBottomNavigationView()
        replaceFragment(
            R.id.mainFl,
            MangaGridFragment.newInstance(categoryIndex, siteIndex),
            true
        )
    }

    companion object {
        fun newInstance(index: Int): MangaSourceFragment {
            return createSourceFragment(MangaSourceFragment(), index)
        }
    }
}
