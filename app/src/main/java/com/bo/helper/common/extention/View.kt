package com.bo.helper.common.extention

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Rect
import android.os.Build
import android.view.View
import androidx.annotation.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

fun View.animationAlpha(startAlpha: Float, endAlpha: Float): ValueAnimator {
    return ObjectAnimator.ofFloat(startAlpha, endAlpha).apply {
        addUpdateListener {
            this@animationAlpha.alpha = it.animatedValue as Float
        }
    }
}

fun View.changeVerticalBias(startPosition: Float, endPosition: Float): ValueAnimator {
    val params = this.layoutParams as? ConstraintLayout.LayoutParams
        ?: throw IllegalArgumentException("ConstraintLayout should be parent")
    return ObjectAnimator.ofFloat(startPosition, endPosition).apply {
        addUpdateListener {
            params.verticalBias = it.animatedValue as Float
            this@changeVerticalBias.layoutParams = params
        }
    }
}

fun View.changeHorizontalBias(startPosition: Float, endPosition: Float): ValueAnimator {
    val params = this.layoutParams as? ConstraintLayout.LayoutParams
        ?: throw IllegalArgumentException("ConstraintLayout should be parent")
    return ObjectAnimator.ofFloat(startPosition, endPosition).apply {
        addUpdateListener {
            params.horizontalBias = it.animatedValue as Float
            this@changeHorizontalBias.layoutParams = params
        }
    }
}

fun View.translationX(startPosition: Float, endPosition: Float): ValueAnimator {
    return ObjectAnimator.ofFloat(startPosition, endPosition).apply {
        addUpdateListener {
            this@translationX.translationX = it.animatedValue as Float
        }
    }
}

fun View.translationY(startPosition: Float, endPosition: Float): ValueAnimator {
    return ObjectAnimator.ofFloat(startPosition, endPosition).apply {
        addUpdateListener {
            this@translationY.translationY = it.animatedValue as Float
        }
    }
}

@RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
fun View.doOnApplyWindowInsets(block: (View, WindowInsetsCompat, Rect) -> WindowInsetsCompat) {
    val initialPadding = recordInitialPaddingForView()
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        block(v, insets, initialPadding)
    }
    requestApplyInsetsWhenAttached()
}

fun View.recordInitialPaddingForView() = Rect(paddingLeft, paddingTop, paddingRight, paddingBottom)

@RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        requestApplyInsets()
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {

            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

fun View.getDimens(@DimenRes id: Int): Int = resources.getDimensionPixelSize(id)