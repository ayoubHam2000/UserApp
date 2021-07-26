package com.example.userApp.Fragments


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.userApp.Adapters.ReviewAdapter
import com.example.userApp.Controller.MainActivity

import com.example.userApp.R
import com.example.userApp.Services.GeneralServices
import com.example.userApp.Services.GeneralServicesF.CategoriesServices
import com.example.userApp.Services.GeneralServicesF.ProductsServices
import com.example.userApp.Services.OrderServices
import com.example.userApp.Services.PostCommandServices
import com.example.userApp.Services.Socket
import com.example.userApp.structur.orderStructur.ProductOrder
import com.example.userApp.utilities.UNIT_PRICE
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DecimalFormat


class OrderReviewFragment : Fragment() {

    lateinit var reviewAdapter : ReviewAdapter
    lateinit var globalContext : Context
    lateinit var theView : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        theView = inflater.inflate(R.layout.fragment_order_review, container, false)
        globalContext = container!!.context

        /*---------------------*/
        initFunction()
        /*----------------------*/

        return theView
    }

    private fun initFunction(){
        val totalPriceUnit = theView.findViewById<TextView>(R.id.totalPriceUnit)
        totalPriceUnit.text = UNIT_PRICE
        setUpReviewAdapter(OrderServices.listOfOrders)
        setUpButtons()
        refreshLocalPrice()
    }


    /*###################################################################*/
    /*###################################################################*/
    /*#########################--Adapter--###############################*/
    /*###################################################################*/
    /*###################################################################*/

    private fun setUpReviewAdapter(productOrders : ArrayList<ProductOrder>){
        reviewAdapter = ReviewAdapter(globalContext, productOrders){
            whenChangeOrder()
        }

        val reviewRecyclerView = theView.findViewById<RecyclerView>(R.id.CommadRecyclerView)
        val layoutManager = LinearLayoutManager(globalContext)

        reviewRecyclerView.adapter = reviewAdapter
        reviewRecyclerView.layoutManager = layoutManager
    }

    private fun whenChangeOrder(){
        GeneralServices.changeMainActivityPrice()
        refreshLocalPrice()
    }

    //-------------------------------------------

    private fun setUpButtons(){
        val commandButton = theView.findViewById<ImageView>(R.id.commadRequest)

        commandButton.setOnClickListener {
            commandRequest()
        }
    }

    private fun commandRequest(){
        println("request command")
        if(OrderServices.listOfOrders.size != 0){
            PostCommandServices.postCommand(globalContext, OrderServices.listOfOrders){success->
                if(success){

                    successSendCommand()
                }else{
                    makeMessage("failed to post command")
                }
            }
        }else{
            makeMessage("no commands")
        }

    }

    private fun successSendCommand(){
        Socket.sendSocket()
        makeMessage("success to post your commands")
        OrderServices.listOfOrders.clear()
        CategoriesServices.selectedCategory = 0
        ProductsServices.getListOfProducts(0)
        GeneralServices.changeMainActivityPrice()
        openProductFragment()
        GeneralServices.notifyCategoryRecyclerView()
        GeneralServices.notifyOrderReviewView()
    }

    private fun openProductFragment(){
        val productFragment = ProductsFragment()

        val fragmentTransaction = fragmentManager?.beginTransaction()?.replace(R.id.fragment, productFragment)
        fragmentTransaction?.commit()
    }

    private fun refreshLocalPrice(){
        val totalPrice = theView.findViewById<TextView>(R.id.totalPrice)
        val thePrice = OrderServices.getTotalPrice()

        val stringPrice = DecimalFormat("##.##").format(thePrice)
        totalPrice.text = stringPrice
    }

    private fun makeMessage(message : String){
        Toast.makeText(globalContext, message, Toast.LENGTH_LONG).show()
    }

}
