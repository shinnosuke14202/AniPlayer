package com.example.aniplayer.features.list.ui

import androidx.lifecycle.ViewModelProvider
import com.example.aniplayer.R
import com.example.aniplayer.features.details.ui.MangaDetailFragment
import com.example.aniplayer.features.list.adapter.BaseGridAdapter
import com.example.aniplayer.features.list.adapter.MangaAdapter
import com.example.aniplayer.features.list.factory.MangaViewModelFactory
import com.example.aniplayer.features.list.viewmodel.BaseItemViewModel
import com.example.aniplayer.features.list.viewmodel.MangaViewModel
import com.example.aniplayer.model.manga.Manga
import com.example.aniplayer.model.manga.MangaListFilter
import com.example.aniplayer.site.manga.MangaSite
import com.example.aniplayer.utils.Sources
import com.example.aniplayer.utils.fragment.replaceFragment

class MangaGridFragment : BaseGridFragment<Manga, MangaListFilter>() {

    val site = Sources.allCategories[categoryIndex].sources[siteIndex] as MangaSite

    override val viewModel: MangaViewModel
        get() = ViewModelProvider(
            this,
            MangaViewModelFactory(site)
        )[MangaViewModel::class.java]

    override fun createAdapter(): BaseGridAdapter<Manga> {
        return MangaAdapter(spanCount) {
            replaceFragment(R.id.mainFl, MangaDetailFragment.newInstance(it, site), true)
        }
    }

    override fun getFilterQuery(query: String): MangaListFilter {
        val currentFilter = viewModel.filter.value ?: MangaListFilter()
        return currentFilter.copy(
            query = query.trim()
        )
    }

    companion object {
        fun newInstance(categoryIndex: Int, siteIndex: Int) =
            createSourceFragment(MangaGridFragment(), categoryIndex, siteIndex)
    }
}
