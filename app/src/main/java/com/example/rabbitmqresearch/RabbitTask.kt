package com.example.rabbitmqresearch

import androidx.annotation.WorkerThread
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Delivery
import java.io.IOException
import java.lang.Exception
import java.nio.charset.Charset
import java.util.concurrent.TimeoutException

class RabbitTask {
    private val QUEUE_NAME = "hello"

    @Throws(IOException::class, TimeoutException::class)
    fun receive() {
        val factory = ConnectionFactory()
        factory.setUri("amqp://vxvbbqxq:d55teJIJN8Kz2Y7s29hpzHzYgEHnpvk-@wombat.rmq.cloudamqp.com/vxvbbqxq")
//        factory.setHost("localhost")
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

    @Throws(IOException::class, TimeoutException::class)
    fun send() {
        try {
            println("Creating connection factory")
            val factory = ConnectionFactory()
            println("Finished creating connection factory")
            factory.isAutomaticRecoveryEnabled = false
            factory.setUri("amqp://vxvbbqxq:d55teJIJN8Kz2Y7s29hpzHzYgEHnpvk-@wombat.rmq.cloudamqp.com/vxvbbqxq")
//        factory.setHost("localhost")
            factory.newConnection().use({ connection ->
                connection.createChannel().use({ channel ->
                    channel.queueDeclare(QUEUE_NAME, false, false, false, null)
                    val message = "Lets test this one more time..........!"
                    channel.basicPublish("", QUEUE_NAME, null, message.toByteArray(charset("UTF-8")))
                    println(" [x] Sent '$message'")
                })
            })
        } catch (ex: Exception) {
            println("I caught an exception " + ex)
        }
    }
}