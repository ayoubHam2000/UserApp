package com.example.userApp.structur.restaurantStructur

import com.example.userApp.utilities.LANGUAGE

class Category(
    val name : ArrayList<String>,
    val ListProducts : ArrayList<Product>,
    val id : String
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