package com.bo.helper.common.extention

import android.annotation.SuppressLint
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.widget.EditText

private const val DRAWABLE_END = 2

fun EditText.takeTextOrNull(): String? = this.text.takeIf { it.isNullOrEmpty().not() }?.toString()

@SuppressLint("ClickableViewAccessibility")
fun EditText.togglePassword() {
    this.setOnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_UP) {
            if (event.rawX >= this.right - this.compoundDrawables[DRAWABLE_END].bounds.width()) {
                if (this.transformationMethod is HideReturnsTransformationMethod) {
                    this.transformationMethod = PasswordTransformationMethod.getInstance()
                } else {
                    this.transformationMethod = HideReturnsTransformationMethod.getInstance()
                }
                return@setOnTouchListener true
            }
        }
        false
    }
}