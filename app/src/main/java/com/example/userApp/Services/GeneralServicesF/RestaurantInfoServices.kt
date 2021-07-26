package com.example.userApp.Services.GeneralServicesF

import android.content.Context
import android.util.Log
import com.android.volley.Request.Method.GET
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.userApp.structur.restaurantStructur.*
import com.example.userApp.utilities.RESTAURANT_ID
import com.example.userApp.utilities.RESTAURANT_URL
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object RestaurantInfoServices {

    lateinit var restaurant : RestaurantInfo

    fun getRestaurantInfo(context : Context, complete : (Boolean) -> Unit){

        val url = "${RESTAURANT_URL}${RESTAURANT_ID}/getAllData"

        val request = JsonObjectRequest(GET, url, null, Response.Listener {response ->

            try{
                val resOb = response.getJSONObject("restaurant")
                makeRestaurant(
                    resOb
                )
                Log.d("success", "Success get restaurant Information")
                complete(true)
            }catch(e : JSONException){
                Log.d("Failed", "Failed to get restaurant Information $e")
                complete(false)
            }

        }, Response.ErrorListener { error ->
            Log.d("Json Error", "Failed to restaurant information --> ${error.localizedMessage}")
            complete(false)
        })

        Volley.newRequestQueue(context).add(request)
    }

    private fun makeRestaurant(response : JSONObject){

        val restaurantName = response.getString("name")

        val jsonCategories = response.getJSONArray("categories")
        val restaurantCategories =
            makeCategoriesList(
                jsonCategories
            )

        restaurant =
            RestaurantInfo(
                restaurantName,
                restaurantCategories
            )
        loadCategoryList()
        println("success get restaurant ...")

    }

    private fun loadCategoryList(){
        CategoriesServices.categoryList.addAll(restaurant.categories)
        CategoriesServices.selectedCategory = 0
    }

    private fun makeCategoriesList(categories : JSONArray) : ArrayList<Category> {
        val categoriesList = ArrayList<Category>()

        for(x in 0 until categories.length()){
            val category = categories.getJSONObject(x)

            val categoryName =
                makeName(
                    category.getJSONArray("name")
                )
            val listProduct =
                makeProductsList(
                    category.getJSONArray("products"),
                    x
                )
            val id = category.getString("_id")

            val newCategory =
                Category(
                    categoryName,
                    listProduct,
                    id
                )
            categoriesList.add(newCategory)
        }

        //println("success get category ...")
        return categoriesList
    }

    private fun makeProductsList(productsList : JSONArray, categoryIndex : Int) : ArrayList<Product> {
        val products = ArrayList<Product>()

        for(x in 0 until productsList.length() ){
            val product = productsList.getJSONObject(x)

            val name =
                makeName(
                    product.getJSONArray("name")
                )
            val price = product.getDouble("price")
            val discount = product.getDouble("discount")
            val properties =
                makePropertiesList(
                    product.getJSONArray("properties")
                )
            val details =
                makeName(
                    product.getJSONArray("details")
                )
            val id = product.getString("_id")
            val isImageModified = product.getLong("imageCreatedTime")
            val imageUrl = product.getString("imageUrl")

            val newProduct =
                Product(
                    name,
                    price,
                    discount,
                    properties,
                    details,
                    id,
                    categoryIndex,
                    x,
                    isImageModified,
                    imageUrl
                )
            products.add(newProduct)
        }

        println("success get product ...")
        return products
    }

    private fun makePropertiesList(propertiesList : JSONArray) : ArrayList<Property> {
        val properties = ArrayList<Property>()

        for(x in 0 until propertiesList.length()){
            val property = propertiesList.getJSONObject(x)

            val name =
                makeName(
                    property.getJSONArray("name")
                )
            val require = property.getBoolean("require")
            val min = property.getInt("min")
            val max = property.getInt("max")
            val details =
                makeDetailsList(
                    property.getJSONArray("details")
                )
            val id = property.getString("_id")
            val detailCheck =
                detailCheckedCount(
                    details
                )
            val validity = if(require){ detailCheck in min..max } else { true }

            val newProperty =
                Property(
                    name,
                    require,
                    min,
                    max,
                    details,
                    id,
                    detailCheck,
                    validity
                )
            properties.add(newProperty)
        }

        //println("success get properties ...")
        return properties
    }

    private fun detailCheckedCount(details : ArrayList<Detail>) : Int{
        var i = 0

        for(item in details){
            if(item.check){ i++ }
        }
        return i
    }

    private fun makeDetailsList(detailsList : JSONArray) : ArrayList<Detail> {
        val details = ArrayList<Detail>()

        for(x in 0 until detailsList.length()){
            val detailJson = detailsList.getJSONArray(x)

            val detailName = arrayListOf(detailJson.getString(0), detailJson.getString(1))
            val checked = detailJson.getBoolean(2)
            val price = detailJson.getDouble(3)

            val detail = Detail(
                detailName,
                checked,
                price,
                checked
            )
            details.add(detail)
        }
        return details
    }

    private fun makeName(languagesName : JSONArray) : ArrayList<String>{
        val names = ArrayList<String>()

        for(x in 0 until languagesName.length()){
            names.add(languagesName.getString(x))
        }

        return names
    }
}