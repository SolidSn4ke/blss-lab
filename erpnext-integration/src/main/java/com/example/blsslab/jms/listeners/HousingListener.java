package com.example.blsslab.jms.listeners;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.blsslab.model.dto.CamundaVariable;
import com.example.blsslab.model.dto.HousingDTO;
import com.example.blsslab.model.dto.OperationType;
import com.example.blsslab.service.CamundaConnectionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    final CamundaConnectionService camundaConnectionService;
    final ObjectMapper mapper;

    @Override
    public void onMessage(Message message) {
        try {
            log.debug("Received JMS message: type={}", message.getClass().getSimpleName());

            BytesMessage raw = (BytesMessage) message;
            byte[] buf = new byte[(int) raw.getBodyLength()];
            raw.readBytes(buf);

            String payload = new String(buf);
            log.debug("Received payload: {}", payload);

            com.example.blsslab.model.dto.Message<HousingDTO> data;
            try {
                data = mapper.readValue(
                        payload,
                        new TypeReference<com.example.blsslab.model.dto.Message<HousingDTO>>() {
                        });
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage());
            }

            HousingDTO housing = (HousingDTO) data.getEntity();
            OperationType opType = data.getOpType();

            Map<String, CamundaVariable<? extends Object>> vars = Map.ofEntries(
                    Map.entry("opType", new CamundaVariable<OperationType>(opType, "String")));

            camundaConnectionService.correlateMessage("housingConsumer", String.format("HOUSING-%d", housing.getId()),
                    vars);
        } catch (JMSException e) {
            log.error("Failed to process housing JMS message", e);
            throw new RuntimeException(e);
        }
    }
}
