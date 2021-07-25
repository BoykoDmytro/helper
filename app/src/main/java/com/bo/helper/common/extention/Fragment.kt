package com.bo.helper.common.extention

import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.hideKeyboard() {
    val inputMethodManager = getSystemService(FragmentActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

fun Fragment.hideKeyboard() = requireActivity().hideKeyboard()

fun Fragment.getCompatColor(@ColorRes colorRes: Int): Int = requireContext().getCompatColor(colorRes)

inline fun <reified T> Fragment.getFromBundle(key: String): T = requireArguments()[key] as T

fun Fragment.getDimens(@DimenRes id: Int): Int = resources.getDimensionPixelSize(id)