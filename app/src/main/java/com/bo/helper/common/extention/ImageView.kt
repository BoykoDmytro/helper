package com.bo.helper.common.extention

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

private const val CORNER_RADIUS_SIZE = 16
val cornerOptions = RequestOptions().transform(CenterCrop(), RoundedCorners(CORNER_RADIUS_SIZE))
val cacheOptions = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)

fun ImageView.loadWithGlide(
    url: String? = null,
    @DrawableRes placeHolder: Int = 0,
    requestOptions: RequestOptions? = null
) {
    url?.createImagePath()?.also { link ->
        val options = requestOptions ?: RequestOptions()
        options.also { it.placeholder(placeHolder) }
        Glide.with(this)
            .load(link)
            .centerCrop()
            .apply(cacheOptions)
            .apply(options)
            .into(this)
    } ?: setImageResource(placeHolder)
}

fun ImageView.loadWithGlideCorners(url: String? = null, @DrawableRes holder: Int = 0) =
    loadWithGlide(url, holder, cornerOptions)

fun ImageView.loadWithGlideFromDevice(url: String? = null) {
    Glide.with(this)
        .load(url)
        .centerCrop()
        .apply(cacheOptions)
        .apply(cornerOptions)
        .into(this)
}