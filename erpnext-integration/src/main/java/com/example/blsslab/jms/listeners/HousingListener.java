package com.example.blsslab.jms.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.blsslab.jca.ErpNextConnection;
import com.example.blsslab.jca.ErpNextConnectionFactory;
import com.example.blsslab.model.doctype.DocTypes;
import com.example.blsslab.model.dto.HousingDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.BytesMessage;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.resource.ResourceException;

@Component
public class HousingListener implements MessageListener {

    @Autowired
    ErpNextConnectionFactory erpNextConnectionFactory;

    @Override
    public void onMessage(Message message) {
        try {
            BytesMessage raw = (BytesMessage) message;
            byte[] buf = new byte[(int) raw.getBodyLength()];
            raw.readBytes(buf);
            String payload = new String(buf);
            System.out.println("Recieved: " + payload);

            ObjectMapper mapper = new ObjectMapper();
            HousingDTO housing = mapper.readValue(payload, HousingDTO.class);

            ErpNextConnection connection = erpNextConnectionFactory.getConnection();
            connection.createDocument(DocTypes.ITEM, housing.toDocType());
        } catch (JMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ResourceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
