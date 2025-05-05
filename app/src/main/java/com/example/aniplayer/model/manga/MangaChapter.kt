package com.example.aniplayer.model.manga

import android.os.Parcelable
import com.example.aniplayer.model.site.Source
import com.example.aniplayer.utils.parsers.formatSimple
import kotlinx.parcelize.Parcelize

@Parcelize
data class MangaChapter(
    /**
     * An unique id of chapter
     */
    @JvmField val id: Long,

    /**
     * User-readable name of chapter if provided by parser or null instead
     * Do not pass manga title or chapter number here
     */
    @JvmField val title: String?,

    /**
     * Chapter number starting from 1, 0 if unknown
     */
    @JvmField val number: Float,

    /**
     * Volume number starting from 1, 0 if unknown
     */
    @JvmField val volume: Int,

    /**
     * Relative url to chapter (**without** a domain) or any other uri.
     * Used principally in parsers
     */
    @JvmField val url: String,

    /**
     * User-readable name of scanlator (releaser) or null if unknown
     */
    @JvmField val scanlator: String?,

    /**
     * Chapter upload date in milliseconds
     */
    @JvmField val uploadDate: Long,

    @JvmField val source: Source,
) : Parcelable {
    fun numberString(): String? = if (number > 0f) {
        number.formatSimple()
    } else {
        null
    }

    fun volumeString(): String? = if (volume > 0) {
        volume.toString()
    } else {
        null
    }
}
