package com.msc.spring.consumer.java.client;/***************************************************************
 * Copyright (c) 2020 Errigal Inc.
 *
 * This software is the confidential and proprietary information
 * of Errigal, Inc.  You shall not disclose such confidential
 * information and shall use it only in accordance with the
 * license agreement you entered into with Errigal.
 *
 *************************************************************** */

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by annadowling on 2020-01-16.
 */

@Component
public class RabbitMQSubscriber {

    @Value("${rabbitmq.queueName}")
    private static String queueName;

    @Value("${spring.rabbitmq.host}")
    private static String host;

    @Value("${rabbitmq.exchangeName}")
    private static String exchangeName;

    @Value("${rabbitmq.autoAck}")
    private static boolean autoAck;

    @Value("${rabbitmq.java.client.enabled}")
    private static boolean rabbitJavaClientEnabled;

    @Bean
    public static void consumeMessagefromRabbitJavaClient() throws Exception {
        if (rabbitJavaClientEnabled) {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);

            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()
            ) {

                channel.queueDeclare(queueName, true, false, false, null);
                System.out.println(" [*] Waiting for messages.");

                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), "UTF-8");
                    System.out.println(" [x] Received Message: '" + message + "'");
                };
                channel.basicConsume(queueName, autoAck, deliverCallback, consumerTag -> {
                });
            }
        }

    }
}
