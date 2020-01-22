package com.msc.spring.consumer.spring.amqp;/***************************************************************
 * Copyright (c) 2020 Errigal Inc.
 *
 * This software is the confidential and proprietary information
 * of Errigal, Inc.  You shall not disclose such confidential
 * information and shall use it only in accordance with the
 * license agreement you entered into with Errigal.
 *
 *************************************************************** */

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by annadowling on 2020-01-22.
 */

@Configuration
public class SpringAMQPConfig {

    @Value("${rabbitmq.exchangeName}")
    private String exchangeName;

    @Value("${rabbitmq.queueName}")
    private String queueName;

    @Value("${rabbitmq.routingKey}")
    private String routingKey;

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

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
        connectionFactory.setUsername(rabbitUserName);
        connectionFactory.setPassword(rabbitPassWord);
        connectionFactory.setPort(port);
        connectionFactory.setVirtualHost(virtualHost);

        declareRabbitArchitecture(connectionFactory);

        return connectionFactory;
    }

    @Bean
    RabbitAdmin declareRabbitArchitecture(CachingConnectionFactory connectionFactory){
        RabbitAdmin admin = new RabbitAdmin(connectionFactory());
        DirectExchange topicExchange = new DirectExchange(exchangeName);
        Binding binding = new Binding(queueName, Binding.DestinationType.QUEUE, exchangeName, routingKey, null);

        admin.declareQueue(new Queue(queueName));
        admin.declareExchange(topicExchange);
        admin.declareBinding(binding);

        return admin;
    }
}
