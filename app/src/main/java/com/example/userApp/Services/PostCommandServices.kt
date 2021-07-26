package com.example.userApp.Services

import android.content.Context
import android.util.Log
import com.android.volley.Request.Method.POST
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.userApp.structur.orderStructur.ProductOrder
import com.example.userApp.structur.other.DetailOrdered
import com.example.userApp.structur.other.PropertyOrdered
import com.example.userApp.utilities.COMMAND_URL
import com.example.userApp.utilities.RESTAURANT_ID
import com.example.userApp.utilities.TABLE
import org.json.JSONArray
import org.json.JSONObject

object PostCommandServices {

    fun postCommand(context : Context, listProducts : ArrayList<ProductOrder>, complete : (Boolean) -> Unit){

        val url = "$COMMAND_URL$RESTAURANT_ID"
        val categoryJSON = makeJSONRequest(listProducts)
        val requestBody = categoryJSON.toString()

        val request = object : JsonObjectRequest(POST, url, null, Response.Listener {
            Log.d("SUCCESS", "success post commandInfo")
            complete(true)
        },Response.ErrorListener {error->
            Log.d("FAILED", "failed to post commandInfo $error")
            complete(false)
        }){
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }
        Volley.newRequestQueue(context).add(request)
    }

    private fun makeJSONRequest(listProducts : ArrayList<ProductOrder>) : JSONObject{
        val result = JSONObject()


        val theCommands = JSONArray()
        for(productOr in listProducts){
            val theCommand = JSONObject()
            theCommand.put("productId", productOr.theProduct.id)
            theCommand.put("category", productOr.theCategory.id)
            theCommand.put("productName", makeNameJSON(productOr.theProduct.name) )
            theCommand.put("productPrice", productOr.totalProductPrice())
            theCommand.put("productCount", productOr.count)
            theCommand.put("properties", makeJSONProperties(productOr.getProperties()))

            theCommands.put(theCommand)
        }
        result.put("table", TABLE)
        result.put("theCommands", theCommands)

        return result
    }

    private fun makeJSONProperties(listProperties : ArrayList<PropertyOrdered>) : JSONArray{
        val properties = JSONArray()

        for(item in listProperties){
            val property = JSONObject()
            property.put("propertyName", makeNameJSON(item.name) )
            property.put("details", makeDetailJSON(item.details) )

            properties.put(property)
        }

        return properties
    }

    private fun makeDetailJSON(listDetails : ArrayList<DetailOrdered>) : JSONArray{
        val details = JSONArray()

        for(item in listDetails){
            details.put(makeNameJSON(item.name))
        }

        return details
    }

    private fun makeNameJSON(name : ArrayList<String>) : JSONArray{
        val result = JSONArray()

        for(item in name){
            result.put(item)
        }
        return result
    }

}