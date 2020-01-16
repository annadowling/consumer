package com.msc.spring.consumer.java.client;/***************************************************************
 * Copyright (c) 2020 Errigal Inc.
 *
 * This software is the confidential and proprietary information
 * of Errigal, Inc.  You shall not disclose such confidential
 * information and shall use it only in accordance with the
 * license agreement you entered into with Errigal.
 *
 *************************************************************** */

import com.msc.spring.consumer.spring.amqp.RabbitMQProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by annadowling on 2020-01-16.
 */

public class RabbitMQSubscriber {

    @Autowired
    private static RabbitMQProperties rabbitMQProperties;

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitMQProperties.getHost());

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            consumeMessage(channel);
        }
    }

    public static void consumeMessage(Channel channel) throws Exception{
        channel.queueDeclare(rabbitMQProperties.getQueueName(), true, false, false, null);
        System.out.println(" [*] Waiting for messages.");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received Message: '" + message + "'");
        };
        channel.basicConsume(rabbitMQProperties.getQueueName(), rabbitMQProperties.getAutoAck(), deliverCallback, consumerTag -> { });

    }
}
