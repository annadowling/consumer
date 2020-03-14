package com.msc.spring.consumer.jeromq.jms;

import com.msc.spring.consumer.message.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.zeromq.ZMQ;
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
    public void consumeJeroMQMessage() {
        if (jeroMQEnabled) {
            // Prepare our context and subscriber
            try {
                ZMQ.Context context = ZMQ.context(1);
                ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
                Msg msg =null;

                subscriber.connect(bindAddress);
                subscriber.subscribe("B".getBytes());

                System.out.println("Starting Subscriber..");
                int i = 0;
                while (true) {
                    if(multiThreaded){
                        receiveMessageMultiThread(subscriber);
                    }else{
                        byte[] messageBody = subscriber.recv();
                        messageUtils.saveMessage(messageBody, false);
                        System.out.println(" [x] Received Message: '" + messageBody + "'");
                    }
                    i++;
                }
            } catch (Exception e) {
                System.out.println(errorMessage + e.getLocalizedMessage());
            }
        }
    }

    @Async
    void receiveMessageMultiThread(ZMQ.Socket subscriber){
        byte[] messageBody = subscriber.recv();
        messageUtils.saveMessage(messageBody, true);
        System.out.println(" [x] Received Message: '" + messageBody + "'");
    }
}
