package com.example.userApp.healper

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import com.example.userApp.Adapters.SearchAdapters.SearchAdapter
import com.example.userApp.R
import com.example.userApp.Services.GeneralServicesF.CategoriesServices
import com.example.userApp.Services.GeneralServicesF.ProductsServices
import com.example.userApp.structur.other.IndexPattern
import com.example.userApp.structur.restaurantStructur.Product
import com.example.userApp.utilities.SEARCHTAP
import kotlin.concurrent.thread

//import kotlinx.android.synthetic.main.activity_main.*


class SearchServices(val context : Context, private val searchView : CustomAutoCompleteTextView, val textChange : (Int)-> Unit) {

    fun startSearch(){
        productSearch()
    }

    private fun productSearch(){

        //this is just for set up the filter and list adapter look at the next function
        val listOfProductString = ArrayList<String>()
        val etAdapter = SearchAdapter(context, listOfProductString)


        val filterTextWatcher: TextWatcher = object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int){
                onTextChange(s, listOfProductString)

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        }

        searchView.addTextChangedListener(filterTextWatcher)
        searchView.setAdapter(etAdapter.setArrayAdapter)
        searchView.threshold = 0

    }

    private fun onTextChange(s: CharSequence, listOfProductString : ArrayList<String>){
        if(s.isNotEmpty()){
            SEARCHTAP++
            println("send is $SEARCHTAP")
            setMatchProductSearch(s.toString(), listOfProductString)
            textChange(SEARCHTAP)
        }
    }

    private fun setMatchProductSearch(pattern : String, matchedString : ArrayList<String>){
        val categories = CategoriesServices.categoryList
        val matchedProducts = ArrayList<Product>()
        val patternMap = ArrayList<IndexPattern>()
        var x = 0

        matchedString.clear()
        for(category in categories){
            for(product in category.ListProducts){
                val index = product.getName().indexOf(pattern)
                if(index != -1){
                    matchedString.add(product.getName())
                    matchedProducts.add(product)

                    //build a map for easy sort
                    patternMap.add(IndexPattern(x, index))
                    x++
                }
            }
        }
        sortSearchOrder(patternMap, matchedString, matchedProducts)
        thread {
            Thread.sleep(100)
            ProductsServices.getListOfSearchedProducts(matchedProducts)
            //openProductsFragment(matchedProducts)
        }

    }

    private fun sortSearchOrder(patternMap : ArrayList<IndexPattern>, theListString : ArrayList<String>, theListProduct : ArrayList<Product>){

        //sort map
        val sortedPatternMap = patternMap.sortedBy { it.patternIndex }

        //build sorted list Strings and Products
        val sortedListString = ArrayList<String>()
        val sortedProducts = ArrayList<Product>()
        for(x in sortedPatternMap.indices){
            val newPlace = sortedPatternMap[x].index
            sortedListString.add( theListString[newPlace] )
            sortedProducts.add( theListProduct[newPlace] )
        }

        //copy the sorted list to the original list
        theListString.clear()
        theListProduct.clear()
        theListString.addAll(sortedListString)
        theListProduct.addAll(sortedProducts)
    }


}