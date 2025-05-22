package com.example.aniplayer.features.details.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.aniplayer.model.manga.Manga
import com.example.aniplayer.site.manga.MangaSite
import com.example.aniplayer.utils.DEFAULT_ERROR_MESSAGE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MangaDetailViewModel(private val mangaSite: MangaSite) : BaseDetailViewModel<Manga>() {

    override fun loadDetail(item: Manga) {
        viewModelScope.launch {
            setLoading() // Run on main thread
            try {
                val mangaDetail = withContext(Dispatchers.IO) {
                    mangaSite.getDetails(item) // Run on IO thread
                }
                setSuccess(mangaDetail) // Back on main thread
            } catch (e: Exception) {
                setError(e.message ?: DEFAULT_ERROR_MESSAGE)
            }
        }
    }
}
