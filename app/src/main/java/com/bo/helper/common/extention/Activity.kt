package com.bo.helper.common.extention

import androidx.fragment.app.FragmentActivity

inline fun <reified T> FragmentActivity.getFromBundle(bundleKey: String, key: String): T =
    intent?.getBundleExtra(bundleKey)?.get(key) as T

inline fun <reified T> FragmentActivity.getFromBundleOrNull(bundleKey: String, key: String): T? =
    intent?.getBundleExtra(bundleKey)?.get(key) as T?