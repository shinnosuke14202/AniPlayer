package com.example.aniplayer.model.manga

import android.os.Parcelable
import com.example.aniplayer.model.site.Source
import kotlinx.parcelize.Parcelize

@Parcelize
data class MangaPage(
    @JvmField val id: Long,
    @JvmField val url: String,
    @JvmField val source: Source,
    @JvmField val chapterTitle: String,
) : Parcelable
