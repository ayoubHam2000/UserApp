package com.example.userApp.Services

import android.content.Context
import android.net.ConnectivityManager
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.userApp.R
import com.example.userApp.structur.other.SearchStruct
import com.example.userApp.structur.restaurantStructur.Product
import java.text.DecimalFormat


object GeneralServices {

    lateinit var mainActivityPrice : TextView
    lateinit var searchField : AutoCompleteTextView
    var categoryRecyclerView : RecyclerView? = null
    var orderReviewView : ImageView? = null



    fun changeMainActivityPrice(){
        val price = OrderServices.getTotalPrice()
        val stringPrice = DecimalFormat("##.##").format(price)
        mainActivityPrice.text = stringPrice
    }


    fun notifyCategoryRecyclerView(){
        if(categoryRecyclerView != null)
            categoryRecyclerView?.adapter?.notifyDataSetChanged()
    }

    fun notifyOrderReviewView(){
        if(orderReviewView != null)
            orderReviewView?.setBackgroundResource(R.color.transparent)
    }




}