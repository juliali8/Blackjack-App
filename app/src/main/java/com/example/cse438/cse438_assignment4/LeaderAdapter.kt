package com.example.cse438.cse438_assignment4

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.leader_item.view.*

class LeaderAdapter(private val leaderList: List<LeaderItem>) : RecyclerView.Adapter<LeaderAdapter.LeaderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.leader_item, parent, false)
        return LeaderViewHolder(itemView)
    }

    override fun getItemCount() = leaderList.size

    override fun onBindViewHolder(holder: LeaderViewHolder, position: Int) {
        val currentItem = leaderList[position]
        holder.textView.text = currentItem.text1
    }

    class LeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.item_text_content
    }
}