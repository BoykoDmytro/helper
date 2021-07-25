package com.bo.helper.common.extention

import android.graphics.Typeface
import android.text.method.LinkMovementMethod
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.view.marginStart
import androidx.core.widget.doAfterTextChanged

fun TextView.navigatableTextOf(string: String, block: (View) -> Unit) {
    movementMethod = LinkMovementMethod.getInstance()
    text = string.bold().clickable { block(it) }
}

fun TextView.navigatableTextOf(@StringRes stringId: Int, block: (View) -> Unit) {
    navigatableTextOf(context.getString(stringId), block)
}

fun TextView.setEndDrawableRes(@DrawableRes res: Int) = this.setCompoundDrawablesWithIntrinsicBounds(0, 0, res, 0)

fun TextView.clearCompoundDrawables() = this.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

fun TextView.clickableLabel(
    @ColorInt
    colorLabel: Int,
    template: String,
    withUnderLine: Boolean = true,
    @ColorRes
    highlightColorRes: Int = android.R.color.transparent,
    typeface: Typeface? = null,
    vararg pairs: Pair<String, (() -> Unit)>
) {
    val spannableStrings = pairs.map { pair ->
        val (value, action) = pair
        value.toClickableSpan(action, withUnderLine, colorLabel, typeface)
    }.toTypedArray()
    highlightColor = context.getCompatColor(highlightColorRes)
    text = template.expandTemplate(*spannableStrings)
    movementMethod = LinkMovementMethod.getInstance()
}

fun <V : TextView> V.cursorBug() {
    val margin = marginStart + marginStart
    maxWidth = resources.displayMetrics.widthPixels - margin
    doAfterTextChanged {
        this.gravity = when {
            it.isNullOrBlank() -> Gravity.START
            else -> Gravity.CENTER
        }
    }
}