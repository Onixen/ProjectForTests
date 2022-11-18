package com.e.projectfortests.ui.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.e.projectfortests.MainActivity
import com.e.projectfortests.R
import com.e.projectfortests.databinding.FragmentSettingsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.e.projectfortests.ui.Settings as SettingsInterface

class Settings : Fragment(R.layout.fragment_settings), SettingsInterface {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var preferences: SharedPreferences
    private var canShowDialog = true
    private var currentSystemNightMode = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        preferences = requireActivity().getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE)
        binding.useAdaptiveColors.isChecked = preferences.getBoolean(ADAPTIVE_COLORS, false)
        binding.useNightMode.isChecked = preferences.getBoolean(NIGHT_MODE, false)
        currentSystemNightMode = requireActivity().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        if (currentSystemNightMode) {
            binding.useNightMode.isChecked = true
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.useAdaptiveColors.setOnCheckedChangeListener { _, value ->
            if(canShowDialog) {
                saveUsageAdaptiveColors(value)
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Предупреждение")
                    .setMessage("Эти изменения вступят в силу после перезагрузки приложения. Перезагрузить сейчас?")
                    .setPositiveButton("Ок") { dialog, _ ->
                        dialog.dismiss()
                        triggerRestart(requireActivity())
                    }
                    .setNegativeButton("Отмена") { dialog, _ ->
                        dialog.dismiss()
                        saveUsageAdaptiveColors(!value)
                        canShowDialog = false
                        binding.useAdaptiveColors.isChecked = !value
                    }
                    .setNeutralButton("Продолжить") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            } else {
                canShowDialog = true
            }
        }
        binding.useNightMode.setOnCheckedChangeListener { _, value ->
            when(value) {
                true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                false -> {
                    if (currentSystemNightMode) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
            saveUsageNightMode(value)
        }
    }

    private fun saveUsageAdaptiveColors(flag: Boolean) {
        preferences.edit {
            putBoolean(ADAPTIVE_COLORS, flag)
            apply()
        }
    }
    private fun saveUsageNightMode(flag: Boolean) {
        preferences.edit {
            putBoolean(NIGHT_MODE, flag)
            apply()
        }
    }
    private fun triggerRestart(context: Activity) {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        context.finish()
        Runtime.getRuntime().exit(0)
    }

    companion object {
        const val SETTINGS_PREFERENCES = "settings" //Название файла конфигурации
        const val ADAPTIVE_COLORS = "useAdaptiveColors"
        const val NIGHT_MODE = "nightMode"
    }
}