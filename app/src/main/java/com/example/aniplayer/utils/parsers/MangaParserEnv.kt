package com.example.aniplayer.utils.parsers

import com.example.aniplayer.exception.ParseException
import com.example.aniplayer.model.manga.MangaTag
import com.example.aniplayer.model.parsers.ContentType
import com.example.aniplayer.model.parsers.State
import com.example.aniplayer.utils.messages.ErrorMessages
import org.jsoup.nodes.Element

fun Element.parseFailed(message: String? = null): Nothing {
    throw ParseException(message, ownerDocument()?.location() ?: baseUri(), null)
}

fun Set<MangaTag>?.oneOrThrowIfMany(): MangaTag? = oneOrThrowIfMany(
    ErrorMessages.FILTER_MULTIPLE_GENRES_NOT_SUPPORTED,
)

fun Set<State>?.oneOrThrowIfMany(): State? = oneOrThrowIfMany(
    ErrorMessages.FILTER_MULTIPLE_STATES_NOT_SUPPORTED,
)

fun Set<ContentType>?.oneOrThrowIfMany(): ContentType? = oneOrThrowIfMany(
    ErrorMessages.FILTER_MULTIPLE_CONTENT_TYPES_NOT_SUPPORTED,
)

private fun <T> Set<T>?.oneOrThrowIfMany(msg: String): T? = when {
    isNullOrEmpty() -> null
    size == 1 -> first()
    else -> throw IllegalArgumentException(msg)
}
