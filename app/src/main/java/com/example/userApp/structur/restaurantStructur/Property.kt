package com.example.userApp.structur.restaurantStructur

import com.example.userApp.utilities.LANGUAGE

class Property(
    val name : ArrayList<String>,
    val require : Boolean,
    val min : Int,
    val max : Int,
    val details : ArrayList<Detail>,
    val id : String,
    var itemChecked : Int,
    var validity : Boolean
){

    val detailsSize = details.size

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

    fun propertyReset(){
        itemChecked = 0

        for(item in details){
            if(item.check){ itemChecked++ }
            item.isCheck = item.check
        }

        validity = if(require){ itemChecked in min..max }else true
    }

    fun getPropertyPrice() : Double{
        var totalPrice = 0.0

        for(item in details){
            if(item.isCheck){
                totalPrice += item.price
            }
        }
        return totalPrice
    }

}