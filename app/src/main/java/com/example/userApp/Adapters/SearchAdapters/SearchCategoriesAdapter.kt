package com.example.userApp.Adapters.SearchAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.userApp.Adapters.restaurantAdapters.ProductsAdapter
import com.example.userApp.R
import com.example.userApp.Services.GeneralServicesF.RestaurantInfoServices
import com.example.userApp.structur.other.SearchStruct

class SearchCategoriesAdapter(val context : Context, val searchList : ArrayList<SearchStruct>, val clickEvent : (String) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    //it use ProductAdapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.search_categories_name, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bindView(position)
    }

    inner class ViewHolder(viewItem : View?) : RecyclerView.ViewHolder(viewItem!!){

        private lateinit var productAdapter : ProductsAdapter
        private val layoutManager = GridLayoutManager(context, 2)

        private val categoryName = viewItem?.findViewById<TextView>(R.id.searchCategoryName)
        private val productRecyclerView = viewItem?.findViewById<RecyclerView>(R.id.productRecyclerViewSearch)

        fun bindView(position : Int){
            val theCategory = RestaurantInfoServices.restaurant.categories[ searchList[position].categoryIndex ]
            categoryName?.text =  theCategory.getName()

            //set up product recyclerView
            productAdapter = ProductsAdapter(context, searchList[position].products){ event ->
                clickEvent(event)
            }
            productRecyclerView?.adapter = productAdapter
            productRecyclerView?.layoutManager = layoutManager
        }
    }
}