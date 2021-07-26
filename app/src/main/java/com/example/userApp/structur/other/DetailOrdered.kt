package com.example.userApp.structur.other

import com.example.userApp.utilities.LANGUAGE

class DetailOrdered(
    val name : ArrayList<String>,
    val price : Double
) {
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