package com.example.userApp.Adapters.removeProductFromOrder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.userApp.R
import com.example.userApp.structur.other.DetailOrdered

class RemoveDetailsAdapter(val context : Context, val detailsList : ArrayList<DetailOrdered>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.remove_detail_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return detailsList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bindView(position)
    }

    inner class ViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView!!){
        private val detailName = itemView?.findViewById<TextView>(R.id.removeDetailName)

        fun bindView(position: Int){
            detailName?.text = detailsList[position].getName()
        }
    }
}