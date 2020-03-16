package com.msc.spring.consumer.java.client;

import com.msc.spring.consumer.message.MessageUtils;
import com.msc.spring.consumer.spring.amqp.SpringAMQPSubscriber;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.zeromq.ZMQ;

/**
 * Created by annadowling on 2020-01-16.
 */

@Component
@ConditionalOnProperty(prefix = "rabbitmq.java.client", name = "enabled", havingValue = "true")
public class RabbitMQSubscriber {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQSubscriber.class);

    @Value("${rabbitmq.queueName}")
    private String queueName;

    @Value("${rabbitmq.host}")
    private String host;

    @Value("${rabbitmq.port}")
    private Integer port;

    @Value("${rabbitmq.username}")
    private String rabbitUserName;

    @Value("${rabbitmq.password}")
    private String rabbitPassWord;

    @Value("${rabbitmq.durable}")
    private boolean durableQueue;

    @Value("${rabbitmq.virtualhost}")
    private String virtualHost;

    @Value("${rabbitmq.exchangeName}")
    private String exchangeName;

    @Value("${rabbitmq.routingKey}")
    private String routingKey;

    @Value("${rabbitmq.autoAck}")
    private boolean autoAck;

    @Value("${rabbitmq.java.client.enabled}")
    private boolean rabbitJavaClientEnabled;

    Channel channel;

    @Autowired
    MessageUtils messageUtils;

    final String errorMessage = "Exception encountered = ";


    @Bean
    @ConditionalOnProperty(prefix = "multi.thread", name = "enabled", havingValue = "false")
    public void consumeRMQMessage() {
        if (rabbitJavaClientEnabled) {
            try {
                Channel channel = createChannelConnection();

                LOGGER.info("Waiting for RABBITMQ CLIENT messages.");
                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    byte[] messageBody = delivery.getBody();
                    messageUtils.saveMessage(messageBody, false);
                    String message = new String(delivery.getBody(), "UTF-8");
                    LOGGER.info("Received RABBITMQ CLIENT Message: '" + message + "'");
                };
                channel.basicConsume(queueName, autoAck, deliverCallback, consumerTag -> {
                });
            } catch (Exception e) {
                LOGGER.info(errorMessage + e.getLocalizedMessage());
            }
        }
    }

    @Bean
    @Async
    @ConditionalOnProperty(prefix = "multi.thread", name = "enabled", havingValue = "true")
    public void consumeRMQMessageMultiThread() {
        if (rabbitJavaClientEnabled) {
            try {
                Channel channel = createChannelConnection();

                LOGGER.info("Waiting for RABBITMQ CLIENT messages.");
                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    byte[] messageBody = delivery.getBody();
                    messageUtils.saveMessage(messageBody, true);
                    String message = new String(delivery.getBody(), "UTF-8");
                    LOGGER.info("Received RABBITMQ CLIENT Message: '" + message + "'");
                };
                channel.basicConsume(queueName, autoAck, deliverCallback, consumerTag -> {
                });
            } catch (Exception e) {
                LOGGER.info(errorMessage + e.getLocalizedMessage());
            }
        }
    }

    /**
     * Channel to RabbitMQ server used for declaring architecture(queues, exchanges, bindings)
     * and publishing messages.
     *
     * @return Channel
     */

    Channel createChannelConnection() {
        ConnectionFactory factory = createConnection();
        try {
            Connection connection = factory.newConnection();

            channel = connection.createChannel();
            channel.queueDeclare(queueName, durableQueue, false, false, null);
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);
            channel.queueBind(queueName, exchangeName, routingKey);

        } catch (Exception e) {
            LOGGER.info(errorMessage + e.getLocalizedMessage());
        }

        return channel;
    }

    /**
     * Creates connection to RabbitMQ server using specific env variables
     *
     * @return ConnectionFactory
     */
    ConnectionFactory createConnection() {
        ConnectionFactory factory = new ConnectionFactory();
        try {
            factory.setHost(host);
            factory.setPassword(rabbitPassWord);
            factory.setUsername(rabbitUserName);
            factory.setPort(port);
            factory.setVirtualHost(virtualHost);
        } catch (Exception e) {
            LOGGER.info(errorMessage + e.getLocalizedMessage());
        }
        return factory;
    }
}
