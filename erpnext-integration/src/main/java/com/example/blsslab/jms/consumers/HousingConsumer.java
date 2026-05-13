package com.example.blsslab.jms.consumers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.blsslab.jca.ErpNextConnection;
import com.example.blsslab.jca.ErpNextConnectionFactory;
import com.example.blsslab.jms.listeners.HousingListener;

import jakarta.annotation.PostConstruct;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.MessageConsumer;
import jakarta.jms.Session;
import jakarta.resource.ResourceException;

@Component
public class HousingConsumer extends RMQMessageConsumer {

    @Autowired
    ErpNextConnectionFactory erpNextConnectionFactory;

    HousingConsumer(ConnectionFactory connection) {
        super.connectionFactory = connection;
        super.listener = new HousingListener();
    }

    @Value("${rabbitmq.connection.housing.queue}")
    String queueName;

    @PostConstruct
    @Override
    public void initConnection() throws JMSException {
        try {
            ErpNextConnection erpNextConnection = erpNextConnectionFactory.getConnection();
            erpNextConnection.isAlive();
        } catch (ResourceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Connection conn = connectionFactory.createConnection();
        conn.start();
        Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageConsumer consumer = session.createConsumer(setUpDestination(queueName));
        consumer.setMessageListener(message -> listener.onMessage(message));
        conn.start();
    }
}
