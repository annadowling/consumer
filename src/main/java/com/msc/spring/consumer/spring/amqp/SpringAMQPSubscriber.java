package com.msc.spring.consumer.spring.amqp;/***************************************************************
 * Copyright (c) 2020 Errigal Inc.
 *
 * This software is the confidential and proprietary information
 * of Errigal, Inc.  You shall not disclose such confidential
 * information and shall use it only in accordance with the
 * license agreement you entered into with Errigal.
 *
 *************************************************************** */

import com.google.gson.Gson;
import com.msc.spring.consumer.message.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by annadowling on 2020-01-16.
 */

@Component
public class SpringAMQPSubscriber {

    @Value("${spring.amqp.enabled}")
    private static boolean springAMQPEnabled;

    @RabbitListener(queues = "${rabbitmq.queueName}")
    public void listen(byte[] message) {
        if (springAMQPEnabled) {
            String msg = new String(message);
            Message consumedMessage = new Gson().fromJson(msg, Message.class);
            System.out.println("Received a new message...");
            System.out.println(consumedMessage.toString());
        }
    }

}
