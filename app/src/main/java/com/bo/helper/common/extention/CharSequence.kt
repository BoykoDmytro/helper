package com.bo.helper.common.extention

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.text.*
import android.text.style.*
import android.view.View
import androidx.annotation.*
import androidx.core.content.res.ResourcesCompat
import com.bo.helper.BuildConfig

private const val FILES = "api/files/"

fun Context.getStringSpanned(
    @StringRes resId: Int,
    vararg formatArgs: Any
): SpannableStringBuilder {
    var lastArgIndex = 0
    val spannableStringBuilder = SpannableStringBuilder(getString(resId, *formatArgs))
    for (arg in formatArgs) {
        val argString = arg.toString()
        lastArgIndex = spannableStringBuilder.indexOf(argString, lastArgIndex)
        if (lastArgIndex != -1) {
            (arg as? CharSequence)?.let {
                spannableStringBuilder.replace(lastArgIndex, lastArgIndex + argString.length, it)
            }
            lastArgIndex += argString.length
        }
    }
    return spannableStringBuilder
}

fun CharSequence.underline(): CharSequence {
    val content = SpannableString(this)
    content.setSpan(UnderlineSpan(), 0, length, 0)
    return content
}

fun CharSequence.font(@FontRes font: Int, context: Context): CharSequence {
    val typeface = ResourcesCompat.getFont(context, font) ?: return this
    val content = SpannableString(this)
    content.setSpan(StyleSpan(typeface.style), 0, length, 0)
    return content
}

fun CharSequence.clickable(color: Int? = null, block: (View) -> Unit): CharSequence {
    val content = SpannableString(this)
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) = block(widget)

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            color?.let { ds.color = it }
        }
    }
    content.setSpan(clickableSpan, 0, length, 0)
    return content
}

fun CharSequence.highlight(color: Int) = apply {
    val content = SpannableString(this)
    content.setSpan(ForegroundColorSpan(color), 0, length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
    return content
}

fun CharSequence.bold() = apply {
    val content = SpannableString(this)
    content.setSpan(StyleSpan(Typeface.BOLD), 0, length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
    return content
}

fun CharSequence.textSize(size: Int) = apply {
    val content = SpannableString(this)
    content.setSpan(AbsoluteSizeSpan(size, true), 0, length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
    return content
}

fun CharSequence.expandTemplate(vararg spannableString: CharSequence): CharSequence =
    TextUtils.expandTemplate(this, *spannableString)

@RequiresApi(Build.VERSION_CODES.KITKAT)
fun CharSequence.toClickableSpan(
    action: () -> Unit,
    withUnderLine: Boolean,
    @ColorInt
    colorLabel: Int,
    targetTypeface: Typeface?
): SpannableString {
    val clickableSpan = object : ClickableSpan() {

        override fun onClick(widget: View) {
            widget.cancelPendingInputEvents()
            action.invoke()
        }

        override fun updateDrawState(paint: TextPaint) {
            super.updateDrawState(paint)
            paint.apply {
                isUnderlineText = withUnderLine
                color = colorLabel
                targetTypeface?.let { typeface = it }
            }
        }
    }
    return SpannableString(this).apply {
        setSpan(clickableSpan, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}

fun CharSequence.styleableText(
    context: Context,
    @ColorRes colorRes: Int? = null,
    @DimenRes dimenRes: Int? = null,
    @FontRes fontRes: Int? = null
): SpannableString {
    return SpannableString(this).apply {
        dimenRes?.let { res ->
            val targetTextSize = context.resources.getDimensionPixelSize(res)
            setSpan(AbsoluteSizeSpan(targetTextSize), 0, this.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        }
        fontRes?.let { res ->
            val typeface = ResourcesCompat.getFont(context, res)
            val typefaceSpan = object : MetricAffectingSpan() {
                override fun updateDrawState(paint: TextPaint) {
                    paint.typeface = typeface
                }

                override fun updateMeasureState(paint: TextPaint) {
                    paint.typeface = typeface
                }
            }
            setSpan(typefaceSpan, 0, this.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        }
        colorRes?.let { res ->
            setSpan(ForegroundColorSpan(context.getCompatColor(res)), 0, length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
        }
    }
}

fun CharSequence.createImagePath(): String = "${BuildConfig.BASE_URL}$FILES$this"