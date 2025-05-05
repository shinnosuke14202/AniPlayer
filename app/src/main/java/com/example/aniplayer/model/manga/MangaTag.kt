package com.example.aniplayer.model.manga

import android.os.Parcelable
import com.example.aniplayer.model.site.Source
import kotlinx.parcelize.Parcelize

@Parcelize
data class MangaTag(
    /**
     * User-readable tag title, should be in Title case
     */
    @JvmField val title: String,
    /**
     * Identifier of a tag, must be unique among the source.
     * @see MangaParser.getList
     */
    @JvmField val key: String,
    @JvmField val source: Source,
) : Parcelable
