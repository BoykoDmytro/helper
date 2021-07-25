package com.bo.helper.common.extention

import java.util.*

fun String.toFirstCapital() : String =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }