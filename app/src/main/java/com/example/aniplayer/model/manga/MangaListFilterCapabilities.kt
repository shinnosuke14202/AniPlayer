package com.example.aniplayer.model.manga

data class MangaListFilterCapabilities(

    /**
     * Whether parser supports filtering by more than one tag
     * @see [MangaListFilter.tags]
     * @see [MangaListFilterOptions.availableTags]
     */
    val isMultipleTagsSupported: Boolean = false,

    /**
     * Whether parser supports searching by string query
     * @see [MangaListFilter.query]
     */
    val isSearchSupported: Boolean = false,

    /**
     * Whether parser supports searching by string query combined within other filters
     */
    val isSearchWithFiltersSupported: Boolean = false,

    /**
     * Whether parser supports searching by author name
     * @see [MangaListFilter.author]
     */
    val isAuthorSearchSupported: Boolean = false,
)
