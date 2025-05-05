package com.example.aniplayer.model.manga

import com.example.aniplayer.model.parsers.ContentType
import com.example.aniplayer.model.parsers.State
import java.util.Locale

data class MangaListFilter(
    @JvmField val query: String? = null,
    @JvmField val tags: Set<MangaTag> = emptySet(),
    @JvmField val locale: Locale? = null,
    @JvmField val states: Set<State> = emptySet(),
    @JvmField val types: Set<ContentType> = emptySet(),
    @JvmField val author: String? = null,
) {
    private fun isNonSearchOptionsEmpty(): Boolean = tags.isEmpty() &&
            locale == null &&
            states.isEmpty() &&
            author.isNullOrEmpty()

    fun isEmpty(): Boolean = isNonSearchOptionsEmpty() && query.isNullOrEmpty()

    fun isNotEmpty(): Boolean = !isEmpty()

    fun hasNonSearchOptions(): Boolean = !isNonSearchOptionsEmpty()

    companion object {

        @JvmStatic
        val EMPTY: MangaListFilter = MangaListFilter()
    }

    internal class Builder {
        private var query: String? = null
        private val tags: MutableSet<MangaTag> = mutableSetOf()
        private var locale: Locale? = null
        private val states: MutableSet<State> = mutableSetOf()
        private val types: MutableSet<ContentType> = mutableSetOf()

        fun query(query: String?): Builder = apply { this.query = query }

        fun addTag(tag: MangaTag): Builder = apply { tags.add(tag) }
        fun addTags(tags: Collection<MangaTag>): Builder = apply { this.tags.addAll(tags) }

        fun locale(locale: Locale?): Builder = apply { this.locale = locale }

        fun addState(state: State): Builder = apply { states.add(state) }
        fun addStates(states: Collection<State>): Builder =
            apply { this.states.addAll(states) }

        fun addType(type: ContentType): Builder = apply { types.add(type) }
        fun addTypes(types: Collection<ContentType>): Builder = apply { this.types.addAll(types) }

        fun build(): MangaListFilter = MangaListFilter(
            query, tags, locale,
        )
    }
}
