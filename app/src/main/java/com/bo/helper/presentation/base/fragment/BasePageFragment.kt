package com.bo.helper.presentation.base.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bo.helper.common.extention.observe
import com.bo.helper.presentation.base.adapter.BaseAdapter
import com.bo.helper.presentation.base.viewmodel.BasePageViewModel
import com.bo.helper.presentation.base.viewmodel.BaseViewModel

abstract class BasePageFragment<T, VB : ViewBinding> : BaseFragment<VB>() {

    abstract fun getBasePageViewModel(): BasePageViewModel<T>

    abstract val recyclerView: RecyclerView?
    abstract val emptyView: View?
    open var adapter: BaseAdapter<T, *>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(getBasePageViewModel()) {
            observe(loadMoreItemEvent, ::handleLoadMoreItems)
            observe(refreshItemEvent, ::handleRefreshItems)
            observe(emptyDataEvent) { showEmptyScreen() }
        }
    }

    override fun getBaseViewModel(): BaseViewModel = getBasePageViewModel()

    open fun handleLoadMoreItems(items: MutableList<T>) {
        adapter?.addItems(items)
    }

    open fun handleRefreshItems(items: MutableList<T>) {
        adapter?.setItems(items)
        hideEmptyScreen()
    }

    open fun showEmptyScreen() {
        recyclerView?.isVisible = false
        emptyView?.isVisible = true
    }

    open fun hideEmptyScreen() {
        recyclerView?.isVisible = true
        emptyView?.isVisible = false
    }

    fun checkItemsList() {
        val count = adapter?.itemCount
        count?.let { if (it == 0) showEmptyScreen() else hideEmptyScreen() } ?: showEmptyScreen()
    }
}