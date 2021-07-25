package com.bo.helper.presentation.base.listener

import com.bo.helper.presentation.base.adapter.viewholder.BaseViewHolder

interface OnCheckBoxSelectionListener<T> {
    fun selectionClick(vh: BaseViewHolder<T>, checked: Boolean)
}