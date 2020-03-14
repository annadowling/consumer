package com.msc.spring.consumer.jeromq.jms;

import com.msc.spring.consumer.message.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.zeromq.ZContext;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;
import zmq.Msg;

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

    @Value("${multi.thread.enabled}")
    private boolean multiThreaded;

    final String errorMessage = "Exception encountered = ";

    @Autowired
    MessageUtils messageUtils;

    @Bean
    @Async
    @ConditionalOnProperty(prefix = "multi.thread", name = "enabled", havingValue = "true")
    public void consumeJeroMQMessageMultiThread() {
        if (jeroMQEnabled) {
            // Prepare our context and subscriber
            try (ZContext context = new ZContext()) {
                ZMQ.Socket subscriber = context.createSocket(ZMQ.SUB);
                subscriber.connect(bindAddress);
                subscriber.subscribe("B".getBytes(ZMQ.CHARSET));

                System.out.println("Starting Subscriber..");
                int i = 0;
                while (true) {
                    // Read envelope with address
                    String messageAddress = subscriber.recvStr();
                    // Read message contents
                    byte[] messageBody = subscriber.recv();
                    messageUtils.saveMessage(messageBody, true);
                    System.out.println(" [x] Received Message: '" + messageBody + "'" + "for address: " + messageAddress);
                    i++;
                }
            }
        }
    }


    @Bean
    @ConditionalOnProperty(prefix = "multi.thread", name = "enabled", havingValue = "false")
    public void consumeJeroMQMessage() {
        if (jeroMQEnabled) {
            // Prepare our context and subscriber
            try (ZContext context = new ZContext()) {
                ZMQ.Socket subscriber = context.createSocket(ZMQ.SUB);
                subscriber.connect(bindAddress);
                subscriber.subscribe("B".getBytes(ZMQ.CHARSET));

                System.out.println("Starting Subscriber..");
                int i = 0;
                while (true) {
                    // Read envelope with address
                    String messageAddress = subscriber.recvStr();
                    // Read message contents
                    byte[] messageBody = subscriber.recv();
                    messageUtils.saveMessage(messageBody, false);
                    System.out.println(" [x] Received Message: '" + messageBody + "'" + "for address: " + messageAddress);
                    i++;
                }
            }
        }
    }
}
