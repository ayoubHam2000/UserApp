package com.example.userApp.Adapters.removeProductFromOrder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.userApp.R
import com.example.userApp.structur.other.PropertyOrdered

//this class is used in removeProductAdapter for show productProperties in recyclerView

class RemovePropertiesAdapter(val context : Context, private val listOfProperties : ArrayList<PropertyOrdered>, private val extendable : ArrayList<Boolean>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.remove_properties_recycler_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if(extendable[0]){
            listOfProperties.size
        }else{
            0
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bindView(position)
    }


    inner class ViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView!!){

        private val layoutManager = LinearLayoutManager(context)

        private lateinit var detailAdapter : RemoveDetailsAdapter
        private val propertyNameRemove = itemView?.findViewById<TextView>(R.id.propertyNameRemove)
        private val detailsRecyclerView = itemView?.findViewById<RecyclerView>(R.id.detailsRecyclerViewRemove)

        fun bindView(position : Int){
            propertyNameRemove?.text = listOfProperties[position].getName()

            //set Up details adapter
            detailAdapter = RemoveDetailsAdapter(context, listOfProperties[position].details)
            detailsRecyclerView?.adapter = detailAdapter
            detailsRecyclerView?.layoutManager = layoutManager
        }
    }

}