package com.example.userApp.Services

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Environment
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.example.userApp.Services.GeneralServicesF.RestaurantInfoServices
import java.io.File
import java.io.FileOutputStream


object RefreshDataServicies {

    fun downloadImages(context : Context){
        val categories = RestaurantInfoServices.restaurant.categories
        for(category in categories){
            val products = category.ListProducts
            for(product in products){
                val productId = product.id
                val theImage = context.getExternalFilesDir("productsImages/${productId}.jpg")
                if(!theImage!!.exists()){
                    performDownloadImage(context, "", productId)
                }
                /*else{
                    val originImageHash = MethodsHelpers.calculateMD5(theImage)
                    if(product.imageHash != originImageHash){
                        performDownloadImage(context)
                    }
                }*/
            }
        }
    }

    private fun performDownloadImage(context : Context, imageUrl : String, productId : String){
        Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                ) {
                    saveImageToStorage(context, resource, productId)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun saveImageToStorage(context : Context, bitmap: Bitmap, productId : String){
        val externalStorageStats = Environment.getExternalStorageState()
        if(externalStorageStats == Environment.MEDIA_MOUNTED){
            val storageDirectory = context.getExternalFilesDir("productsImages")
            val file = File(storageDirectory, "${productId}.jpg")
            try{
                val stream  = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
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