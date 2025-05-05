package com.example.aniplayer.features.reader.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.aniplayer.model.manga.MangaChapter
import com.example.aniplayer.model.manga.MangaPage
import com.example.aniplayer.site.manga.MangaSite

class MangaPagePagingSource(
    private val site: MangaSite,
    private val chapter: MangaChapter,
) : PagingSource<Int, MangaPage>() {

    override fun getRefreshKey(state: PagingState<Int, MangaPage>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MangaPage> {
        return try {
            val mangaPages = site.getPages(chapter)
            LoadResult.Page(
                data = mangaPages, prevKey = null, nextKey = null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
