package com.example.aniplayer.features.reader.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.aniplayer.features.reader.paging.MangaPagePagingSource
import com.example.aniplayer.model.manga.MangaChapter
import com.example.aniplayer.model.manga.MangaPage
import com.example.aniplayer.site.manga.MangaSite
import kotlinx.coroutines.flow.Flow

class ReaderViewModel(mangaSite: MangaSite, chapters: List<MangaChapter>, position: Int) :
    ViewModel() {

    val pagingDataFlow: Flow<PagingData<MangaPage>> = Pager(
        config = PagingConfig(
            pageSize = 30, prefetchDistance = 1
        ), pagingSourceFactory = {
            MangaPagePagingSource(mangaSite, chapters, position)
        }).flow.cachedIn(viewModelScope)
}
