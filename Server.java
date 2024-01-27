package org.example.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Server {
    public static void main(String[] args) throws TimeoutException, IOException {

        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare("request", "direct");
        channel.exchangeDeclare("response", "direct");

        String queueName = channel.queueDeclare().getQueue();

        channel.queueBind(queueName, "request", "CL");
        channel.queueBind(queueName, "request", "RV");
        channel.queueBind(queueName, "request", "BV");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String topic = delivery.getEnvelope().getRoutingKey();
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            switch (topic) {
                case "CL" -> message = message.toUpperCase();
                case "RV" -> message = new StringBuilder(message).reverse().toString();
                case "BV" -> {
                    StringBuilder sb = new StringBuilder();
                    for(byte value : message.getBytes()) {
                        sb.append(value).append(" ");
                    }
                    message = sb.toString().trim();
                }
            }
            channel.basicPublish("response", topic, null, message.getBytes(StandardCharsets.UTF_8));
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });

    }
}
