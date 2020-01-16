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
import org.zeromq.ZMQ;

/**
 * Created by annadowling on 2020-01-16.
 */

public class ZMQSubscriber {

    @Value("${zeromq.address}")
    private static String bindAddress;

    public static void main(String[] args) throws Exception {
        ZMQ.Context ctx = ZMQ.context(1);

        ZMQ.Socket sub = ctx.socket(ZMQ.SUB);
        sub.subscribe("".getBytes());
        sub.connect(bindAddress);

        // Eliminate slow subscriber problem
        Thread.sleep(100);
        logReceivedMessage(sub);

        sub.close();
        ctx.close();
    }

    public static void logReceivedMessage(ZMQ.Socket subscriber){
        System.out.println("Message received is: " + subscriber.recvStr());
    }
}
