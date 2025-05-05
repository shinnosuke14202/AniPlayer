package com.example.aniplayer.site.manga

import com.example.aniplayer.model.manga.Manga
import com.example.aniplayer.model.manga.MangaChapter
import com.example.aniplayer.model.manga.MangaListFilter
import com.example.aniplayer.model.manga.MangaListFilterCapabilities
import com.example.aniplayer.model.manga.MangaPage
import com.example.aniplayer.model.parsers.SortOrder
import com.example.aniplayer.site.Site
import org.jsoup.nodes.Document

abstract class MangaSite : Site() {

    abstract val filterCapabilities: MangaListFilterCapabilities
    abstract val availableSortOrders: Set<SortOrder>

    abstract suspend fun getList(
        offset: Int,
        order: SortOrder,
        filter: MangaListFilter
    ): List<Manga>

    abstract suspend fun getDetails(manga: Manga): Manga

    abstract suspend fun getChapters(mangaId: String, mangaDocument: Document): List<MangaChapter>

    abstract suspend fun getPages(chapter: MangaChapter): List<MangaPage>

}
