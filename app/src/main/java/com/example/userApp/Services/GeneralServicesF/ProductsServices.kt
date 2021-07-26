package com.example.userApp.Services.GeneralServicesF

import com.example.userApp.structur.other.SearchStruct
import com.example.userApp.structur.restaurantStructur.Product

object ProductsServices {

    val productList = ArrayList<Product>()
    var selectedProduct : Product? = null

    fun getListOfProducts(categoryIndex : Int){
        productList.clear()
        productList.addAll(CategoriesServices.categoryList[categoryIndex].ListProducts)
    }

    fun getListOfSearchedProducts(list : ArrayList<Product>){
        productList.clear()
        productList.addAll(list)
    }

    fun buildSearchStructList(productsList : ArrayList<Product>) : ArrayList<SearchStruct>{
        val result = ArrayList<SearchStruct>()

        for(item in productsList){
            val categoryExist = indexOfProductInSearchList(result, item.categoryIndex)
            if(categoryExist == -1){
                val searchItem = SearchStruct(item.categoryIndex, arrayListOf(item) )
                result.add(searchItem)
            }else{
                result[categoryExist].products.add(item)
            }
        }

        return result
    }

    private fun indexOfProductInSearchList(searchList : ArrayList<SearchStruct>, CategoryIndex : Int) : Int{

        for(x in 0 until  searchList.size){
            val item = searchList[x]
            if(item.categoryIndex == CategoryIndex)
                return x
        }

        return -1
    }


}