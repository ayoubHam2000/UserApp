package com.example.userApp.structur.restaurantStructur

import com.example.userApp.utilities.LANGUAGE

class Detail(
    val name : ArrayList<String>,
    val check : Boolean,
    val price : Double,
    var isCheck : Boolean
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
}