package com.bo.helper.presentation.base.fragment

import android.Manifest
import androidx.viewbinding.ViewBinding
import com.bo.helper.common.type.ContentType
import com.bo.helper.presentation.base.listener.OnChooseFileListener
import com.bo.helper.presentation.dialog.FileBottomSheetDialog

abstract class BaseMediaFragment<VB : ViewBinding> : BaseChooseFileFragment<VB>() {

    private val photoListener: OnChooseFileListener = object : OnChooseFileListener {
        override fun openCamera() = openPhotoCamera()

        override fun chooseVideo() = openImageExplorer()
    }

    private val videoListener: OnChooseFileListener = object : OnChooseFileListener {
        override fun openCamera() = openVideoCamera()

        override fun chooseVideo() = openVideoExplorer()
    }

    protected fun showFileDialog(chooserType: ContentType) {
        verifyPermission(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            accepted = {
                val listener = when (chooserType) {
                    ContentType.VIDEO ->videoListener
                    ContentType.IMAGE ->  photoListener
                }
                FileBottomSheetDialog.show(childFragmentManager, false, listener)
            }
        )
    }
}