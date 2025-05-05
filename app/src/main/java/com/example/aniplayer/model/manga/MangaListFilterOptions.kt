package com.example.aniplayer.model.manga

import com.example.aniplayer.model.parsers.ContentType
import com.example.aniplayer.model.parsers.State
import java.util.Locale

data class MangaListFilterOptions(

    /**
     * Available tags (genres)
     */
    val availableTags: Set<MangaTag> = emptySet(),

    /**
     * Supported [State] variants for filtering. May be empty.
     *
     * For better performance use [EnumSet] for more than one item.
     */
    val availableStates: Set<State> = emptySet(),

    /**
     * Supported [ContentType] variants for filtering. May be empty.
     *
     * For better performance use [EnumSet] for more than one item.
     */
    val availableContentTypes: Set<ContentType> = emptySet(),

    /**
     * Supported content locales for multilingual sources
     */
    val availableLocales: Set<Locale> = emptySet(),
)
