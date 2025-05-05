package com.example.aniplayer.features.list.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.aniplayer.model.manga.Manga
import com.example.aniplayer.model.manga.MangaListFilter
import com.example.aniplayer.model.parsers.SortOrder
import com.example.aniplayer.site.manga.MangaSite

class MangaPagingSource(
    private val site: MangaSite,
    private val sortOrder: SortOrder,
    private val filter: MangaListFilter,
    private val offset: Int,
) : PagingSource<Int, Manga>() {

    override fun getRefreshKey(state: PagingState<Int, Manga>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1) ?: state.closestPageToPosition(
                anchor
            )?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Manga> {

        if (params.key == null) {
            val initialResults = site.getList(0, sortOrder, filter)

            if (initialResults.size < offset) {
                return LoadResult.Page(
                    data = initialResults,
                    prevKey = null,
                    nextKey = null
                )
            }

            return LoadResult.Page(
                data = initialResults,
                prevKey = null,
                nextKey = 1
            )
        }

        val page = params.key!!
        return try {
            val manga = site.getList(page * offset, sortOrder, filter)
            LoadResult.Page(
                data = manga,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (manga.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
