package com.msc.spring.consumer.java.client;/***************************************************************
 * Copyright (c) 2020 Errigal Inc.
 *
 * This software is the confidential and proprietary information
 * of Errigal, Inc.  You shall not disclose such confidential
 * information and shall use it only in accordance with the
 * license agreement you entered into with Errigal.
 *
 *************************************************************** */

import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by annadowling on 2020-01-16.
 */

@Component
public class RabbitMQSubscriber {

    @Value("${rabbitmq.queueName}")
    private String queueName;

    @Value("${rabbitmq.host}")
    private String host;

    @Value("${rabbitmq.port}")
    private Integer port;

    @Value("${rabbitmq.username}")
    private String rabbitUserName;

    @Value("${rabbitmq.password}")
    private String rabbitPassWord;

    @Value("${rabbitmq.virtualhost}")
    private String virtualHost;

    @Value("${rabbitmq.exchangeName}")
    private String exchangeName;

    @Value("${rabbitmq.routingKey}")
    private String routingKey;

    @Value("${rabbitmq.autoAck}")
    private static boolean autoAck;

    @Value("${rabbitmq.java.client.enabled}")
    private static boolean rabbitJavaClientEnabled;

    @Bean
    public void consumeMessagefromRabbitJavaClient() throws Exception {

        if (rabbitJavaClientEnabled) {
            try {
                Channel channel = createChannelConnection();

                System.out.println(" [*] Waiting for messages.");
                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), "UTF-8");
                    System.out.println(" [x] Received Message: '" + message + "'");
                };
                channel.basicConsume(queueName, autoAck, deliverCallback, consumerTag -> {
                });
            } catch (IOException e) {
                System.out.println("IOException encountered = " + e.getLocalizedMessage());
            }
        }
    }

    Channel createChannelConnection() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPassword(rabbitPassWord);
        factory.setUsername(rabbitUserName);
        factory.setPort(port);
        factory.setVirtualHost(virtualHost);
        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare(queueName, true, false, false, null);
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);
        channel.queueBind(queueName, exchangeName, routingKey);

        return channel;
    }
}
