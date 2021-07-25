package com.bo.helper.common.extention

import android.content.res.Resources
import android.util.TypedValue

private const val KILOBYTE = 1024L
private const val MEGABYTE = KILOBYTE * KILOBYTE

fun Number.toDp(): Int {
    return (this.toFloat() * Resources.getSystem().displayMetrics.density).toInt()
}

fun Number.toPx(): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics
).toInt()

fun Number.toMegabytes(): Float = this.toFloat() / MEGABYTE