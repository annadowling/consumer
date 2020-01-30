package com.msc.spring.consumer.spring.amqp;

import com.msc.spring.consumer.message.MessageUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * Created by annadowling on 2020-01-16.
 */

@Service
@ConditionalOnProperty(prefix = "spring.amqp", name = "enabled", havingValue = "true")
public class SpringAMQPSubscriber implements MessageListener {

    @Value("${spring.amqp.enabled}")
    private boolean springAMQPEnabled;

    @Autowired
    MessageUtils messageUtils;


    public void onMessage(Message message) {
        if (springAMQPEnabled) {
            System.out.println("Consuming Message - " + new String(message.getBody()));
            byte[] messageBody = message.getBody();
            messageUtils.saveMessage(messageBody);
        }
    }
}


