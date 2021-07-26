package com.example.userApp.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.userApp.Adapters.SearchAdapters.SearchCategoriesAdapter
import com.example.userApp.Adapters.restaurantAdapters.ProductsAdapter
import com.example.userApp.Modules.DeleteOrderBuilder

import com.example.userApp.R
import com.example.userApp.Services.GeneralServices
import com.example.userApp.Services.GeneralServicesF.CategoriesServices
import com.example.userApp.Services.OrderServices
import com.example.userApp.Services.GeneralServicesF.ProductsServices
import com.example.userApp.structur.other.SearchStruct
import com.example.userApp.structur.restaurantStructur.Product
import kotlin.collections.ArrayList


class ProductsFragment : Fragment() {

    private lateinit var searchAdapter : SearchCategoriesAdapter
    private lateinit var productsAdapter : ProductsAdapter
    private lateinit var globalContext : Context
    private lateinit var theView : View
    private lateinit var deleteOrderBuilder : DeleteOrderBuilder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        theView = inflater.inflate(R.layout.fragment_products, container, false)
        globalContext = container!!.context

        initFunction()

        return theView
    }

    private fun initFunction(){
        if(CategoriesServices.selectedCategory == -1){
            //search
            val searchList = ProductsServices.buildSearchStructList(ProductsServices.productList)
            setUpSearchRecyclerView(searchList)
        }else{
            setUpProductAdapter(ProductsServices.productList)
        }

        //class for delete builder when we want to delete orders
        setUpDeleteBuilder()
    }

    private fun setUpSearchRecyclerView(listOfSearchResult : ArrayList<SearchStruct>){
        val productsRecyclerView = theView.findViewById<RecyclerView>(R.id.productsRecyclerView)

        searchAdapter = SearchCategoriesAdapter(globalContext, listOfSearchResult) { event ->
                when (event) {
                    "addProductWithoutDetail" -> {
                        addProductToOrder()
                    }
                    "addProductWithDetail" -> {
                        openDetailFragment()
                    }
                    "removeProduct" -> {
                        removeProductSetting()
                    }
                }
            }

        val layoutManager = LinearLayoutManager(context)

        productsRecyclerView.adapter = searchAdapter
        productsRecyclerView.layoutManager = layoutManager

    }

    private fun setUpProductAdapter(products : ArrayList<Product>){
        val productsRecyclerView = theView.findViewById<RecyclerView>(R.id.productsRecyclerView)
        productsAdapter =
            ProductsAdapter(
                globalContext,
                products
            ) {event ->

                when (event) {
                    "addProductWithoutDetail" -> {
                        addProductToOrder()
                    }
                    "addProductWithDetail" -> {
                        openDetailFragment()
                    }
                    "removeProduct" -> {
                        removeProductSetting()
                    }
                }
            }

        val layoutManager = GridLayoutManager(globalContext, 2)

        productsRecyclerView.adapter = productsAdapter
        productsRecyclerView.layoutManager = layoutManager

    }

    private fun setUpDeleteBuilder(){
        deleteOrderBuilder = DeleteOrderBuilder(globalContext){
            notifyAdapter()
        }
    }

    /*###################################################################*/
    /*###################################################################*/
    /*###################--Add Product Section--#########################*/
    /*###################################################################*/
    /*###################################################################*/

    private fun openDetailFragment(){
        val detailFragment = DetailFragment()
        val fragmentTransaction = fragmentManager?.beginTransaction()?.replace(R.id.fragment, detailFragment)
        fragmentTransaction?.commit()
    }


    private fun addProductToOrder(){
        OrderServices.addAnOrder(ProductsServices.selectedProduct!!, 1)
        GeneralServices.changeMainActivityPrice()
        notifyAdapter()
    }

    /*###################################################################*/
    /*###################################################################*/
    /*#################--Remove Product Section--########################*/
    /*###################################################################*/
    /*###################################################################*/


    //if there is just one product we deleted it automatically
    //if not we open detail builder
    private fun removeProductSetting(){
        //select the product

        val orderedProductList = OrderServices.getOrderedProduct(ProductsServices.selectedProduct!!)
        if(orderedProductList.size == 1){
            OrderServices.removeAnOrder(orderedProductList[0])
            notifyAdapter()
            GeneralServices.changeMainActivityPrice()
        }else{
            deleteOrderBuilder.removeProductDialog(orderedProductList)
        }
    }


    private fun notifyAdapter(){
        if(CategoriesServices.selectedCategory == -1){
            searchAdapter.notifyDataSetChanged()
        }else{
            productsAdapter.notifyDataSetChanged()
        }
    }

}
