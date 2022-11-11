package com.e.projectfortests

import android.app.Application
import com.google.android.material.color.DynamicColors

class CustomApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}