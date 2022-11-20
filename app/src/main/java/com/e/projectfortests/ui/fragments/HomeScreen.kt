package com.e.projectfortests.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.e.projectfortests.R
import com.e.projectfortests.databinding.FragmentHomeScreenBinding
import com.e.projectfortests.model.HomeScreenRecyclerItem
import com.e.projectfortests.presenter.Presenter
import com.e.projectfortests.ui.adapters.HomeScreenRecyclerAdapter
import com.e.projectfortests.utilities.ModifiedLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.*

class HomeScreen: Fragment(R.layout.fragment_home_screen), com.e.projectfortests.ui.HomeScreen {
    private lateinit var binding: FragmentHomeScreenBinding

    private lateinit var presenter: Presenter
    private lateinit var customLayoutManager: ModifiedLayoutManager
    private var loadingData:  List<HomeScreenRecyclerItem>? = null

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
    override fun updateListData(data: List<HomeScreenRecyclerItem>) {
        lifecycleScope.launch(Dispatchers.Main) {
            binding.jobsList.adapter = HomeScreenRecyclerAdapter(data)
            binding.swipeRefreshLayout.isRefreshing = false
            loadingData = data
        }
    }
    override fun enableScroll(flag: Boolean) {
        binding.jobsList.layoutManager = customLayoutManager.apply { setScrollEnable(flag) }
    }

    private fun uiSettings() {
        if (loadingData == null) lifecycleScope.launch { presenter.fetchTODO() }
        binding.jobsList.adapter = HomeScreenRecyclerAdapter(loadingData)
        binding.toolbar.setOnMenuItemClickListener(menuClickListener)
        binding.swipeRefreshLayout.setOnRefreshListener {
            startUpdateData()
        }
    }
    private fun startUpdateData() {
        binding.jobsList.adapter = HomeScreenRecyclerAdapter(null)
        lifecycleScope.launch { presenter.fetchTODO() }
    }
    private val menuClickListener = Toolbar.OnMenuItemClickListener { item ->
        when (item!!.itemId) {
            R.id.settings -> {
                findNavController().navigate(R.id.action_homeScreen_to_settings2)
                true
            }
            else -> true
        }
    }
}