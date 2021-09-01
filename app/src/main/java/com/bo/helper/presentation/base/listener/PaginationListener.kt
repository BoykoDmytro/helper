package com.bo.helper.presentation.base.listener

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PaginationListener(private val onLoadMoreListener: OnLoadMoreListener) :
    RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val layoutManager = recyclerView.layoutManager as? LinearLayoutManager

        layoutManager?.let {
            val visible = it.childCount
            val total = it.itemCount
            val firstVisible = it.findFirstVisibleItemPosition()

            if (!onLoadMoreListener.isLoading() && onLoadMoreListener.hasMoreItems() && firstVisible > 0 && (visible + firstVisible) >= total)
                onLoadMoreListener.loadMoreItems()
        }
    }

    interface OnLoadMoreListener {
        fun loadMoreItems()
        fun isLoading(): Boolean
        fun hasMoreItems(): Boolean
    }
}