package com.example.rabbitmqresearch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Delivery
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.nio.charset.Charset
import java.util.concurrent.TimeoutException


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rabbitTask = RabbitTask()

        GlobalScope.async {
            // launch new coroutine in background and continue
            delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
            println("World!") // print after delay
            rabbitTask.send()
        }
        println("Hello,") // main thread continues while coroutine is delayed
        Thread.sleep(2000L) // block main thread for 2 seconds to keep JVM alive



        sendButton.setOnClickListener {

            println("Button clicked")
            GlobalScope.async {
                println("Sending it!")
                rabbitTask.send()
                println("Sent it!")
            }
        }
        recieveButton.setOnClickListener {

            GlobalScope.async {
                rabbitTask.receive()
            }
        }
    }


}


