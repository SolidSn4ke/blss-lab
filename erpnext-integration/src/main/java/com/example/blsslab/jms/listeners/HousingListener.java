package com.example.blsslab.jms.listeners;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.blsslab.model.dto.RequestStatus;
import com.example.blsslab.model.entity.ReceivedMessageEntity;
import com.example.blsslab.model.repo.ReceivedMessageRepository;
import jakarta.jms.BytesMessage;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class HousingListener implements MessageListener {

    final ReceivedMessageRepository receivedMessageRepository;

    @Override
    @Transactional
    public void onMessage(Message message) {
        try {
            log.debug("Received JMS message: type={}", message.getClass().getSimpleName());

            BytesMessage raw = (BytesMessage) message;
            byte[] buf = new byte[(int) raw.getBodyLength()];
            raw.readBytes(buf);

            String payload = new String(buf);
            log.debug("Received payload: {}", payload);

            ReceivedMessageEntity receivedMessage = new ReceivedMessageEntity();
            receivedMessage.setStatus(RequestStatus.PENDING);
            receivedMessage.setMessage(payload);

            receivedMessageRepository.save(receivedMessage);
        } catch (JMSException e) {
            log.error("Failed to process housing JMS message", e);
            throw new RuntimeException(e);
        }
    }
}
