package com.example.aniplayer.features.reader.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.aniplayer.model.manga.MangaChapter
import com.example.aniplayer.model.manga.MangaPage
import com.example.aniplayer.site.manga.MangaSite

class MangaPagePagingSource(
    private val site: MangaSite,
    private val chapters: List<MangaChapter>,
    private val position: Int,
) : PagingSource<Int, MangaPage>() {

    override fun getRefreshKey(state: PagingState<Int, MangaPage>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MangaPage> {
        val currentPosition = params.key ?: position
        return try {
            val mangaPages = site.getPages(chapters[currentPosition])
            val preKey = if (currentPosition - 1 >= 0) currentPosition - 1 else null
            val nextKey = if (currentPosition + 1 < chapters.size) currentPosition + 1 else null
            LoadResult.Page(
                data = mangaPages, prevKey = preKey, nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
