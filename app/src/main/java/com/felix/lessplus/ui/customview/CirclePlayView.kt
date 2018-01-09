package com.felix.lessplus.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.ImageView
import com.felix.lessplus.R
import org.jetbrains.anko.dimen


/**
 * Created by liuhaiyang on 2018/1/8.
 */
class CirclePlayView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ImageView(context, attrs, defStyleAttr) {

    private var mSize: Float = 0f
    private var mColor: Int = 0

    private var mPaint: Paint? = null

    init {
        val array = context.theme.obtainStyledAttributes(attrs, R.styleable.CirclePlayView, defStyleAttr, 0)
        mColor = array.getColor(R.styleable.CirclePlayView_playColor, ContextCompat.getColor(context, R.color.colorPrimary))
        mSize = array.getDimension(R.styleable.CirclePlayView_playSize, dimen(R.dimen.bottom_play_bar_height).toFloat())
        array.recycle()

        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint!!.style = Paint.Style.FILL

    }

    fun setColor(color: Int) {
        mColor = color
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPaint!!.color = mColor
        canvas.drawCircle((width / 2).toFloat(), (width / 2).toFloat(), mSize, mPaint)
    }
}