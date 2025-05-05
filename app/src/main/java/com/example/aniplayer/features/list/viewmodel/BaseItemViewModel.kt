package com.example.aniplayer.features.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.aniplayer.model.parsers.SortOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest

abstract class BaseItemViewModel<T : Any, F> : ViewModel() {

    private val _sortOrder = MutableStateFlow(SortOrder.POPULARITY)
    val sortOrder: StateFlow<SortOrder> = _sortOrder

    private val _filter = MutableStateFlow<F?>(null)
    val filter: StateFlow<F?> = _filter

    abstract fun getPager(order: SortOrder, filter: F): Pager<Int, T>

    fun updateSortOrder(newOrder: SortOrder) {
        _sortOrder.value = newOrder
    }

    fun updateFilter(newFilter: F) {
        _filter.value = newFilter
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingDataFlow: Flow<PagingData<T>> = combine(sortOrder, filter) { order, filter ->
        order to filter
    }.flatMapLatest { (order, filter) ->
        getPager(order, filter!!).flow
    }.cachedIn(viewModelScope)
}
