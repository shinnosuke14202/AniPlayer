package com.example.aniplayer.features.reader.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aniplayer.features.list.viewmodel.MangaViewModel
import com.example.aniplayer.model.manga.MangaChapter
import com.example.aniplayer.site.manga.MangaSite
import com.example.aniplayer.utils.UNKNOWN_VIEWMODEL

class ReaderViewModelFactory(
    private val mangaSite: MangaSite,
    private val chapters: List<MangaChapter>,
    private val position: Int,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReaderViewModel::class.java)) {
            return ReaderViewModel(mangaSite, chapters, position) as T
        }
        throw IllegalArgumentException(UNKNOWN_VIEWMODEL)
    }
}
