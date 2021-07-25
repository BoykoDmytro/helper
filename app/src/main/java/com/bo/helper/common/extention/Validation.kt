package com.bo.helper.common.extention

import android.util.Patterns
import java.util.regex.Pattern

private const val MIN_PASSWORD_LENGTH = 8
private const val MAX_PASSWORD_LENGTH = 30
private const val PASSWORD_LENGTH_REGEX = ".{$MIN_PASSWORD_LENGTH,$MAX_PASSWORD_LENGTH}"
private const val CHARS_NUMBER = "([0-9]*|[A-Z]*|[a-z]*|[a-zA-Z0-9]*)\$"
private const val PASSWORD_VALIDATION = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{$MIN_PASSWORD_LENGTH,$MAX_PASSWORD_LENGTH}\$"

fun CharSequence?.isEmailValid(): Boolean =
    this?.let { Patterns.EMAIL_ADDRESS.matcher(it).matches() } ?: false

fun CharSequence?.isPasswordLengthValid(): Boolean =
    this?.let { Pattern.compile(PASSWORD_LENGTH_REGEX).matcher(it).matches() } ?: false

fun CharSequence?.hasDigit(): Boolean =
    this?.find { Character.isDigit(it) }?.let { true } ?: false

fun CharSequence?.containsUpperCase(): Boolean =
    this?.find { Character.isLetter(it) && Character.isUpperCase(it) }?.let { true } ?: false

fun CharSequence?.containsLowerCase(): Boolean =
    this?.find { Character.isLetter(it) && Character.isLowerCase(it) }?.let { true } ?: false

fun CharSequence?.isNoSpecialSymbols(): Boolean =
    this?.let { Pattern.compile(CHARS_NUMBER).matcher(it.toString()).matches() } ?: false

fun CharSequence?.isPasswordValid(): Boolean =
    this?.let { Pattern.compile(PASSWORD_VALIDATION).matcher(it.toString()).matches() } ?: false

