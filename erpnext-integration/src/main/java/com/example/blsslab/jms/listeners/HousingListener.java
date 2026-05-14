package com.example.blsslab.jms.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.BytesMessage;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;

public class HousingListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        try {
            BytesMessage raw = (BytesMessage) message;
            byte[] buf = new byte[(int) raw.getBodyLength()];
            raw.readBytes(buf);
            String payload = new String(buf);
            System.out.println("Recieved: " + payload);

            ObjectMapper mapper = new ObjectMapper();
            // TODO: Импортировать класс HousingDTO
            // HousingDTO housingDTO = mapper.readValue(payload, HousingDTO.class);

            // } catch (JsonProcessingException e) {
            // e.printStackTrace();
        } catch (JMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
