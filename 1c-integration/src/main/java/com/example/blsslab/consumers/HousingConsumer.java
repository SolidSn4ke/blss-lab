package com.example.blsslab.consumers;

import org.springframework.stereotype.Component;

import com.example.blsslab.listeners.HousingListener;
import com.rabbitmq.jms.admin.RMQDestination;

import jakarta.annotation.PostConstruct;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.MessageConsumer;
import jakarta.jms.Session;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HousingConsumer {

    private final ConnectionFactory connectionFactory;

    @PostConstruct
    public void startListener() throws JMSException {
        Connection conn = connectionFactory.createConnection();
        conn.start();
        String queueName = "testTopic.queue";
        Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

        RMQDestination destination = new RMQDestination();
        destination.setDestinationName(queueName);
        destination.setAmqp(true);
        destination.setAmqpQueueName(queueName);

        MessageConsumer consumer = session.createConsumer(destination);
        consumer.setMessageListener(message -> {
            HousingListener listener = new HousingListener();
            listener.onMessage(message);
        });

        conn.start();
        System.out.println("Raw JMS consumer started");
    }
}
