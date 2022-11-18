package com.e.projectfortests.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.e.projectfortests.R
import com.e.projectfortests.model.HomeScreenRecyclerItem

class HomeScreenRecyclerAdapter(private val list: List<HomeScreenRecyclerItem>?): RecyclerView.Adapter<HomeScreenRecyclerAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.name)
        val job = itemView.findViewById<TextView>(R.id.job)
        val completed = itemView.findViewById<CheckBox>(R.id.completed)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val listItem = if (list == null) {
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_placeholder_home_screen, parent, false)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_home_screen, parent, false)
        }
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            list?.let {
                name.text = list[position].name
                job.text = list[position].job
                completed.isChecked = list[position].completed
            }
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 1
    }
}