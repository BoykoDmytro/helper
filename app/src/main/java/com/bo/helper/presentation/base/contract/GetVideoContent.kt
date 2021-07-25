package com.bo.helper.presentation.base.contract

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import com.bo.helper.common.type.ContentType

class GetVideoContent : ActivityResultContract<Array<String>, Uri?>() {

    override fun createIntent(context: Context, allowMime: Array<String>): Intent {
        return Intent(Intent.ACTION_GET_CONTENT).apply {
            type = ContentType.VIDEO.type
            addCategory(Intent.CATEGORY_OPENABLE)
            putExtra(Intent.EXTRA_MIME_TYPES, allowMime)
            putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? =
        if (resultCode == RESULT_OK) intent?.data else null
}