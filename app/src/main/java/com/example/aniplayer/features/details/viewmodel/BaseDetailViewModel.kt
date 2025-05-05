package com.example.aniplayer.features.details.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aniplayer.utils.state.UiState

abstract class BaseDetailViewModel<T> : ViewModel() {

    private val _uiState = MutableLiveData<UiState<T>>(UiState.Loading)
    val uiState get() = _uiState

    abstract fun loadDetail(item: T)

    fun setLoading() {
        _uiState.value = UiState.Loading
    }

    fun setSuccess(data: T) {
        _uiState.value = UiState.Success(data)
    }

    fun setError(message: String) {
        _uiState.value = UiState.Error(message)
    }

}
