package com.bo.helper.common.extention

import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

fun ViewPager2.horizontalAlphaTransformer() {
    this.setPageTransformer { page, position ->
        page.apply {
            translationX = width * -position
            alpha = when {
                position <= -1.0F || position >= 1.0F -> 0.0F
                position == 0.0F -> 1.0F
                else -> 1.0F - abs(position)
            }
        }
    }
}

fun ViewPager2.verticalAlphaTransformer() {
    this.setPageTransformer { page, position ->
        page.apply {
            translationY = height * -position
            alpha = when {
                position <= -1.0F || position >= 1.0F -> 0.0F
                position == 0.0F -> 1.0F
                else -> 1.0F - abs(position)
            }
        }
    }
}