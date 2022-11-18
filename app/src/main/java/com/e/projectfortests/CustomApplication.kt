package com.e.projectfortests

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.e.projectfortests.ui.fragments.Settings
import com.google.android.material.color.DynamicColors

class CustomApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        applySettings()
    }

    private fun applySettings() {
        val preferences = getSharedPreferences(Settings.SETTINGS_PREFERENCES, Context.MODE_PRIVATE)

        if (preferences.getBoolean(Settings.ADAPTIVE_COLORS, false)) {
            DynamicColors.applyToActivitiesIfAvailable(this)
        }

        if (preferences.getBoolean(Settings.NIGHT_MODE, false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}