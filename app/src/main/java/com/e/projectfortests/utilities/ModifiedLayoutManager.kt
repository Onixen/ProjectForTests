package com.e.projectfortests.utilities

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

/**
 * Стандартный LayoutManager с добавлением метода [setScrollEnable]
 * */
class ModifiedLayoutManager(context: Context) : LinearLayoutManager(context) {
    private var isScrollEnable = false

    /**
     * Метод управления состоянием скроллинга
     * */
    fun setScrollEnable(flag: Boolean) {
        this.isScrollEnable = flag
    }

    override fun canScrollVertically(): Boolean {
        return super.canScrollVertically() && isScrollEnable
    }
}