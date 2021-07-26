package com.example.userApp.Adapters.AddProductToOrder

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.userApp.R
import com.example.userApp.structur.restaurantStructur.Property

class PropertiesAdapter(val context : Context, val properties : ArrayList<Property>, val totalPrice : (Boolean) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.detail_title_recycler_view, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return properties.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bindView(position)
    }

    inner class ViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView!!){

        private lateinit var detailAdapter : PropertyDetailsAdapter

        private val propertyName = itemView?.findViewById<TextView>(R.id.propertyName)
        private val propertyRequire = itemView?.findViewById<TextView>(R.id.propertyRequire)
        private val detailBound = itemView?.findViewById<TextView>(R.id.detailBound)
        private val propertyDetailRecyclerVIew = itemView?.findViewById<RecyclerView>(R.id.propertyDetailRecyclerVIew)

        @SuppressLint("DefaultLocale", "SetTextI18n")
        fun bindView(position: Int){

            val min = properties[position].min
            val max = properties[position].max
            val require = properties[position].require

            propertyName?.text = properties[position].getName()
            detailBound?.text = "(${min} - ${max})"
            if(require){
                propertyRequire?.text = context.resources.getString(R.string.require)
                if(properties[position].validity){
                    propertyRequire?.setTextColor(ContextCompat.getColor(context, R.color.green))
                }else{
                    propertyRequire?.setTextColor(ContextCompat.getColor(context, R.color.red))
                }

            }else{
                propertyRequire?.text = context.resources.getString(R.string.optional)
                propertyRequire?.setTextColor(ContextCompat.getColor(context, R.color.green))
            }

            setUpRecyclerView(position, require)
        }

        private fun setUpRecyclerView(position: Int, require : Boolean){
            detailAdapter =
                PropertyDetailsAdapter(
                    context,
                    properties[position]
                ) { itemCheck ->

                    if (itemCheck && require) {
                        properties[position].validity = true
                        propertyRequire?.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.green
                            )
                        )
                    } else if (require) {
                        properties[position].validity = false
                        propertyRequire?.setTextColor(ContextCompat.getColor(context, R.color.red))
                    }

                    totalPrice(itemCheck)
                }

            val layoutManager = LinearLayoutManager(context)

            propertyDetailRecyclerVIew?.adapter = detailAdapter
            propertyDetailRecyclerVIew?.layoutManager = layoutManager

        }

    }

}