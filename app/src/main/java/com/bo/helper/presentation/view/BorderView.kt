package com.bo.helper.presentation.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.bo.helper.R
import com.bo.helper.common.extention.toDp

class BorderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val LINE_LENGTH = 48
        private const val STROKE_SIZE = 6
        private const val ALPHA_CHANNEL = 96
        private const val WHITE = 255
    }

    var rect: Rect? = null
    private val lineSize = LINE_LENGTH.toDp().toFloat()
    private val strokeSize = STROKE_SIZE.toDp().toFloat()

    private val erasePaint: Paint = Paint().apply {
        color = Color.TRANSPARENT
        isAntiAlias = true
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        style = Paint.Style.FILL
    }
    private val linePaint: Paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.teal_700)
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = strokeSize
    }

    init {
        setLayerType(LAYER_TYPE_HARDWARE, null)
    }

    fun shouldDelayChildPressedState(): Boolean {
        return false
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        rect?.let { r ->
            canvas?.apply {
                drawARGB(ALPHA_CHANNEL, WHITE, WHITE, 255)
                drawRect(r, erasePaint)
                val strokeOffset = strokeSize / 2f
                val top = r.top.toFloat()
                val topO = r.top.toFloat() + strokeOffset
                val left = r.left.toFloat()
                val leftO = r.left.toFloat() + strokeOffset
                val bottom = r.bottom.toFloat()
                val bottomO = r.bottom.toFloat() - strokeOffset
                val right = r.right.toFloat()
                val rightO = r.right.toFloat() - strokeOffset
                drawLine(left, topO, left + lineSize, topO, linePaint)
                drawLine(leftO, top, leftO, top + lineSize, linePaint)

                drawLine(left, bottomO, left + lineSize, bottomO, linePaint)
                drawLine(leftO, bottom, leftO, bottom - lineSize, linePaint)

                drawLine(right, topO, right - lineSize, topO, linePaint)
                drawLine(rightO, top, rightO, top + lineSize, linePaint)

                drawLine(right, bottomO, right - lineSize, bottomO, linePaint)
                drawLine(rightO, bottom, rightO, bottom - lineSize, linePaint)
            }
        } ?: return
    }
}