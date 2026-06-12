package com.example.blsslab.jms.consumers;

import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.blsslab.jca.exception.ErpNextDuplicateOperationException;
import com.example.blsslab.jms.listeners.HousingListener;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.annotation.PostConstruct;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.MessageConsumer;
import jakarta.jms.Session;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HousingConsumer extends RMQMessageConsumer {

    RuntimeService runtimeService;

    public HousingConsumer(ConnectionFactory connection, HousingListener listener, RuntimeService runtimeService) {
        super.connectionFactory = connection;
        super.listener = listener;
        this.runtimeService = runtimeService;
    }

    @Value("${rabbitmq.connection.housing.queue}")
    private String queueName;

    private boolean needRecovery = false;

    @PostConstruct
    @Override
    public void initConnection() throws JMSException {
        log.info("Initializing HousingConsumer for queue={}", queueName);

        Connection conn = connectionFactory.createConnection();
        conn.start();

        Session session = conn.createSession(false, Session.CLIENT_ACKNOWLEDGE);

        log.info("JMS session created (CLIENT_ACKNOWLEDGE)");

        MessageConsumer consumer = session.createConsumer(setUpDestination(queueName));

        consumer.setMessageListener(message -> {
            try {
                log.debug("Received JMS message, delegating to listener");

                listener.onMessage(message);

                runtimeService.startProcessInstanceByMessage("housingConsumer");

                message.acknowledge();
                log.debug("Message acknowledged successfully");
            } catch (Exception e) {
                needRecovery = true;
                log.error("Error while processing JMS message", e);
            }
        });
        conn.start();

        log.info("HousingConsumer successfully started");
    }

    @Scheduled(fixedRate = 10000)
    private void recover() throws JMSException {
        if (needRecovery) {
            log.warn("Attempting to recover HousingConsumer connection...");

            needRecovery = false;
            initConnection();

            log.info("HousingConsumer recovery completed");
        }
    }
}
