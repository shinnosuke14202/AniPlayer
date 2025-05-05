package com.example.aniplayer.features.details.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aniplayer.features.details.viewmodel.MangaDetailViewModel
import com.example.aniplayer.site.manga.MangaSite
import com.example.aniplayer.utils.UNKNOWN_VIEWMODEL

class MangaDetailViewModelFactory(
    private val mangaSite: MangaSite
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MangaDetailViewModel::class.java)) {
            return MangaDetailViewModel(mangaSite) as T
        }
        throw IllegalArgumentException(UNKNOWN_VIEWMODEL)
    }
}
