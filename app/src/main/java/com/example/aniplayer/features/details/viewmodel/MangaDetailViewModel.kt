package com.example.aniplayer.features.details.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.aniplayer.model.manga.Manga
import com.example.aniplayer.site.manga.MangaSite
import com.example.aniplayer.utils.DEFAULT_ERROR_MESSAGE
import kotlinx.coroutines.launch

class MangaDetailViewModel(private val mangaSite: MangaSite) : BaseDetailViewModel<Manga>() {

    override fun loadDetail(item: Manga) {
        viewModelScope.launch {
            setLoading()
            try {
                val mangaDetail = mangaSite.getDetails(item)
                setSuccess(mangaDetail)
            } catch (e: Exception) {
                setError(e.message ?: DEFAULT_ERROR_MESSAGE)
            }
        }
    }

}
