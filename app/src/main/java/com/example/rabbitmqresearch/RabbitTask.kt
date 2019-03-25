package com.example.rabbitmqresearch

import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Delivery
import java.io.IOException
import java.nio.charset.Charset
import java.util.concurrent.TimeoutException

class RabbitTask {
    private val QUEUE_NAME = "hello"
    val factory = ConnectionFactory()

    init {
        factory.setHost("localhost")
//        factory.setUri("amqp://vxvbbqxq:d55teJIJN8Kz2Y7s29hpzHzYgEHnpvk-@wombat.rmq.cloudamqp.com/vxvbbqxq")
//        factory.setPort(1883)
    }

    @Throws(IOException::class, TimeoutException::class)
    fun receive() {
        val connection = factory.newConnection()
        val channel = connection.createChannel()

        channel.queueDeclare(QUEUE_NAME, false, false, false, null)
        val deliverCallback = { consumerTag: String, delivery: Delivery ->
            val message = String(delivery.getBody(), Charset.forName("UTF-8"))
            println(" [x] Received '$message'")
        }

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, { consumerTag -> })
    }

    @Throws(IOException::class, TimeoutException::class)
    fun send(message: String) {
        factory.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                channel.queueDeclare(QUEUE_NAME, false, false, false, null)
                channel.basicPublish("", QUEUE_NAME, null, message.toByteArray(charset("UTF-8")))
                println(" [x] Sent '$message'")

            }
        }
    }
}