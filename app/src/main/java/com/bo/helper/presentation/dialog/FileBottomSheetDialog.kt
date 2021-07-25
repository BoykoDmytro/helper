package com.bo.helper.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import com.bo.helper.R
import com.bo.helper.databinding.DialogFileBinding
import com.bo.helper.presentation.base.dialog.BaseBottomDialogFragment
import com.bo.helper.presentation.base.listener.OnChooseFileListener

class FileBottomSheetDialog(
    val listener: OnChooseFileListener? = null
) : BaseBottomDialogFragment<DialogFileBinding>() {

    companion object {

        private const val KEY_IS_VIDEO = "type.isVideo"

        private fun newInstance(
            listener: OnChooseFileListener? = null,
            isVideo: Boolean = true
        ): FileBottomSheetDialog {
            val fragment = FileBottomSheetDialog(listener)
            fragment.arguments = bundleOf(KEY_IS_VIDEO to isVideo)
            return fragment
        }


        fun show(
            fragmentManager: FragmentManager,
            isVideo: Boolean = true,
            listener: OnChooseFileListener? = null
        ) {
            val dialog = newInstance(listener, isVideo)
            dialog.show(fragmentManager, dialog::class.java.simpleName)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isVideo = arguments?.getBoolean(KEY_IS_VIDEO, false) ?: false
        viewBinding.dialogGalleryFeatureTv.text = getString(when (isVideo) {
            true -> R.string.video
            else -> R.string.gallery
        })
        initListeners()
    }

    private fun initListeners() {
        with(viewBinding) {
            view?.setOnClickListener { dismiss() }
            dialogFileCameraBtn.setOnClickListener {
                listener?.openCamera()
                dismiss()
            }
            dialogFileGalleryBtn.setOnClickListener {
                listener?.chooseVideo()
                dismiss()
            }
        }
    }

    override fun bindViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogFileBinding = DialogFileBinding.inflate(inflater, container, false)
}