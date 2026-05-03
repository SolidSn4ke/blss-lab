package com.example.blsslab.consumers;

import com.rabbitmq.jms.admin.RMQDestination;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.MessageListener;

public abstract class RMQMessageConsumer {

    ConnectionFactory connectionFactory;
    MessageListener listener;

    abstract void initConnection() throws JMSException;

    RMQDestination setUpDestination(String queueName) {
        RMQDestination destination = new RMQDestination();
        destination.setDestinationName(queueName);
        destination.setAmqp(true);
        destination.setAmqpQueueName(queueName);
        return destination;
    }
}
