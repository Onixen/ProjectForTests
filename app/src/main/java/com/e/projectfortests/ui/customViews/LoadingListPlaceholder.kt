package com.e.projectfortests.ui.customViews

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.e.projectfortests.R
import kotlin.math.ceil
import kotlin.math.roundToInt


class LoadingListPlaceholder @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0
) : View(context, attrs, defStyleAttrs) {
    private var placeholderColorPrimary: Int = DEFAULT_PRIMARY_COLOR
    private var placeholderColorSecondary: Int = DEFAULT_SECONDARY_COLOR

    private val backgroundPaint = Paint()
    private val foregroundPaint = Paint()

    private var itemsCount: Int = 0

    private var backgroundRect: MutableList<Rect> = mutableListOf()
    private var leftRect: MutableList<Rect> = mutableListOf()
    private var nameRect: MutableList<Rect> = mutableListOf()
    private var jobRect: MutableList<Rect> = mutableListOf()

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

        for(position in 0 until itemsCount) {
            canvas?.drawRect(backgroundRect[position], backgroundPaint)
            canvas?.drawRect(leftRect[position], foregroundPaint)
            canvas?.drawRect(nameRect[position], foregroundPaint)
            canvas?.drawRect(jobRect[position], foregroundPaint)
        }

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

        content()
    }

    private fun content() {
        var offset = 0
        val dpInPx = context.dpToPx(80).roundToInt()
        itemsCount = ceil(height / dpInPx.toDouble()).toInt()
        for (position in 0..itemsCount) {
            createPlaceholderItem(offset)
            offset += 80
        }
    }
    private fun createPlaceholderItem(offset: Int) {
        backgroundRect.add(Rect(0, context.dpToPx(offset).toInt(), width, context.dpToPx(offset).toInt()))

        Log.d("placeholder", "[backgroundRect] top left Point: x=0, y=${context.dpToPx(offset)};")

        var xTopLeft = context.dpToPx(30).toInt()
        var yTopLeft = context.dpToPx(30 + offset).toInt()
        var xBottomRight = xTopLeft + context.dpToPx(18).toInt()
        var yBottomRight = yTopLeft + context.dpToPx(18).toInt()
        leftRect.add(Rect(xTopLeft, yTopLeft, xBottomRight, yBottomRight))

        Log.d("placeholder", "[leftRect] top left Point: x=$xTopLeft, y=$yTopLeft;")

        xTopLeft += context.dpToPx(44).toInt()
        yTopLeft = context.dpToPx(20 + offset).toInt()
        xBottomRight = xTopLeft + width / 2
        yBottomRight = yTopLeft + context.dpToPx(16).toInt()
        nameRect.add(Rect(xTopLeft, yTopLeft, xBottomRight, yBottomRight))

        Log.d("placeholder", "[nameRect] top left Point: x=$xTopLeft, y=$yTopLeft;")

        yTopLeft = yBottomRight + context.dpToPx(2).toInt()
        xBottomRight = xTopLeft + width - ( context.dpToPx(20).toInt() + xTopLeft )
        yBottomRight = yTopLeft + context.dpToPx(16).toInt()
        jobRect.add(Rect(xTopLeft, yTopLeft, xBottomRight, yBottomRight))

        Log.d("placeholder", "[jobRect] top left Point: x=$xTopLeft, y=$yTopLeft;")
    }

    companion object {
        private const val DEFAULT_WIDTH = 300
        private const val DEFAULT_PRIMARY_COLOR = Color.DKGRAY
        private const val DEFAULT_SECONDARY_COLOR = Color.LTGRAY
    }
}

