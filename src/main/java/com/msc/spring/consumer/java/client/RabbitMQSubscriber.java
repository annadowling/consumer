package com.msc.spring.consumer.java.client;

import com.msc.spring.consumer.message.MessageUtils;
import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by annadowling on 2020-01-16.
 */

@Component
@ConditionalOnProperty(prefix = "rabbitmq.java.client", name = "enabled", havingValue = "true")
public class RabbitMQSubscriber {

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
    public void consumeRMQMessage() {
        if (rabbitJavaClientEnabled) {
            try {
                Channel channel = createChannelConnection();

                System.out.println(" [*] Waiting for messages.");
                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    byte[] messageBody = delivery.getBody();
                    messageUtils.saveMessage(messageBody);
                    String message = new String(delivery.getBody(), "UTF-8");
                    System.out.println(" [x] Received Message: '" + message + "'");
                };
                channel.basicConsume(queueName, autoAck, deliverCallback, consumerTag -> {
                });
            } catch (Exception e) {
                System.out.println(errorMessage + e.getLocalizedMessage());
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
            System.out.println(errorMessage + e.getLocalizedMessage());
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
            System.out.println(errorMessage + e.getLocalizedMessage());
        }
        return factory;
    }
}
