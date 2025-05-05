package com.example.aniplayer.utils

import com.example.aniplayer.model.site.SiteModel
import com.example.aniplayer.site.Site
import com.example.aniplayer.site.manga.MangaSite
import com.example.aniplayer.site.manga.WeebCentral

object Sources {
    const val TOTAL = 2

    private val mangaSources = SiteModel(
        name = "Manga",
        sources = listOf<MangaSite>(WeebCentral())
    )
    private val animeSources = SiteModel(
        name = "Anime",
        sources = listOf<Site>()
    )

    val allCategories = listOf(
        mangaSources,
        animeSources
    )
}
