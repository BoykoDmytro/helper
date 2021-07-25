package com.bo.helper.common.extention

fun <T : Any> List<T>.mapClass() = this.map { it::class.java }