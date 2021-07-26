package com.example.userApp.Adapters.removeProductFromOrder

import android.R.attr.*
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Matrix
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.userApp.R
import com.example.userApp.structur.orderStructur.ProductOrder
import com.example.userApp.structur.other.PropertyOrdered
import kotlin.random.Random


class RemoveProductAdapter(val context : Context, val productOrders : ArrayList<ProductOrder>, val clickEvent : (Int, String) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.remove_product_recycler_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productOrders.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bindView(position)
    }


    //there is tow adapter attach to this section
    inner class ViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView!!){

        private lateinit var propertiesAdapter : RemovePropertiesAdapter
        private val layoutManager = LinearLayoutManager(context)
        private val extendable = ArrayList<Boolean>()

        private val productName = itemView?.findViewById<TextView>(R.id.productNameRemove)
        private val productQuantity = itemView?.findViewById<TextView>(R.id.productQuantityRemove)
        private val properties = itemView?.findViewById<TextView>(R.id.propertiesHintRemove)
        private val productRemoveOrderButton = itemView?.findViewById<ImageView>(R.id.productRemoveOrderButton)
        private val productProperties = itemView?.findViewById<ImageView>(R.id.productPropertiesRemove)
        private val recyclerViewOfProperties = itemView?.findViewById<RecyclerView>(R.id.recyclerViewOfProperties)


        @SuppressLint("SetTextI18n")
        fun bindView(position : Int){
            extendable.add(false)
            productName?.text = "TYPE #${position + 1}"
            productQuantity?.text = productOrders[position].count.toString()
            properties?.text = makePropertiesText(productOrders[position].getProperties())

            productRemoveOrderButton?.setOnClickListener {
                removeProduct(position)
            }

            productProperties?.setOnClickListener {
                extendable[0] = !extendable[0]
                propertiesAdapter.notifyDataSetChanged()
                productProperties.rotation = if(extendable[0]){180f} else {0f}
                clickEvent(position, "scroll")
            }

            //setUpAdapter
            propertiesAdapter = RemovePropertiesAdapter(context, productOrders[position].getProperties(), extendable)
            recyclerViewOfProperties?.adapter = propertiesAdapter
            recyclerViewOfProperties?.layoutManager = layoutManager
        }

        private fun removeProduct(position : Int){
            clickEvent(position, "delete")
        }



        private fun makePropertiesText(properties : ArrayList<PropertyOrdered>) : String{
            var temp = ""
            var result = ""

            for(y in 0 until properties.size){
                val item = properties[y]
                val randomDetailItem = 3
                for(x in 0 until item.details.size){
                    if(x <= randomDetailItem){
                        temp += item.details[x].getName()
                        if(x != item.details.size - 1){
                            temp += ", "
                        }
                    }else{
                        break
                    }
                }
                if(y != properties.size - 1)
                    temp += ", "
            }

            val randomLength = 50
            for(x in 0 until randomLength){
                if(x < temp.length)
                    result += temp[x]
            }
            result += " ..."
            return result
        }
    }
}