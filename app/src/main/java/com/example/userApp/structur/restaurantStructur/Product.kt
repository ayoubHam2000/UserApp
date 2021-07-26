package com.example.userApp.structur.restaurantStructur

import com.example.userApp.utilities.LANGUAGE

class Product (
    val name : ArrayList<String>,
    val price : Double,
    val discount : Double,
    val properties : ArrayList<Property>,
    val details : ArrayList<String>,
    val id : String,
    val categoryIndex : Int,
    val productIndex : Int,
    val imageCreatedTime : Long,
    val imageURl : String
){
    fun getName() : String{
        return if(LANGUAGE == 0){
            name[0]
        }else{
            if(name[1] == ""){
                name[0]
            }else{
                name[1]
            }
        }
    }

    fun getDetails() : String{
        return if(LANGUAGE == 0){
            details[0]
        }else{
            if(details[1] == ""){
                details[0]
            }else{
                details[1]
            }
        }
    }

    fun getPropertiesPrice() : Double{
        var totalPrice = 0.0

        for(item in properties){
            totalPrice += item.getPropertyPrice()
        }

        return totalPrice
    }
}