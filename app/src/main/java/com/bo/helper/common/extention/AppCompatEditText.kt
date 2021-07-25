package com.bo.helper.common.extention

import androidx.appcompat.widget.AppCompatEditText

fun AppCompatEditText.placeCursorToEnd() {
    this.post {
        this.text?.let { this.setSelection(it.length) }
    }
}