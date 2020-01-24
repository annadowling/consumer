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
            // Prepare our context and subscriber
            try {
                ZMQ.Context context = ZMQ.context(1);
                ZMQ.Socket subscriber = context.socket(ZMQ.SUB);

                subscriber.connect(bindAddress);
                subscriber.subscribe("B".getBytes());

                System.out.println("Starting Subscriber..");
                int i = 0;
                while (true) {
                    String address = subscriber.recvStr();
                    String contents = subscriber.recvStr();
                    System.out.println(address + ":" + new String(contents) + ": " + (i));
                    i++;
                }
            } catch (Exception e) {
                System.out.println(errorMessage + e.getLocalizedMessage());
            }
        }
    }
}
