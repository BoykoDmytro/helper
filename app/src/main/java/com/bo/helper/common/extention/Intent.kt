package com.bo.helper.common.extention

import android.content.Intent

fun Intent.createSendAction(content: CharSequence): Intent {
    this.apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, content)
        type = "text/plain"
    }
    return Intent.createChooser(this, null)
}
