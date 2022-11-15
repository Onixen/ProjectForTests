package com.e.projectfortests.ui.customViews

import android.content.Context
import android.graphics.Canvas

fun Context.dpToPx(dp: Int): Float {
    return dp.toFloat() * this.resources.displayMetrics.density
}