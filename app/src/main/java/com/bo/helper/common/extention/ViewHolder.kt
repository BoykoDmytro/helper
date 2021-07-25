package com.bo.helper.common.extention

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.ViewHolder.getString(@StringRes resId: Int): String =
    itemView.context.getString(resId)

@ColorInt
fun RecyclerView.ViewHolder.getColor(@ColorRes resId: Int):  Int =
    ContextCompat.getColor(itemView.context, resId)
