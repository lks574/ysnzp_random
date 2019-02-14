package com.example.ysnzp_random.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ysnzp_random.R
import kotlinx.android.synthetic.main.item_people.view.*

class CleanAdapter(val items: ArrayList<String>, val context: Context) : RecyclerView.Adapter<CleanAdapter.CleanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CleanViewHolder {
        return CleanViewHolder(LayoutInflater.from(context).inflate(R.layout.item_people, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CleanViewHolder, position: Int) {
        holder.recyclerPeopleText.text = items[position]
    }

    inner class CleanViewHolder(view: View): RecyclerView.ViewHolder(view){
        val recyclerPeopleText = view.recycler_clean_text
    }
}