package com.msc.spring.consumer.spring.amqp;

import com.msc.spring.consumer.jeromq.jms.JEROMQSubscriber;
import com.msc.spring.consumer.message.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

/**
 * Created by annadowling on 2020-01-16.
 */

@Component
@ConditionalOnProperty(prefix = "spring.amqp", name = "enabled", havingValue = "true")
public class SpringAMQPSubscriber {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringAMQPSubscriber.class);

    @Value("${spring.amqp.enabled}")
    private boolean springAMQPEnabled;

    @Autowired
    MessageUtils messageUtils;

    private CountDownLatch latch = new CountDownLatch(1);


    public void receiveMessage(HashMap<String, String> message) {
        if (springAMQPEnabled) {
            LOGGER.info("Consuming Message from Spring AMQP Rabbit");
            messageUtils.saveMessageMap(message, false);
            LOGGER.info("Received <" + message + ">");
            latch.countDown();
        }
    }

    @Async
    public void receiveMessageMultiThread(HashMap<String, String> message) {
        if (springAMQPEnabled) {
            LOGGER.info("Consuming Message from Spring AMQP Rabbit");
            messageUtils.saveMessageMap(message, true);
            LOGGER.info("Received <" + message + ">");
            latch.countDown();
        }
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}


