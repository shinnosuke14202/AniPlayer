package com.example.aniplayer.model.manga

import android.os.Parcelable
import com.example.aniplayer.model.parsers.State
import com.example.aniplayer.model.site.Source
import com.example.aniplayer.utils.parsers.findById
import kotlinx.parcelize.Parcelize

@Parcelize
data class Manga(
    /**
     * Unique identifier for manga
     */
    @JvmField val id: Long,
    /**
     * Manga title, human-readable
     */
    @JvmField val title: String,

    /**
     * Relative url to manga (**without** a domain) or any other uri.
     * Used principally in parsers
     */
    @JvmField val url: String,

    /**
     * Absolute url to manga, must be ready to open in browser
     */
    @JvmField val publicUrl: String,

    /**
     * Absolute link to the cover
     * @see largeCoverUrl
     */
    @JvmField val coverUrl: String?,

    /**
     * Tags (genres) of the manga
     */
    @JvmField val tags: Set<MangaTag>,

    /**
     * Manga status (ongoing, finished) or null if unknown
     */
    @JvmField val state: State?,

    /**
     * Authors of the manga
     */
    @JvmField val authors: Set<String>,

    /**
     * Large cover url (absolute), null if is no large cover
     * @see coverUrl
     */
    @JvmField val largeCoverUrl: String? = null,

    /**
     * Manga description, may be html or null
     */
    @JvmField val description: String? = null,

    /**
     * List of chapters
     */
    @JvmField val chapters: List<MangaChapter>? = null,

    /**
     * Manga source
     */
    @JvmField val source: Source,
) : Parcelable {
    fun findChapterById(id: Long): MangaChapter? = chapters?.findById(id)

    fun requireChapterById(id: Long): MangaChapter = findChapterById(id)
        ?: throw NoSuchElementException("Chapter with id $id not found")
}
