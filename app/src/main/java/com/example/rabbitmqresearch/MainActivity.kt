package com.example.rabbitmqresearch

import android.os.Bundle
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Delivery
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.nio.charset.Charset
import java.util.concurrent.TimeoutException


class MainActivity : AppCompatActivity() {
    private val QUEUE_NAME = "hello"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sendButton.setOnClickListener {
            GlobalScope.launch {
                send()
            }
        }
        recieveButton.setOnClickListener {
            GlobalScope.launch {
                receive()
            }
        }
    }


    @WorkerThread
    @Throws(IOException::class, TimeoutException::class)
    private suspend fun receive() {
        val factory = ConnectionFactory()
        val connection = factory.newConnection()
        val channel = connection.createChannel()

        channel.queueDeclare(QUEUE_NAME, false, false, false, null)
        println(" [*] Waiting for messages. To exit press CTRL+C")


        val deliverCallback = { consumerTag: String, delivery: Delivery ->
            val message = String(delivery.getBody(), Charset.forName("UTF-8"))
            println(" [x] Received '$message'")
        }
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, { consumerTag -> })
    }

    @WorkerThread
    @Throws(IOException::class, TimeoutException::class)
    private suspend fun send() {
        var factory = ConnectionFactory()
        try {
            factory.setUri("amqp://vxvbbqxq:d55teJIJN8Kz2Y7s29hpzHzYgEHnpvk-@wombat.rmq.cloudamqp.com/vxvbbqxq")
            factory.newConnection().use({ connection ->
                connection.createChannel().use({ channel ->
                    channel.queueDeclare(QUEUE_NAME, false, false, false, null)
                    val message = "Lets test this one more time..........!"
                    channel.basicPublish("", QUEUE_NAME, null, message.toByteArray(charset("UTF-8")))
                    println(" [x] Sent '$message'")
                })
            })
        } catch (exception: ClassCastException) {
            println("Suh dude: " + exception)
        }
    }
}


