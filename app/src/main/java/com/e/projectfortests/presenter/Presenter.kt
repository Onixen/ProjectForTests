package com.e.projectfortests.presenter

import com.e.projectfortests.model.*
import com.e.projectfortests.ui.HomeScreen
import com.e.projectfortests.ui.ViewHelper
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class Presenter(private val view: ViewHelper) {
    private var api: Api = Api()

    suspend fun fetchTODO() {
        view as HomeScreen
        withContext(Dispatchers.Default) {
            view.enableScroll(false)
            try {
                val jobs = async { api.fetchToDo() }
                val users = async { api.fetchUsers() }

                cookedDataForList(jobs.await()!!, users.await()!!).let {
                    view.updateListData(it)
                }
            } catch (e: Exception) {
                view.showError(e)
            }
            view.enableScroll(true)
        }
    }

    private fun cookedDataForList(jobs: TODOs, users: Users): List<HomeScreenRecyclerItem> {
        return mutableListOf<HomeScreenRecyclerItem>().apply {
            for (job in jobs) {
                add(
                    HomeScreenRecyclerItem(
                        name = users.find { user -> user.id == job.userId }?.name.toString(),
                        job = job.title,
                        completed = job.completed
                    )
                )
            }
        }
    }
}