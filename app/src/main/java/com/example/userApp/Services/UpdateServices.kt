package com.example.userApp.Services

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.userApp.structur.restaurantStructur.Product
import com.example.userApp.utilities.*
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

object UpdateServices {

    val queueImageString = ArrayList<String>()


    fun getBitmapFromURLAndSaveIt(context : Context, src: String?, productId : String, imageCreatedTime : Long, complete : (Boolean) -> Unit) {
        try {
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            saveImageToStorage(context, BitmapFactory.decodeStream(input) , productId, imageCreatedTime)
            Log.d("Success", "Success to download image $productId")
            complete(true)
        } catch (e: IOException) {
            Log.d("FAILED", "failed to download image $productId")
            complete(false)
        }
    }

    private fun saveImageToStorage(context : Context, bitmap: Bitmap, productId : String, imageCreatedTime : Long){
        val name = "$productId-$imageCreatedTime"
        val externalStorageStats = Environment.getExternalStorageState()
        if(externalStorageStats == Environment.MEDIA_MOUNTED){
            val storageDirectory = context.getExternalFilesDir("productsImages")
            val file = File(storageDirectory, "${name}.jpg")
            try{
                val stream  = FileOutputStream(file)
                val dataSize = bitmap.rowBytes * bitmap.height
                when {
                    dataSize > HUGE_IMAGE -> {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, HUGE_COMPRESS, stream)
                    }
                    dataSize > BIG_IMAGE -> {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, HEIGHT_COMPRESS, stream)
                    }
                    else -> {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, LOW_COMPRESS, stream)
                    }
                }
                stream.flush()
                stream.close()
            }catch(e : Exception){
                Log.d("IError", "${e.printStackTrace()}")
            }
        }else{
            Log.d("IError", "Enable To Access The Storage")
        }
    }

}