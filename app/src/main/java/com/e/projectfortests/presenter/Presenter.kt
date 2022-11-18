package com.e.projectfortests.presenter

import android.content.SharedPreferences
import androidx.core.content.edit
import com.e.projectfortests.model.Api
import com.e.projectfortests.ui.HomeScreen
import com.e.projectfortests.ui.ViewHelper
import com.e.projectfortests.ui.fragments.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class Presenter(private val view: ViewHelper) {
    private var api: Api = Api()

    suspend fun fetchTODO() {
        view as HomeScreen
        withContext(Dispatchers.Default) {
            view.enableScroll(false)
            try {
                api.fetchToDo()?.let { view.updateListData(it) }
            } catch (e: Exception) {
                view.showError(e)
            }
            view.enableScroll(true)
        }
    }
}