package com.example.rabbitmqresearch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rabbitTask = RabbitTask()

        sendButton.setOnClickListener {
            GlobalScope.async {
                val message = sendMessageTextArea.text.toString()
                rabbitTask.send(message)
            }
        }

        recieveButton.setOnClickListener {
            GlobalScope.async {
                rabbitTask.receive()

            }
        }
    }


}


