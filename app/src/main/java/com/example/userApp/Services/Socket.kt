package com.example.userApp.Services

import com.example.userApp.utilities.RESTAURANT_ID
import com.example.userApp.utilities.URL
import com.github.nkzawa.socketio.client.IO
import java.net.URISyntaxException

object Socket {

    fun sendSocket(){
        try {

            //if you are using a phone device you should connect to same
            // local network as your laptop and disable your pubic firewall as well
            val socket = IO.socket(URL)

            //create connection
            socket.connect()

            // emit the event join along side with the nickname
            socket.emit("join", RESTAURANT_ID)
            println("sent success")


        }
        catch (e : URISyntaxException)
        {
            e.printStackTrace()

        }
    }
}