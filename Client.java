package org.example.demo;

import com.rabbitmq.client.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Client {
    private boolean isCapital;
    private boolean isReversed;
    private boolean isByte;
    private Connection connection;
    private Channel publishChannel, consumeChannel;

    @FXML
    VBox scrollBox;

    public Client(VBox scrollBox) {
        isCapital = true;
        isReversed = false;
        isByte = false;
        this.scrollBox = scrollBox;
        ConnectionFactory factory = new ConnectionFactory();
        try {
            this.connection = factory.newConnection();
            subscribe();
            this.publishChannel = connection.createChannel();
            this.publishChannel.exchangeDeclare("request", "direct");
        } catch (TimeoutException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void subscribe() {
        try {
            consumeChannel = connection.createChannel();
            consumeChannel.exchangeDeclare("response", "direct");
            String queueName = consumeChannel.queueDeclare().getQueue();

            if(isCapital) consumeChannel.queueBind(queueName, "response", "CL");
            if(isReversed) consumeChannel.queueBind(queueName, "response", "RV");
            if(isByte) consumeChannel.queueBind(queueName, "response", "BV");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                Platform.runLater(() -> MessengerController.addMessage(message, scrollBox));
            };

            consumeChannel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean getCapital() {
        return isCapital;
    }

    public boolean getReversed() {
        return isReversed;
    }

    public boolean getByte() {
        return isByte;
    }

    public void setCapital(boolean value) {
        isCapital = value;
        if(consumeChannel != null) {
            try {
                consumeChannel.close();
            } catch (TimeoutException | IOException e) {
                System.out.println(e.getMessage());
            }
        }
        subscribe();
    }

    public void setReversed(boolean value) {
        isReversed = value;
        if(consumeChannel != null) {
            try {
                consumeChannel.close();
            } catch (TimeoutException | IOException e) {
                System.out.println(e.getMessage());
            }
        }
        subscribe();
    }

    public void setByte(boolean value) {
        isByte = value;
        if(consumeChannel != null) {
            try {
                consumeChannel.close();
            } catch (TimeoutException | IOException e) {
                System.out.println(e.getMessage());
            }
        }
        subscribe();
    }

    public void sendMessage(String message, String topic) {
        try {
            publishChannel.basicPublish("request", topic, null, message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
