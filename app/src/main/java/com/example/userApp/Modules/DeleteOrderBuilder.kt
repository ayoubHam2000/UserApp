package com.example.userApp.Modules

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.userApp.Adapters.removeProductFromOrder.RemoveProductAdapter
import com.example.userApp.R
import com.example.userApp.Services.GeneralServices
import com.example.userApp.Services.OrderServices
import com.example.userApp.structur.orderStructur.ProductOrder
import java.util.*
import kotlin.collections.ArrayList

class DeleteOrderBuilder(val context : Context, val notifyProductAdapter : () -> Unit) {

    private lateinit var removeProduct : RemoveProductAdapter

     fun removeProductDialog(selectedProduct : ArrayList<ProductOrder>){


        val builder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.remove_product, null)
        builder.setView(dialogView)  //.setPositiveButton("ok", null)
        val dialog = builder.create()



        dialog.setOnShowListener {

            val productName = dialogView.findViewById<TextView>(R.id.productNameRemove)
            val productDetailRecyclerView = dialogView.findViewById<RecyclerView>(R.id.productDetailREcyclerView)
            val layoutManager = LinearLayoutManager(context)

            productName.text = selectedProduct[0].theProduct.getName().toUpperCase(Locale.FRENCH)
            removeProduct = RemoveProductAdapter(context, selectedProduct) { productOrderIndex, action ->
                when(action){
                    "scroll" ->{
                        smoothScroll(productDetailRecyclerView, productOrderIndex)
                    }
                    "delete" ->{
                        onItemClickInRemoveDialog(selectedProduct, productOrderIndex)
                        if(selectedProduct.size == 0)
                            dialog.dismiss()
                    }
                }

            }

            productDetailRecyclerView.adapter = removeProduct
            productDetailRecyclerView.layoutManager = layoutManager

        }

        dialog.setOnDismissListener {
            onDialogDismiss()
        }

        dialog.show()
        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    //this function is used in builder for smooth scroll in the recyclerView
    private fun smoothScroll(recyclerView: RecyclerView, position : Int){
        val layout = recyclerView.layoutManager!!
        val smoothScroller: RecyclerView.SmoothScroller = object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }

        smoothScroller.targetPosition = position
        layout.startSmoothScroll(smoothScroller)
    }

    private fun onItemClickInRemoveDialog(productOrders : ArrayList<ProductOrder>, productOrderIndex : Int){
        val theProduct = productOrders[productOrderIndex]

        //remove if the product remove completely it return true else count--
        val isRemoved = OrderServices.removeAnOrder(theProduct)
        if(isRemoved){
            productOrders.removeAt(productOrderIndex)
        }

        GeneralServices.changeMainActivityPrice()
        removeProduct.notifyDataSetChanged()
    }

    private fun onDialogDismiss(){
        notifyProductAdapter()
    }
}