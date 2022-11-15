package com.e.projectfortests.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.MenuRes
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.e.projectfortests.R
import com.e.projectfortests.databinding.FragmentHomeScreenBinding
import com.e.projectfortests.model.HomeScreenRecyclerItem
import com.e.projectfortests.model.ToDo
import com.e.projectfortests.model.User
import com.e.projectfortests.ui.adapters.HomeScreenRecyclerAdapter
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class HomeScreen: Fragment(R.layout.fragment_home_screen) {
    private lateinit var binding: FragmentHomeScreenBinding
    private var loadingData:  List<HomeScreenRecyclerItem>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uiSettings()
        lifecycleScope.launch(Dispatchers.Main) {
            if (loadingData == null) {
                binding.jobsList.adapter = HomeScreenRecyclerAdapter(null)
                val data = fetchData()
                val cookedData = prepareDataForRecyclerAdapter(data)
                loadingData = cookedData
                binding.jobsList.adapter = HomeScreenRecyclerAdapter(cookedData)
            } else {
                binding.jobsList.adapter = HomeScreenRecyclerAdapter(loadingData)
            }
        }
    }

    private fun uiSettings() {
        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.settings -> {
                    findNavController().navigate(R.id.action_homeScreen_to_settings2)
                    true
                }
                else -> true
            }
        }
    }
    private suspend fun fetchData() :  MutableList<ToDo> = coroutineScope {
        val response = withContext(Dispatchers.IO) {
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