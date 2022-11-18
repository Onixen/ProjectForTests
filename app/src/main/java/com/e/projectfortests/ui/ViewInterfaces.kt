package com.e.projectfortests.ui

import com.e.projectfortests.model.ToDo

interface ViewHelper
interface HomeScreen : ViewHelper {
    fun uiSettings()
    fun showError(e: Exception)
    fun updateListData(data: List<ToDo>)
    fun enableScroll(flag: Boolean)
}
interface Settings : ViewHelper {

}