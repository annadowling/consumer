package com.msc.spring.consumer.jeromq.jms;/***************************************************************
 * Copyright (c) 2020 Errigal Inc.
 *
 * This software is the confidential and proprietary information
 * of Errigal, Inc.  You shall not disclose such confidential
 * information and shall use it only in accordance with the
 * license agreement you entered into with Errigal.
 *
 *************************************************************** */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.zeromq.ZMQ;

/**
 * Created by annadowling on 2020-01-16.
 */

@Component
@ConditionalOnProperty(prefix = "jeromq", name = "enabled", havingValue = "true")
public class JEROMQSubscriber {

    @Value("${zeromq.address}")
    private String bindAddress;

    @Value("${jeromq.enabled}")
    private boolean jeroMQEnabled;

    final String errorMessage = "Exception encountered = ";

    @Bean
    public void consumeJeroMQMessage() {
        if (jeroMQEnabled) {
            try {
                ZMQ.Context ctx = ZMQ.context(1);

                ZMQ.Socket subscriber = ctx.socket(ZMQ.SUB);
                subscriber.subscribe("".getBytes());
                subscriber.connect(bindAddress);

                // Eliminate slow subscriber problem
                Thread.sleep(100);
                subscriber.close();
                ctx.close();
                System.out.println("Message received is: " + subscriber.recvStr());
            } catch (Exception e) {
                System.out.println(errorMessage + e.getLocalizedMessage());

            }
        }
    }
}
