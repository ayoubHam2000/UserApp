package com.example.userApp.structur.orderStructur

import com.example.userApp.Services.GeneralServicesF.RestaurantInfoServices
import com.example.userApp.structur.other.DetailOrdered
import com.example.userApp.structur.other.PropertyOrdered
import com.example.userApp.structur.restaurantStructur.Property

class ProductOrder(
    val productIndex : Int,
    val categoryIndex : Int,
    var count : Int,
    val productProperties : ArrayList<PropertyOrder>
){
    val theCategory = RestaurantInfoServices.restaurant.categories[categoryIndex]
    val theProduct = theCategory.ListProducts[productIndex]

    fun getProductPrice() : Double{
        return theProduct.price * (1 - (theProduct.discount) / 100)
    }

    fun getPropertyPrice() : Double{
        var totalPrice = 0.0

        for(item in productProperties){
            val property = theProduct.properties[item.property]
            for(detail in item.details){
                totalPrice += property.details[detail].price
            }
        }

        return totalPrice
    }

    fun totalProductPrice() : Double{
        return getProductPrice() + getPropertyPrice()
    }

    fun getProperties() : ArrayList<PropertyOrdered>{
        val properties = ArrayList<PropertyOrdered>()

        for(item in productProperties){
            val name = theProduct.properties[item.property].name
            val details = makePropertyDetail(theProduct.properties[item.property], item.details)
            val id = theProduct.properties[item.property].id

            val newProperty = PropertyOrdered(name, details, id)
            properties.add(newProperty)
        }

        return properties
    }

    private fun makePropertyDetail(property : Property, checkedDetail : ArrayList<Int>) : ArrayList<DetailOrdered>{
        val details = ArrayList<DetailOrdered>()

        for(item in checkedDetail){
            val name = property.details[item].name
            val price = property.details[item].price

            val newDetail = DetailOrdered(name, price)
            details.add(newDetail)
        }

        return details
    }
}