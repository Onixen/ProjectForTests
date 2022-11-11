package com.e.projectfortests.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.e.projectfortests.R
import com.e.projectfortests.databinding.FragmentHomeScreenBinding
import com.e.projectfortests.model.HomeScreenRecyclerItem
import com.e.projectfortests.model.ToDo
import com.e.projectfortests.model.User
import com.e.projectfortests.ui.adapters.HomeScreenRecyclerAdapter
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class HomeScreen: Fragment(R.layout.fragment_home_screen) {
    lateinit var binding: FragmentHomeScreenBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uiSettings()
        lifecycleScope.launch(Dispatchers.Main) {
            val data = fetchData()
            val cookedData = prepareDataForRecyclerAdapter(data)
            binding.jobsList.adapter = HomeScreenRecyclerAdapter(cookedData)
        }
    }

    private fun uiSettings() {

    }
    private suspend fun fetchData() :  MutableList<ToDo> = coroutineScope {
        val response = withContext(Dispatchers.IO) {
            delay(2000)
            URL("https://jsonplaceholder.typicode.com/todos").readText()
        }
        val itemsList = withContext(Dispatchers.Default) {
            val jsonArray = JSONArray(response)
            val temp = mutableListOf<ToDo>()

            for (position in 0 until jsonArray.length()) {
                val item: JSONObject = jsonArray[position] as JSONObject

                temp.add(
                    ToDo(
                    userId = item.getInt("userId"),
                    id = item.getInt("id"),
                    title = item.getString("title"),
                    completed = item.getBoolean("completed")
                )
                )
            }
            return@withContext temp
        }
        Log.i("testCoroutines", "Data is fetched")
        return@coroutineScope itemsList
    }
    private suspend fun prepareDataForRecyclerAdapter(jobs: List<ToDo>): List<HomeScreenRecyclerItem> = coroutineScope {
        withContext(Dispatchers.Default) {
                val usersList: List<User> = withContext(Dispatchers.IO) {
                    val response = JSONArray(URL("https://jsonplaceholder.typicode.com/users").readText())
                    val users = mutableListOf<User>()
                    for (position in 0 until response.length()) {
                        users.add(User(
                            id = (response[position] as JSONObject).getInt("id"),
                            name = (response[position] as JSONObject).getString("name")
                        ))
                    }
                    users
                }
                val temp = mutableListOf<HomeScreenRecyclerItem>()
                for (job in jobs) {
                    temp.add(HomeScreenRecyclerItem(
                        name = usersList.find { user -> user.id == job.userId }?.name.toString(),
                        job = job.title,
                        completed = job.completed
                    ))
                }
                temp
            }
    }
}