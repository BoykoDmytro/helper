package com.bo.helper.presentation.base.adapter.viewholder

import android.view.View
import androidx.viewbinding.ViewBinding

abstract class BaseVpViewHolder<In>(protected val item: In?, viewBinding: ViewBinding) {

    val itemView: View = viewBinding.root

    abstract fun onBindView(item: In?)

    fun setPositionInTag(tag: Int) {
        itemView.tag = tag
    }
}