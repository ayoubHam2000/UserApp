package com.example.userApp.Adapters.AddProductToOrder

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.userApp.R
import com.example.userApp.structur.restaurantStructur.Property
import com.example.userApp.utilities.UNIT_PRICE
import java.text.DecimalFormat

class PropertyDetailsAdapter(val context : Context, val property : Property, val detailValidity : (Boolean) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.detail_name_recyclerview, parent, false)
        return ViewHolder((view))
    }

    override fun getItemCount(): Int {
        return property.detailsSize
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bindView(position)
    }

    inner class ViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView!!){

        private val detailName = itemView?.findViewById<CheckBox>(R.id.detailName)
        private val detailPrice = itemView?.findViewById<TextView>(R.id.detailPrice)

        @SuppressLint("SetTextI18n")
        fun bindView(position : Int){

            val max = property.max
            val min = property.min
            val price = property.details[position].price

            detailName?.isChecked = property.details[position].check
            detailName?.text = property.details[position].getName()
            detailPrice?.text = price.toString()

            if(property.details[position].price == 0.0){
                detailPrice?.visibility = View.INVISIBLE
            }else{
                val Sprice = DecimalFormat("##.##").format(price)
                detailPrice?.text = "+${Sprice}${UNIT_PRICE}"
            }

            detailName?.setOnClickListener {
                //println("before---> ${property.itemChecked} || $min || $max ")
                if(detailName.isChecked){

                    if(property.itemChecked < max){
                        property.itemChecked++
                        property.details[position].isCheck = true
                    }else{
                        detailName.isChecked = false
                        makeMessage("max choices is $max")
                    }
                }else{
                    property.details[position].isCheck = false
                    property.itemChecked--
                }

                if(property.itemChecked >= min){
                    detailValidity(true)
                }else{
                    detailValidity(false)
                }
                //println("after---> ${property.itemChecked} || $min || $max ")
            }
        }

        private fun makeMessage(message : String){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}