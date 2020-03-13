package com.msc.spring.consumer.spring.amqp;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * Created by annadowling on 2020-01-22.
 */

@Configuration
@ConditionalOnProperty(prefix = "spring.amqp", name = "enabled", havingValue = "true")
public class SpringAMQPConfig {

    @Value("${rabbitmq.exchangeName}")
    private String exchangeName;

    @Value("${rabbitmq.queueName}")
    private String queueName;

    @Value("${rabbitmq.durable}")
    private boolean durableQueue;

    @Value("${rabbitmq.routingKey}")
    private String routingKey;

    @Value("${rabbitmq.host}")
    private String host;

    @Value("${rabbitmq.port}")
    private Integer port;

    @Value("${rabbitmq.username}")
    private String rabbitUserName;

    @Value("${rabbitmq.password}")
    private String rabbitPassWord;

    @Value("${rabbitmq.virtualhost}")
    private String virtualHost;

    @Value("${multi.thread.enabled}")
    private boolean multiThreaded;

    @Bean
    Queue queue() {
        return new Queue(queueName, durableQueue);
    }

    //create custom connection factory
	@Bean
	ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
        connectionFactory.setUsername(rabbitUserName);
        connectionFactory.setPassword(rabbitPassWord);
        connectionFactory.setPort(port);
        connectionFactory.setVirtualHost(virtualHost);

        return connectionFactory;
	}

    @Bean
    SimpleMessageListenerContainer container(MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(SpringAMQPSubscriber subscriber) {
        if(multiThreaded){
            return new MessageListenerAdapter(subscriber, "receiveMessageMultiThread");
        }else{
            return new MessageListenerAdapter(subscriber, "receiveMessage");
        }
    }
}
