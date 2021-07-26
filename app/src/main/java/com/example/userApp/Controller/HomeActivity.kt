package com.example.userApp.Controller

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.userApp.R
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //full size screen
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_home)

        setUp()
    }

    private fun setUp(){

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        var dY = 0.0f

        homeImage.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event!!.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        dY = homeImage.y - event.rawY
                    }
                    MotionEvent.ACTION_MOVE -> {
                        homeImage.y = event.rawY + dY
                        if(homeImage.y > 0){
                            homeImage.y = 0.0f
                        }
                        //println("pos ${homeImage.y}")
                        //println("height $height")
                    }
                    MotionEvent.ACTION_UP ->{

                        val minUp = -400f
                        val velocity = 10f
                        val amount = 10f
                        if(homeImage.y < minUp){
                            smoothMauve(height.toFloat(), -velocity, -amount)
                            Handler().postDelayed({
                                startMainActivity()
                            }, 100)

                        }else{
                            smoothMauve(0f, velocity, amount)
                        }
                    }
                    else -> return false
                }
                return true
            }
        })
    }

    private fun smoothMauve(end : Float, velocityInit : Float, Amount : Float){


        val fact = if(velocityInit < 0f) -1 else 1
        var velocity = velocityInit


        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run() {

                println(fact * homeImage.y)
                println(velocity)
                if(fact * homeImage.y < end){
                    homeImage.y += velocity
                    velocity += Amount
                    mainHandler.postDelayed(this, 10)
                }else{
                    println("stop")
                    homeImage.y = end
                    mainHandler.removeCallbacksAndMessages(null)
                }
            }
        })
    }

    private fun startMainActivity(){
        onBackPressed()
    }



}
