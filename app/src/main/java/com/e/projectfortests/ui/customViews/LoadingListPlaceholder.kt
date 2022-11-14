package com.e.projectfortests.ui.customViews

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import com.e.projectfortests.R


class LoadingListPlaceholder @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0
) : View(context, attrs, defStyleAttrs) {
    private var placeholderColorPrimary: Int = DEFAULT_PRIMARY_COLOR
    private var placeholderColorSecondary: Int = DEFAULT_SECONDARY_COLOR
    private val backgroundPaint = Paint()
    private val foregroundPaint = Paint()
    private val backgroundRect = Rect()
    private val leftRect = Rect()
    private val nameRect = Rect()
    private val jobRect = Rect()
    private lateinit var foregroundColorAnimator: ObjectAnimator

    init {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.LoadingListPlaceholder)
            placeholderColorPrimary = ta.getColor(R.styleable.LoadingListPlaceholder_primary_color, DEFAULT_PRIMARY_COLOR)
            placeholderColorSecondary = ta.getColor(R.styleable.LoadingListPlaceholder_secondary_color, DEFAULT_SECONDARY_COLOR)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val initWidth = resolveDefaultWidth(widthMeasureSpec)
        setMeasuredDimension(initWidth, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setup()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawRect(backgroundRect, backgroundPaint)
        canvas?.drawRect(leftRect, foregroundPaint)
        canvas?.drawRect(nameRect, foregroundPaint)
        canvas?.drawRect(jobRect, foregroundPaint)
        invalidate()
    }

    /**
     * Функция определения ширины view, если она явно не задана или не определена родительским контейнером
     * */
    private fun resolveDefaultWidth(spec: Int): Int {
        return when(MeasureSpec.getMode(spec)) {
            MeasureSpec.UNSPECIFIED -> { context.dpToPx(DEFAULT_WIDTH).toInt() } // Значение по умолчанию
            MeasureSpec.AT_MOST -> { MeasureSpec.getSize(spec) }
            MeasureSpec.EXACTLY -> { MeasureSpec.getSize(spec) }
            else -> MeasureSpec.getSize(spec)
        }
    }
    private fun setup() {
        Log.d("LoadingListPlaceholder", "setup: ")
        backgroundPaint.color = placeholderColorSecondary
        foregroundPaint.color = placeholderColorPrimary

        ValueAnimator.ofInt(foregroundPaint.color, Color.TRANSPARENT).apply {
            duration = 1000
            repeatCount = ValueAnimator.INFINITE
            setEvaluator(ArgbEvaluator())
            repeatMode = ValueAnimator.REVERSE
            addUpdateListener { valueAnimator ->
                foregroundPaint.color = valueAnimator.animatedValue as Int
                invalidate()
            }
            start()
        }
        /*foregroundColorAnimator = ObjectAnimator.ofInt(foregroundPaint, "color", placeholderColorPrimary, Color.TRANSPARENT);
        foregroundColorAnimator.duration = 3000;
        foregroundColorAnimator.setEvaluator(ArgbEvaluator());
        foregroundColorAnimator.repeatCount = ValueAnimator.INFINITE;
        foregroundColorAnimator.repeatMode = ValueAnimator.RESTART;
        foregroundColorAnimator.start()*/


        backgroundRect.set(0, 0, width, height)

        var p1 = context.dpToPx(30).toInt()
        var p2 = context.dpToPx(30).toInt()
        var p3 = p1 + context.dpToPx(18).toInt()
        var p4 = p2 + context.dpToPx(18).toInt()
        leftRect.set(p1, p2, p3, p4)

        p1 += context.dpToPx(44).toInt()
        p2 = context.dpToPx(20).toInt()
        p3 = p1 + width / 2
        p4 = p2 + context.dpToPx(16).toInt()
        nameRect.set(p1, p2, p3, p4)

        p2 = p4 + context.dpToPx(2).toInt()
        p3 = p1 + width - ( context.dpToPx(20).toInt() + p1 )
        p4 = p2 + context.dpToPx(16).toInt()
        jobRect.set(p1, p2, p3, p4)
    }

    companion object {
        private const val DEFAULT_WIDTH = 300
        private const val DEFAULT_PRIMARY_COLOR = Color.DKGRAY
        private const val DEFAULT_SECONDARY_COLOR = Color.LTGRAY
    }
}

