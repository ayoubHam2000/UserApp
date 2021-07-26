package com.example.userApp.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.userApp.R
import com.example.userApp.Services.OrderServices
import com.example.userApp.structur.orderStructur.ProductOrder
import com.example.userApp.utilities.UNIT_PRICE
import java.text.DecimalFormat

class ReviewAdapter(val context : Context, val productOrders : ArrayList<ProductOrder>, val notify : (Boolean) -> Unit ) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.command_review_recycler_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productOrders.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bindView(position)
    }

    inner class ViewHolder(itemView : View?): RecyclerView.ViewHolder(itemView!!){

        private val productNameReview = itemView?.findViewById<TextView>(R.id.productNameReview)
        private val productDetailReview = itemView?.findViewById<TextView>(R.id.productDetailReview)
        private val priceCount = itemView?.findViewById<TextView>(R.id.priceCount)
        private val addButtonReview = itemView?.findViewById<ImageView>(R.id.addButtonReview)
        private val removeProductReview = itemView?.findViewById<ImageView>(R.id.removeProductReview)
        private val totalPriceProduct = itemView?.findViewById<TextView>(R.id.totalPriceProduct)
        private val priceUnitReview = itemView?.findViewById<TextView>(R.id.priceUnitReview)


        @SuppressLint("SetTextI18n")
        fun bindView(position : Int){
            val price = productOrders[position].totalProductPrice()
            val totalPrice = price * productOrders[position].count

            productNameReview?.text = productOrders[position].theProduct.getName()
            productDetailReview?.text = makeDetail(position)
            priceCount?.text = "$price * ${productOrders[position].count}"
            totalPriceProduct?.text = DecimalFormat("##.##").format(totalPrice)
            priceUnitReview?.text = UNIT_PRICE

            addButtonReview?.setOnClickListener {
                addProductToOrder(position)
            }

            removeProductReview?.setOnClickListener {
                removeProductFromOrder(position)
            }


        }

        private fun addProductToOrder(position: Int){
            OrderServices.addAnOrder(productOrders[position])
            notify(true)
            notifyDataSetChanged()
        }

        private fun removeProductFromOrder(position: Int){
            OrderServices.removeAnOrder(productOrders[position])
            notify(true)
            notifyDataSetChanged()
        }

        private fun makeDetail(position : Int) : String{
            val maxi = 3
            val maxj = 3
            var result = ""
            val properties = productOrders[position].getProperties()

            for(i in 0 until properties.size){
                val property = properties[i]
                if(i < maxi){
                    result += property.getName() + " : "
                    for(j in 0 until  property.details.size){
                        val detail = property.details[j]
                        if(j < maxj){
                            result += detail.getName()
                            if(j != property.details.size - 1)
                                result += " ,"
                        }else{
                            break
                        }
                    }
                    result += "\n"
                }else{
                    break
                }
            }

            return result
        }


    }



}