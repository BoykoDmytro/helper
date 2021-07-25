package com.bo.helper.presentation.base.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.bo.helper.databinding.DialogProgressBinding

class ProgressDialog : BaseDialogFragment<DialogProgressBinding>() {

    companion object {

        private fun newInstance(): ProgressDialog {
            return ProgressDialog()
        }

        fun show(fragmentManager: FragmentManager): ProgressDialog {
            val dialog = newInstance()
            dialog.isCancelable = false
            dialog.show(fragmentManager, dialog::class.java.simpleName)
            return dialog
        }
    }

    override fun bindViewBinding(inflater: LayoutInflater, container: ViewGroup?): DialogProgressBinding =
        DialogProgressBinding.inflate(inflater, container, false)
}