package com.example.userApp.Adapters.restaurantAdapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.userApp.R
import com.example.userApp.Services.OrderServices
import com.example.userApp.Services.UpdateServices
import com.example.userApp.Services.GeneralServicesF.ProductsServices
import com.example.userApp.structur.restaurantStructur.Product
import com.example.userApp.utilities.DISCOUNT
import com.example.userApp.utilities.NO_DISCOUNT
import com.example.userApp.utilities.UNIT_PRICE
import java.io.File
import java.text.DecimalFormat
import kotlin.concurrent.thread


class ProductsAdapter(val context : Context, val products : ArrayList<Product>, val clickEvent : (String) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewWithoutDiscount = LayoutInflater.from(context).inflate(R.layout.product_without_discount_recycler_view, parent, false)
        val viewDiscount = LayoutInflater.from(context).inflate(R.layout.product_with_discount_recycler_view, parent, false)

        return if(viewType == NO_DISCOUNT){
            ViewHolderWithOutDiscount(viewWithoutDiscount)
        }else{
            ViewHolderWithDiscount(viewDiscount)
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(products[position].discount == 0.0){
            NO_DISCOUNT
        }else{
            DISCOUNT
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder.itemViewType == NO_DISCOUNT){
            (holder as ViewHolderWithOutDiscount).bindView(position)
        }else{
            (holder as ViewHolderWithDiscount).bindView(position)
        }
    }

    inner class ViewHolderWithOutDiscount(itemView : View?) : RecyclerView.ViewHolder(itemView!!){

        private val productName = itemView?.findViewById<TextView>(R.id.productName)
        private val productPrice = itemView?.findViewById<TextView>(R.id.productPrice)
        private val productPriceUnit = itemView?.findViewById<TextView>(R.id.productPriceUnit)
        private val productDetail = itemView?.findViewById<TextView>(R.id.productDetail)
        private val productNumber = itemView?.findViewById<TextView>(R.id.productNumbers)
        private val productAddButton = itemView?.findViewById<ImageView>(R.id.productAdd)
        private val productRemoveButton = itemView?.findViewById<ImageView>(R.id.productRemove)
        private val productImage = itemView?.findViewById<ImageView>(R.id.productImage)


        fun bindView(position : Int){

            val price = products[position].price
            val productCount = OrderServices.getProductNumber(products[position].categoryIndex, products[position].productIndex)

            productName?.text = products[position].getName()
            productPriceUnit?.text = UNIT_PRICE
            productDetail?.text = products[position].getDetails()
            productPrice?.text = DecimalFormat("##.##").format(price)

            //functions
            productNumber?.text = productCount.toString()
            productAddButton?.setOnClickListener {
                addButtonFunction(position)
            }
            productRemoveButton?.setOnClickListener {
                deleteButtonFunction(position)
            }

            if(productCount == 0){
                productRemoveButton?.visibility = View.INVISIBLE
            }else{
                productRemoveButton?.visibility = View.VISIBLE
            }

            setImageFromUrl(productImage, products[position])

        }

    }

    inner class ViewHolderWithDiscount(itemView : View?) : RecyclerView.ViewHolder(itemView!!){

        private val productOldPrice = itemView?.findViewById<TextView>(R.id.productOldPrice)
        private val productDiscount = itemView?.findViewById<TextView>(R.id.productDiscount)

        private val productName = itemView?.findViewById<TextView>(R.id.productName)
        private val productPrice = itemView?.findViewById<TextView>(R.id.productPrice)
        private val productPriceUnit = itemView?.findViewById<TextView>(R.id.productPriceUnit)
        private val productDetail = itemView?.findViewById<TextView>(R.id.productDetail)
        private val productNumber = itemView?.findViewById<TextView>(R.id.productNumbers)
        private val productAddButton = itemView?.findViewById<ImageView>(R.id.productAdd)
        private val productRemoveButton = itemView?.findViewById<ImageView>(R.id.productRemove)
        private val productImage = itemView?.findViewById<ImageView>(R.id.productImage)

        @SuppressLint("SetTextI18n")
        fun bindView(position : Int){

            val price = products[position].price
            val newPrice = products[position].price * (1 - products[position].discount / 100)
            val productCount = OrderServices.getProductNumber(products[position].categoryIndex, products[position].productIndex)

            productName?.text = products[position].getName()
            productPriceUnit?.text = UNIT_PRICE
            productDetail?.text = products[position].getDetails()
            productPrice?.text = DecimalFormat("##.##").format(newPrice)

            //discount
            productDiscount?.text = "${products[position].discount} %"
            productOldPrice?.text = "${DecimalFormat("##.##").format(price)} $UNIT_PRICE"

            //functions
            productNumber?.text = productCount.toString()

            productAddButton?.setOnClickListener {
                addButtonFunction(position)

            }
            productRemoveButton?.setOnClickListener {
                deleteButtonFunction(position)
            }

            if(productCount == 0){
                productRemoveButton?.visibility = View.INVISIBLE
            }else{
                productRemoveButton?.visibility = View.VISIBLE
            }

            setImageFromUrl(productImage, products[position])
        }
    }

    private fun addButtonFunction(position : Int){
        if(products[position].properties.size == 0){
            addProductWithoutDetail(position)
        }else{
            addProductWithDetail(position)
        }
    }

    //delete programme
    private fun deleteButtonFunction(position : Int){
        ProductsServices.selectedProduct = products[position]
        clickEvent("removeProduct")
    }

    //add programme
    private fun addProductWithDetail(position: Int) {
        ProductsServices.selectedProduct = products[position]
        clickEvent("addProductWithDetail")
    }

    private fun addProductWithoutDetail(position: Int){
        ProductsServices.selectedProduct = products[position]
        clickEvent("addProductWithoutDetail")
    }

    fun setImageFromUrl(imageView : ImageView?, theProduct: Product){
        val name = "${theProduct.id}-${theProduct.imageCreatedTime}"
        val url = context.getExternalFilesDir("productsImages/${name}.jpg").toString()
        val file = File(url)
        if(!file.isDirectory){
            val bitmap = BitmapFactory.decodeFile(url)
            val drawable: Drawable = BitmapDrawable(context.resources, bitmap)
            imageView?.background = drawable
        }else{
            Log.d("NOTFOUND", "IMAGE NOT FOUNT $name")
            file.delete()
            if(theProduct.imageURl.isNotEmpty() && isImageNotStartedDownload(name)){
                thread{
                    UpdateServices.queueImageString.add(name)
                    UpdateServices.getBitmapFromURLAndSaveIt(context, theProduct.imageURl, theProduct.id, theProduct.imageCreatedTime){success->
                        if(success){
                            UpdateServices.queueImageString.remove(name)
                            val handler = Handler(Looper.getMainLooper())
                            handler.postDelayed({
                                notifyDataSetChanged()
                            }, 10)
                        }
                    }
                }
            }else if(theProduct.imageURl.isNotEmpty()){
                println("image not in the server")
            }
        }
    }

    private fun isImageNotStartedDownload(imageName : String) : Boolean{
        for(name in UpdateServices.queueImageString){
            if(name == imageName){
                return false
            }
        }
        return true
    }

}