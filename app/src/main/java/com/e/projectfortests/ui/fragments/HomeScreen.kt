package com.e.projectfortests.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.e.projectfortests.R
import com.e.projectfortests.databinding.FragmentHomeScreenBinding
import com.e.projectfortests.model.HomeScreenRecyclerItem
import com.e.projectfortests.model.ToDo
import com.e.projectfortests.model.User
import com.e.projectfortests.presenter.Presenter
import com.e.projectfortests.ui.adapters.HomeScreenRecyclerAdapter
import com.e.projectfortests.utilities.ModifiedLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class HomeScreen: Fragment(R.layout.fragment_home_screen), com.e.projectfortests.ui.HomeScreen {
    private lateinit var binding: FragmentHomeScreenBinding
    private var loadingData:  List<HomeScreenRecyclerItem>? = null
    private lateinit var presenter: Presenter
    private lateinit var customLayoutManager: ModifiedLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        presenter = Presenter(this)
        customLayoutManager = ModifiedLayoutManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiSettings()
    }
    override fun uiSettings() {
        binding.toolbar.setOnMenuItemClickListener {
        when(it.itemId) {
            R.id.settings -> {
                findNavController().navigate(R.id.action_homeScreen_to_settings2)
                true
            }
            else -> true
        }
    }
        lifecycleScope.launch(Dispatchers.Default) { presenter.fetchTODO() }
        binding.jobsList.adapter = HomeScreenRecyclerAdapter(loadingData)
    }

    override fun showError(e: Exception) {
       lifecycleScope.launch(Dispatchers.Main) {
           MaterialAlertDialogBuilder(requireContext())
               .setTitle("Ошибка")
               .setMessage(e.localizedMessage)
               .setPositiveButton("Продолжить") { dialog, _ ->
                   dialog.dismiss()
                   binding.jobsList.adapter = null
               }
               .show()
       }
    }
    override fun updateListData(data: List<ToDo>) {
        lifecycleScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.Default) { loadingData = prepareDataForRecyclerAdapter(data) }
            binding.jobsList.adapter = HomeScreenRecyclerAdapter(loadingData)
        }
    }

    override fun enableScroll(flag: Boolean) {
        binding.jobsList.layoutManager = customLayoutManager.apply { setScrollEnable(flag) }
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