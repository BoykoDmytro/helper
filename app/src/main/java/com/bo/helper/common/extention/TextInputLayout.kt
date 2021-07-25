package com.bo.helper.common.extention

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.hideError(
    inputView: TextInputEditText,
    @DrawableRes errorDrawable: Int? = null
) {
    this.error = null
    inputView.setEndDrawableRes(errorDrawable ?: 0)
}

fun TextInputLayout.displayError(
    inputView: TextInputEditText,
    @StringRes errorRes: Int,
    @DrawableRes errorDrawable: Int? = null,
) {
    this.error = this.context.getString(errorRes)
    inputView.setEndDrawableRes(errorDrawable ?: 0)
}