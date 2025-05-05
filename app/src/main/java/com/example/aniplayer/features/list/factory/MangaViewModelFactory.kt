package com.example.aniplayer.features.list.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aniplayer.features.list.viewmodel.MangaViewModel
import com.example.aniplayer.site.manga.MangaSite
import com.example.aniplayer.utils.UNKNOWN_VIEWMODEL

class MangaViewModelFactory(
    private val mangaSite: MangaSite
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MangaViewModel::class.java)) {
            return MangaViewModel(mangaSite) as T
        }
        throw IllegalArgumentException(UNKNOWN_VIEWMODEL)
    }
}
