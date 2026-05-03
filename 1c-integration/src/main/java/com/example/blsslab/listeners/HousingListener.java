package com.example.blsslab.listeners;

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
        } catch (JMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
