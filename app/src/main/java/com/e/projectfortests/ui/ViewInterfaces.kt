package com.e.projectfortests.ui

import com.e.projectfortests.model.HomeScreenRecyclerItem
import com.e.projectfortests.model.ToDo

interface ViewHelper
interface HomeScreen : ViewHelper {
    fun showError(e: Exception)
    fun updateListData(data: List<HomeScreenRecyclerItem>)
    fun enableScroll(flag: Boolean)
}
interface Settings : ViewHelper {

}