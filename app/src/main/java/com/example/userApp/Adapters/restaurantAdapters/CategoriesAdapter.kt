package com.example.userApp.Adapters.restaurantAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.userApp.R
import com.example.userApp.Services.GeneralServicesF.CategoriesServices
import com.example.userApp.structur.restaurantStructur.Category

class CategoriesAdapter(val context : Context, val categories : ArrayList<Category>, val clickEvent : () -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.category_recycler_view, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
         (holder as ViewHolder).bindView(position)
    }

    inner class ViewHolder(viewItem : View?) : RecyclerView.ViewHolder(viewItem!!){

        private val name = viewItem?.findViewById<TextView>(R.id.categoryName)
        private val background = viewItem?.findViewById<ImageView>(R.id.categoryBackground)
        private val subBackground = viewItem?.findViewById<ImageView>(R.id.categorySubBackground)

        fun bindView(position : Int){
            name?.text =  categories[position].getName()
            val isSelected = CategoriesServices.selectedCategory
            when {
                isSelected == -1 -> {
                    background?.setImageResource(R.color.transparent)
                    subBackground?.setImageResource(R.color.transparent)
                }
                position != isSelected -> {
                    background?.setImageResource(R.color.transparent)
                    subBackground?.setImageResource(R.color.transparent)
                }
                else -> {
                    background?.setImageResource(R.color.color1)
                    subBackground?.setImageResource(R.color.blackTransparent)
                }
            }

            background?.setOnClickListener {
                val selected = CategoriesServices.selectedCategory
                if(position != selected){
                    CategoriesServices.selectedCategory = position
                    notifyDataSetChanged()
                    clickEvent()
                }
            }
        }
    }


}