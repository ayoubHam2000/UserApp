package com.example.userApp.Fragments


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.example.userApp.Adapters.AddProductToOrder.DetailReviewAdapter
import com.example.userApp.Adapters.AddProductToOrder.PropertiesAdapter
import com.example.userApp.R
import com.example.userApp.Services.GeneralServices
import com.example.userApp.Services.GeneralServicesF.CategoriesServices
import com.example.userApp.Services.OrderServices
import com.example.userApp.Services.GeneralServicesF.ProductsServices
import com.example.userApp.structur.restaurantStructur.Product
import com.example.userApp.structur.restaurantStructur.Property
import com.example.userApp.utilities.UNIT_PRICE
import java.text.DecimalFormat


class DetailFragment : Fragment() {

    private var demandCount = 1
    private lateinit var product : Product

    private lateinit var propertiesAdapter : PropertiesAdapter
    private lateinit var detailReviewAdapter : DetailReviewAdapter
    private lateinit var propertiesRecyclerView : RecyclerView
    private lateinit var theView : View
    private lateinit var globalContext : Context

    private val detailReview = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        theView = inflater.inflate(R.layout.fragment_detail, container, false)
        globalContext = container!!.context

        initFunctions()

        return theView
    }

    /*###################################################################*/
    /*###################################################################*/
    /*#########################--Set Up--################################*/
    /*###################################################################*/
    /*###################################################################*/

    private fun initFunctions(){


        if(ProductsServices.selectedProduct != null){
            product = ProductsServices.selectedProduct!!
            //reset all properties to show the original properties
            propertiesReset(product.properties)

            //than open properties recyclerView
            openPropertiesAdapter(product.properties)

            //than open detail recyclerView
            openDetailReviewAdapter()

        }else{
            println("error product is null")
        }



        setUp()
    }

    private fun propertiesReset(properties : ArrayList<Property>){
        for(item in properties){
            item.propertyReset()
        }
    }

    //every click it call refreshFun()
    private fun openPropertiesAdapter(properties : ArrayList<Property>){
        propertiesAdapter = PropertiesAdapter(globalContext, properties) {
                refreshFun()
        }

        val layoutManager = LinearLayoutManager(globalContext)

        propertiesRecyclerView = theView.findViewById(R.id.propertiesRecyclerView)

        propertiesRecyclerView.adapter = propertiesAdapter
        propertiesRecyclerView.layoutManager = layoutManager
        propertiesRecyclerView.setHasFixedSize(true)
        propertiesRecyclerView.setItemViewCacheSize(20)

    }

    private fun openDetailReviewAdapter(){
        makeDetailReviewList()
        detailReviewAdapter = DetailReviewAdapter(globalContext, detailReview)

        val layoutManager = LinearLayoutManager(globalContext)

        val detailResultRecyclerView = theView.findViewById<RecyclerView>(R.id.detailResultRecyclerView)

        detailResultRecyclerView.adapter = detailReviewAdapter
        detailResultRecyclerView.layoutManager = layoutManager
    }

    private fun setUp(){
        //just set the product name
        setProductName()

        //just change the total Product Price
        refreshPrice()

        //just change the color of the button
        refreshButton()

        //set up buttons
        setUpButtons()
    }

        private fun setProductName(){
            val productNameDetailHead = theView.findViewById<TextView>(R.id.productNameDetailHead)

            productNameDetailHead.text = product.getName()
        }

        @SuppressLint("SetTextI18n")
        private fun refreshPrice(){
            var price = 0.0

            for(item in product.properties){
                for(detail in item.details)
                    if(detail.isCheck){
                        price += detail.price
                    }
            }

            val detailTotalPrice = theView.findViewById<TextView>(R.id.detailTotalPrice)

            val sPrice = DecimalFormat("##.##").format(price)
            detailTotalPrice.text = "+${sPrice}${UNIT_PRICE}"
        }

        private fun refreshButton(){
            val commitCommandButton = theView.findViewById<Button>(R.id.addToCommandButton)

            if( checkPropertyValidity(false) == -1){
                commitCommandButton.setBackgroundResource(R.drawable.button_active)
            }else{
                commitCommandButton.setBackgroundResource(R.drawable.button_un_active)
            }
        }

        private fun setUpButtons(){
            val commitCommandButton = theView.findViewById<Button>(R.id.addToCommandButton)
            val addButton = theView.findViewById<ImageView>(R.id.addButton)
            val removeButton = theView.findViewById<ImageView>(R.id.removeButton)

            commitCommandButton.setOnClickListener {
                commitCommand()
            }

            addButton.setOnClickListener {
                demandCountFunction(1)
            }

            removeButton.setOnClickListener {
                demandCountFunction(-1)
            }
        }

    private fun demandCountFunction(delta : Int){
        val demandCountTextView = theView.findViewById<TextView>(R.id.demandCount)

        if(demandCount + delta != 0){
            demandCount += delta
            demandCountTextView.text = demandCount.toString()
        }
    }

    /*###################################################################*/
    /*###################################################################*/
    /*########################--commitCommand--##########################*/
    /*###################################################################*/
    /*###################################################################*/

    private fun commitCommand(){
        val isValid = checkValidityOfCommand()
        if(isValid){
            OrderServices.addAnOrder(product, demandCount)
            GeneralServices.changeMainActivityPrice()
            openProductFragment()
        }
    }

    private fun checkValidityOfCommand() : Boolean{
        val commitCommandButton = theView.findViewById<Button>(R.id.addToCommandButton)
        val isValid = checkPropertyValidity(true)
        val animShake = AnimationUtils.loadAnimation(globalContext, R.anim.shake)
        val layout = propertiesRecyclerView.layoutManager!!
        val smoothScroller: SmoothScroller = object : LinearSmoothScroller(globalContext) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }

        return if(isValid != -1){

            smoothScroller.targetPosition = isValid
            layout.startSmoothScroll(smoothScroller)

            commitCommandButton.startAnimation(animShake)
            Handler().postDelayed({
                val view = layout.findViewByPosition(isValid)
                view?.startAnimation(animShake)
            }, 100)
            false
        }else{
            true
        }

    }

    private fun checkPropertyValidity(displayMessage : Boolean) : Int{
        for(x in 0 until  product.properties.size){
            val item = product.properties[x]
            if(!item.validity){
                if(displayMessage){
                    val sentence = resources.getString(R.string.sentence)
                    val element = resources.getString(R.string.elements)
                    makeMessage("${item.getName()} $sentence ${item.min} $element")
                }
                return x
            }
        }
        return -1
    }



    //-----------------------------------------------------------------
    //refresh when check or uncheck detail (button, local price, detail checked list)
    private fun refreshFun(){

        makeDetailReviewList()
        detailReviewAdapter.notifyDataSetChanged()
        refreshPrice()
        refreshButton()

    }


    private fun makeDetailReviewList(){

        detailReview.clear()
        for(item in product.properties){
            val propertyName = item.getName()

            for(detail in item.details){
                if(detail.isCheck){
                    detailReview.add("$propertyName : ${detail.getName()}")
                }
            }
        }
    }


    //---------------------------------------------------------------
    private fun openProductFragment(){
        if(CategoriesServices.selectedCategory == -1){
            GeneralServices.searchField.setText(GeneralServices.searchField.text)
            GeneralServices.searchField.dismissDropDown()
        }else{
            val productFragment = ProductsFragment()

            val fragmentTransaction = fragmentManager?.beginTransaction()?.replace(R.id.fragment, productFragment)
            fragmentTransaction?.commit()
        }

    }

    private fun makeMessage(message : String){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

}
