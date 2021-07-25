package com.bo.helper.presentation.base.listener

interface BaseDialogListener {
    fun onDismiss() = Unit

    fun onApprove() = Unit

    fun onDecline() = Unit
}