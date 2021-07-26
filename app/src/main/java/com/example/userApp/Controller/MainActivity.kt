package com.example.userApp.Controller

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userApp.Adapters.restaurantAdapters.CategoriesAdapter
import com.example.userApp.Fragments.OrderReviewFragment
import com.example.userApp.Fragments.ProductsFragment
import com.example.userApp.Modules.LanguageDisplay
import com.example.userApp.R
import com.example.userApp.Services.GeneralServices
import com.example.userApp.Services.GeneralServicesF.CategoriesServices
import com.example.userApp.Services.GeneralServicesF.ProductsServices
import com.example.userApp.Services.GeneralServicesF.RestaurantInfoServices
import com.example.userApp.Services.OrderServices
import com.example.userApp.healper.SearchServices
import com.example.userApp.healper.localeLanguageChanger
import com.example.userApp.utilities.*
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity(){

    private lateinit var categoriesAdapter : CategoriesAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //full size screen
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        //initialize sharedPreferences in onCreate method after setContent
        sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this)

        initFunction()

    }

    private fun initFunction(){
        //getRestaurantInformation
        if(!INFOREADY){
            getInformation()
        }else{
            successGetInformation()
        }


        //content
        setUpContent()

        //buttons
        setUpButtons()


        val socket = IO.socket(URL)
        socket.connect()
        socket.on("updateTablet", onNewChannel )
    }



    private fun setUpContent(){
        GeneralServices.mainActivityPrice = totalPriceCommand
        GeneralServices.searchField = searchView
        GeneralServices.categoryRecyclerView = categoriesRecyclerView
        GeneralServices.orderReviewView = orderReviewButton
        restaurantName.text = RESTAURANT_NAME
    }

    private fun setUpButtons(){

        orderReviewButton.setOnClickListener {
            CategoriesServices.selectedCategory = -1
            categoriesAdapter.notifyDataSetChanged()
            openOrderReviewFragment()
            it.setBackgroundResource(R.color.color1)
        }

        val lan = LanguageDisplay(this, sharedPreferences){
            clearInfoForExit()
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        }

        languageChanger.setOnClickListener {
            lan.languageManagement()
        }

        exitButton.setOnClickListener {
            exitToHomePage()
        }


    }

    //feature
    private val onNewChannel = Emitter.Listener {args ->
        //args[0] is tablet ID
        if(args[0] == "123"){
            println("start update ....")
            /*UpdateServices.getInformation(this){success->
                if(success){
                    categoriesAdapter.notifyDataSetChanged()
                    val category = RestaurantInfoServices.restaurant.categories[0]
                    openProductsFragment(category.ListProducts)
                }
            }*/
        }
    }

    /*###################################################################*/
    /*###################################################################*/
    /*####################--getInformation--#############################*/
    /*###################################################################*/
    /*###################################################################*/

    //use one function if the get is success --->successGetInformation
    //when get information success the restaurant has bean leaded and also the categories
    //to CategoriesServices and selected category is automatically selected to 0
    private fun getInformation(){
        if(RESTAURANT_ID != ""){
            RestaurantInfoServices.getRestaurantInfo(this){ success ->
                if(success){
                    Log.d("Success", "success to get restaurant information")
                    successGetInformation()
                    INFOREADY = true
                }else{
                    Log.d("Failed", "failed to get restaurant information")
                }
            }
        }else{
            Log.d("MISS", "Missing restaurant")
        }
    }

    private fun successGetInformation(){
        setUpCategoriesAdapter()

        //set up search class
        setUpSearch()

        openProductsFragment()
    }

    private fun setUpSearch(){
        val searchClass = SearchServices(this, searchView){action ->
            if(CategoriesServices.selectedCategory != -1){
                CategoriesServices.selectedCategory = -1
                categoriesAdapter.notifyDataSetChanged()
            }
            thread{
                Thread.sleep(700)
                if(action == SEARCHTAP){
                    println("receive is $action")
                    openProductsFragment()
                    SEARCHTAP = 0
                }
            }

        }
        searchClass.startSearch()

        searchView.setOnItemClickListener { _, view, _, _ ->
            val inp = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inp.hideSoftInputFromWindow(view.applicationWindowToken, 0)
        }
    }

    /*###################################################################*/
    /*###################################################################*/
    /*#####################--  Adapter    --#############################*/
    /*###################################################################*/
    /*###################################################################*/

    //use one function whenCategoryClicked
    private fun setUpCategoriesAdapter(){
        categoriesAdapter = CategoriesAdapter(this, CategoriesServices.categoryList) {
            whenCategoryClicked()
        }
        val layoutManager = LinearLayoutManager(this)

        if(ProductsServices.productList.size == 0 && CategoriesServices.categoryList.size > 0){
            ProductsServices.getListOfProducts(0)
        }
        categoriesRecyclerView.adapter = categoriesAdapter
        categoriesRecyclerView.layoutManager = layoutManager

    }

    private fun whenCategoryClicked(){
        //selected category change in the adapter
        ProductsServices.getListOfProducts(CategoriesServices.selectedCategory)
        openProductsFragment()
        //clear focus of the autoSearchTextView
        currentFocus?.clearFocus()
    }


    /*###################################################################*/
    /*###################################################################*/
    /*########################--Fragment--###############################*/
    /*###################################################################*/
    /*###################################################################*/

    private fun openProductsFragment(){
        orderReviewButton.setBackgroundResource(R.color.transparent)
        val ft = supportFragmentManager.beginTransaction()

        val productFragment = ProductsFragment()

        ft.replace(R.id.fragment , productFragment)
        ft.commit()
    }


    private fun openOrderReviewFragment(){
        val ft = supportFragmentManager.beginTransaction()

        val orderReview = OrderReviewFragment()

        ft.replace(R.id.fragment , orderReview)
        ft.commit()
    }


    //-------------------------------------------------------------------

    private fun makeMessage(message : String){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    //about exit
    override fun onBackPressed() {
        super.onBackPressed()
        exitToHomePage()
    }

    private fun exitToHomePage(){
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
        clearInfoForExit()
        finish()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun clearInfoForExit(){
        searchView.text.clear()
        hideKeyboard()
        currentFocus?.clearFocus()
        OrderServices.listOfOrders.clear()
        ProductsServices.getListOfProducts(0)
        CategoriesServices.selectedCategory = 0

    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    //about language
    override fun attachBaseContext(newBase: Context?) {

        sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(newBase)
        val lang = sharedPreferences.getString(SELECTED_LANGUAGE, "fr")
        super.attachBaseContext(localeLanguageChanger.wrap(newBase!!, lang!!))
    }

    override fun onStop() {
        clearInfoForExit()
        super.onStop()
    }
}


