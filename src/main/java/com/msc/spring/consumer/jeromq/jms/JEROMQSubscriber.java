package com.msc.spring.consumer.jeromq.jms;/***************************************************************
 * Copyright (c) 2020 Errigal Inc.
 *
 * This software is the confidential and proprietary information
 * of Errigal, Inc.  You shall not disclose such confidential
 * information and shall use it only in accordance with the
 * license agreement you entered into with Errigal.
 *
 *************************************************************** */

import com.msc.spring.consumer.interfaces.ConsumerSetup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.zeromq.ZMQ;

/**
 * Created by annadowling on 2020-01-16.
 */

@Component
public class JEROMQSubscriber implements ConsumerSetup {

    @Value("${zeromq.address}")
    private static String bindAddress;

    @Value("${jeromq.enabled}")
    private static boolean jeroMQEnabled;

    final String errorMessage = "Exception encountered = ";

    @Bean
    @Override
    public void consumeMessage() {
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
