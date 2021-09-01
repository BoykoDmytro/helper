package com.bo.helper.presentation.base.viewmodel

import com.bo.helper.common.SingleLiveData
import com.bo.helper.data.ErrorResult
import com.bo.helper.data.SuccessResult
import com.bo.helper.presentation.base.listener.PaginationListener
import com.bo.helper.domain.usecase.base.BasePageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BasePageViewModel<T>() : BaseViewModel(), PaginationListener.OnLoadMoreListener {

    protected var currentJob: Job? = null
    private var hasMoreItems = true
    private var total: Long = 0

    val refreshItemEvent = SingleLiveData<MutableList<T>>()
    val loadMoreItemEvent = SingleLiveData<MutableList<T>>()

    override fun hasMoreItems(): Boolean = hasMoreItems

    override fun isLoading(): Boolean = currentJob?.isActive ?: false

    protected fun setupItems(data: List<T>) {
        if (data.isNullOrEmpty()) emptyDataEvent.call()
        else {
            checkAndSetEntries(data, data.size.toLong())
            refreshItemEvent.value = data.toMutableList()
        }
    }

    private fun checkAndSetEntries(entities: List<T>, total: Long?) {
        total ?: return
        this.total += entities.size
        hasMoreItems = this.total < total
    }

    protected fun setupMoreItems(data: List<T>) {
        if (data.isNullOrEmpty()) return
        checkAndSetEntries(data, data.size.toLong())
        loadMoreItemEvent.value = data.toMutableList()
    }

    abstract fun getPageUseCase(): BasePageUseCase<List<T>>

    open fun firstLoad() {
        total = 0
        currentJob?.cancel()
        val pageUseCase = getPageUseCase()
        pageUseCase.resetPage()
        currentJob = launch {
            when (val response = executePending { pageUseCase.execute() }) {
                is SuccessResult -> setupItems(response.body)
                is ErrorResult -> showError(response)
            }
        }
    }

    override fun loadMoreItems() {
        val pageUseCase = getPageUseCase()
        currentJob = launch {
            pageUseCase.increasePage()
            when (val response = withContext(Dispatchers.IO) { pageUseCase.execute() }) {
                is SuccessResult -> setupMoreItems(response.body)
                is ErrorResult -> showError(response)
            }
        }
    }
}