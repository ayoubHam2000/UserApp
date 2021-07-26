package com.example.userApp.Services

import com.example.userApp.structur.orderStructur.ProductOrder
import com.example.userApp.structur.orderStructur.PropertyOrder
import com.example.userApp.structur.restaurantStructur.Detail
import com.example.userApp.structur.restaurantStructur.Product
import com.example.userApp.structur.restaurantStructur.Property


object OrderServices {

     val listOfOrders = ArrayList<ProductOrder>()

    /*###################################################################*/
    /*###################################################################*/
    /*########################--add An Order--###########################*/
    /*###################################################################*/
    /*###################################################################*/

    fun addAnOrder(theOrder : ProductOrder){

        val indexOfIdenticalOrders = getIdenticalOrder(theOrder)
        listOfOrders[indexOfIdenticalOrders].count += 1
        //displayOrders()
    }

    fun addAnOrder(theProduct : Product, count : Int){

        val theOrder = makeOrder(theProduct, count)
        val indexOfIdenticalOrders = getIdenticalOrder(theOrder)
        if(indexOfIdenticalOrders != -1){
            listOfOrders[indexOfIdenticalOrders].count += theOrder.count
        }else{
            listOfOrders.add(theOrder)
        }
        //displayOrders()
    }

    private fun getIdenticalOrder(theOrder : ProductOrder) : Int{
        //return the index of identical order
        for(x in 0 until listOfOrders.size){
            val item = listOfOrders[x]
            if(item.productIndex == theOrder.productIndex){
                if(item.categoryIndex == theOrder.categoryIndex){
                    if( checkIdenticalProperties(item.productProperties, theOrder.productProperties) ){
                        return x
                    }
                }
            }
        }
        return -1
    }

    private fun checkIdenticalProperties(Properties1 : ArrayList<PropertyOrder>, Properties2 : ArrayList<PropertyOrder>) : Boolean{
        if(Properties1.size != Properties2.size){
            return false
        }
        for(x in 0 until Properties1.size){
            if(Properties1[x].property != Properties2[x].property){
                return false
            }
            if(Properties1[x].details != Properties2[x].details){
                return false
            }
        }
        return true
    }







    /*###################################################################*/
    /*###################################################################*/
    /*#####################--remove An Order--###########################*/
    /*###################################################################*/
    /*###################################################################*/

    //if product completely removed it return true
     fun removeAnOrder(productOrder : ProductOrder) : Boolean{
        val index =  getIdenticalOrder(productOrder)
        return if(listOfOrders[index].count == 1){
            listOfOrders.removeAt(index)
            //displayOrders()
            true
        }else{
            listOfOrders[index].count--
            //displayOrders()
            false
        }
    }


    /*###################################################################*/
    /*###################################################################*/
    /*#####################--Get Information--###########################*/
    /*###################################################################*/
    /*###################################################################*/

    fun getProductNumber(categoryIndex : Int, productIndex : Int) : Int{
        var count = 0
        for(item in listOfOrders){
            if(item.categoryIndex == categoryIndex){
                if(item.productIndex == productIndex){
                    count += item.count
                }
            }
        }
        return count
    }

    fun getTotalPrice() : Double{
        var totalPrice = 0.0

        for(item in listOfOrders){
            totalPrice += item.totalProductPrice() * item.count
        }
        return totalPrice
    }

    fun getOrderedProduct(selectedProduct : Product) : ArrayList<ProductOrder>{
        val listOfThisProduct = ArrayList<ProductOrder>()

        for(item in listOfOrders){
            if(item.categoryIndex == selectedProduct.categoryIndex){
                if(item.productIndex == selectedProduct.productIndex){
                    listOfThisProduct.add(item)
                }
            }
        }

        return listOfThisProduct
    }










    /*###################################################################*/
    /*###################################################################*/
    /*#####################--remove An Order--###########################*/
    /*###################################################################*/
    /*###################################################################*/






    /*###################################################################*/
    /*###################################################################*/
    /*###########--Make new Order From Product given--###################*/
    /*###################################################################*/
    /*###################################################################*/

    private fun makeOrder(product : Product, quantity : Int) : ProductOrder{
        val productIndex = product.productIndex
        val categoryIndex = product.categoryIndex
        val propertyOrder = makePropertyOrder(product.properties)

        return ProductOrder(productIndex, categoryIndex, quantity,propertyOrder)
    }

    private fun makePropertyOrder(properties : ArrayList<Property>) : ArrayList<PropertyOrder>{
        val propertyOrders = ArrayList<PropertyOrder>()

        for(x in 0 until  properties.size){
            val property = properties[x]
            if(property.itemChecked > 0){
                val detailOrder = makeDetailOrder(property.details)
                val newPropertyOrder = PropertyOrder(x, detailOrder)
                propertyOrders.add(newPropertyOrder)
            }
        }

        return propertyOrders
    }

    private fun makeDetailOrder(details : ArrayList<Detail>) : ArrayList<Int> {
        val checkedDetailIndex = ArrayList<Int>()
        for(y in 0 until details.size){
            val detail = details[y]
            if(detail.isCheck){
                checkedDetailIndex.add(y)
            }
        }
        return checkedDetailIndex
    }

    private fun displayOrders(){
        println("---------------------------------------")
        for(item in listOfOrders){
            println("product name : ${item.theProduct.getName()}")
            println("the category : ${item.theCategory.getName()}")
            println("count : ${item.count}")
            println("Product Discount : ${item.theProduct.discount}")
            println("Product Price : ${item.getProductPrice()}")
            println("Properties Price : ${item.getPropertyPrice()} ")

            val properties = item.getProperties()
            for(property in properties){
                print("${property.getName()} : ")
                for(detail in property.details){
                    print("${detail.getName()}, ")
                }
                println("")
            }
        }
        println("---------------------------------------")
    }

}