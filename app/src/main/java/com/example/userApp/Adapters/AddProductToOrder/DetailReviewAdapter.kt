package com.example.userApp.Adapters.AddProductToOrder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.userApp.R

class DetailReviewAdapter(val context : Context, val detailList : ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.detail_review_recycler_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return detailList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bindView(position)
    }

    inner class ViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView!!){

        private val itemText = itemView?.findViewById<TextView>(R.id.itemNameReview)

        fun bindView(position : Int){
            itemText?.text = detailList[position]

        }

    }
}