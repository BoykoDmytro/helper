package com.bo.helper.presentation.base.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bo.helper.common.extention.toDp

abstract class BaseSwipeController(val context: Context) : ItemTouchHelper.Callback() {

    companion object {
        private const val DEFAULT_VIEW_WIDTH = 100F
    }

    enum class ViewState {
        GONE, LEFT_VISIBLE, RIGHT_VISIBLE
    }

    private var swipeBack: Boolean = false
    private var isClicked: Boolean = false
    private var viewWidth: Float = 0f
    private var viewState: ViewState = ViewState.GONE
    private var currentViewHolder: RecyclerView.ViewHolder? = null
    private var tapPositionX = 0f

    override fun getMovementFlags(
        recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder
    ): Int = makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //Do nothing
    }

    protected abstract fun drawViews(
        canvas: Canvas,
        viewHolder: RecyclerView.ViewHolder,
        recyclerView: RecyclerView
    )

    protected open fun isViewRightExist(): Boolean = true

    protected open fun isViewLeftExist(): Boolean = true

    protected open fun onLeftClicked(x: Float, y: Float, position: Int) {
        //Do nothing
    }

    protected open fun onRightClicked(x: Float, y: Float, position: Int) {
        //Do nothing
    }

    protected open fun onCanceled(position: Int) {
        //Do nothing
    }

    protected fun drawText(
        text: String, textSize: Float, @ColorInt textColor: Int,
        canvas: Canvas, rectF: RectF, paint: Paint, top: Float
    ) {
        paint.color = textColor
        paint.isAntiAlias = true
        paint.textSize = textSize

        val rect = Rect()
        paint.getTextBounds(text, 0, text.length, rect)

        val textWidth = paint.measureText(text)
        val textX = rectF.centerX() - (textWidth / 2)
        canvas.drawText(text, textX, top, paint)
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (swipeBack) {
            swipeBack = viewState != ViewState.GONE
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (viewState != ViewState.GONE) {
                var x = 0F
                if (viewState == ViewState.LEFT_VISIBLE) x = dX.coerceAtLeast(getViewWidth())
                if (viewState == ViewState.RIGHT_VISIBLE) x = dX.coerceAtMost(-getViewWidth())
                super.onChildDraw(c, recyclerView, viewHolder, x, dY, actionState, isCurrentActive)
            } else {
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentActive)
            }
        }

        if (viewState == ViewState.GONE) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentActive)
        }

        currentViewHolder = viewHolder
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentActive: Boolean
    ) {
        recyclerView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                    swipeBack = true
                    viewState = when {
                        dX < -getViewWidth() && isViewRightExist() -> ViewState.RIGHT_VISIBLE
                        dX > getViewWidth() && isViewLeftExist() -> ViewState.LEFT_VISIBLE
                        else -> ViewState.GONE
                    }

                    if (viewState != ViewState.GONE) {
                        setTouchDownListener(
                            c,
                            recyclerView,
                            viewHolder,
                            dY,
                            actionState,
                            isCurrentActive
                        )
                        setItemsClickable(recyclerView, false)
                    } else {
                        currentViewHolder = null
                    }
                }
            }
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchDownListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dY: Float,
        actionState: Int,
        isCurrentActive: Boolean
    ) {
        recyclerView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> setTouchUpListener(
                    c,
                    recyclerView,
                    viewHolder,
                    dY,
                    actionState,
                    isCurrentActive
                )
            }
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchUpListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dY: Float,
        actionState: Int,
        isCurrentActive: Boolean
    ) {
        isClicked = true
        recyclerView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        0F,
                        dY,
                        actionState,
                        isCurrentActive
                    )
                    recyclerView.setOnTouchListener { _, _ -> false }
                    setItemsClickable(recyclerView, true)
                    swipeBack = false
                    val position = viewHolder.absoluteAdapterPosition
                    when (viewState) {
                        ViewState.LEFT_VISIBLE ->
                            takeIf { isClicked }?.let { onLeftClicked(event.x, event.y, position) }
                        ViewState.RIGHT_VISIBLE ->
                            takeIf { isClicked }?.let { onRightClicked(event.x, event.y, position) }
                        ViewState.GONE -> onCanceled(position)
                    }
                    viewState = ViewState.GONE
                    currentViewHolder = null
                    tapPositionX = 0f
                    isClicked = true
                }
                MotionEvent.ACTION_MOVE -> {
                    takeIf { tapPositionX == 0f }?.let { tapPositionX = event.x }
                    isClicked = event.x - tapPositionX <= (getViewWidth() / 10)
                }
            }
            false
        }
    }

    private fun setItemsClickable(recyclerView: RecyclerView, isClickable: Boolean) {
        for (i in 0 until recyclerView.childCount)
            recyclerView.getChildAt(i).isClickable = isClickable
    }

    open fun getViewWidth(offset: Int = 0): Float =
        if (viewWidth > 0) viewWidth + offset else (DEFAULT_VIEW_WIDTH.toDp() + offset).toFloat()

    fun setViewWidth(width: Int) {
        viewWidth = width.toFloat()
    }

    fun setViewWidth(width: Float) {
        viewWidth = width
    }

    fun onDraw(canvas: Canvas, recyclerView: RecyclerView) {
        currentViewHolder?.let {
            drawViews(canvas, it, recyclerView)
        }
    }

    protected fun getBitmapForIcon(context: Context, @DrawableRes resId: Int): Bitmap {
        val drawable = AppCompatResources.getDrawable(context, resId) as Drawable
        return drawable.toBitmap()
    }
}