package com.example.aniplayer.utils.parsers

import com.example.aniplayer.model.manga.MangaChapter
import com.example.aniplayer.model.manga.MangaListFilter
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun MangaListFilter?.isNullOrEmpty(): Boolean {
    contract {
        returns(false) implies (this@isNullOrEmpty != null)
    }
    return this == null || this.isEmpty()
}

fun Collection<MangaChapter>.findById(chapterId: Long): MangaChapter? = find { x ->
    x.id == chapterId
}
