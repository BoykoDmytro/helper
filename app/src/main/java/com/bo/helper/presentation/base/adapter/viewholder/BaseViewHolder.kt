package com.bo.helper.presentation.base.adapter.viewholder

import android.widget.Checkable
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bo.helper.presentation.base.listener.OnItemClickListener

abstract class BaseViewHolder<T>(
    viewBinding: ViewBinding,
    open val listener: OnItemClickListener<T>? = null
) : RecyclerView.ViewHolder(viewBinding.root), Checkable {

    protected var isCheckedItem = false

    abstract fun bind(data: T, isSelected: Boolean, action: () -> Unit)

    open fun getCheckableItem(): Checkable = this

    override fun isChecked(): Boolean = isCheckedItem

    override fun setChecked(checked: Boolean) {
        isCheckedItem = checked
    }

    override fun toggle() {
        isCheckedItem = !isCheckedItem
    }
}