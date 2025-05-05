package com.example.aniplayer.features.list.viewmodel

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.aniplayer.features.list.paging.MangaPagingSource
import com.example.aniplayer.model.manga.Manga
import com.example.aniplayer.model.manga.MangaListFilter
import com.example.aniplayer.model.parsers.SortOrder
import com.example.aniplayer.site.manga.MangaSite

class MangaViewModel(private val mangaSite: MangaSite) :
    BaseItemViewModel<Manga, MangaListFilter>() {

    init {
        updateFilter(MangaListFilter())
    }

    override fun getPager(order: SortOrder, filter: MangaListFilter): Pager<Int, Manga> {
        val offset = mangaSite.offset
        return Pager(
            config = PagingConfig(
                pageSize = offset,
            ),
            pagingSourceFactory = {
                MangaPagingSource(mangaSite, order, filter, offset)
            }
        )
    }
}
