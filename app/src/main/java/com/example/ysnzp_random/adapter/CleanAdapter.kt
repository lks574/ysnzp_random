package com.example.ysnzp_random.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ysnzp_random.R
import com.example.ysnzp_random.model.CleanModel
import kotlinx.android.synthetic.main.item_people.view.*

class CleanAdapter(private val items: ArrayList<CleanModel>, private val context: Context) : RecyclerView.Adapter<CleanAdapter.CleanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CleanViewHolder {
        return CleanViewHolder(LayoutInflater.from(context).inflate(R.layout.item_people, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CleanViewHolder, position: Int) {
        holder.recyclerCleanText.text = items[position].name
        holder.recyclerCleanText.tag = position
        holder.personNumber.text = items[position].number.toString()
        holder.personNumber.tag = position
    }



    inner class CleanViewHolder(view: View): RecyclerView.ViewHolder(view){
        val recyclerCleanText = view.recycler_clean_text
        val personNumber = view.clean_person_number
    }
}