package com.example.blsslab.jms.listeners;

import org.springframework.stereotype.Component;

import com.example.blsslab.jca.ErpNextConnection;
import com.example.blsslab.jca.ErpNextConnectionFactory;
import com.example.blsslab.model.doctype.DocTypes;
import com.example.blsslab.model.dto.HousingDTO;
import com.example.blsslab.model.dto.OperationType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.BytesMessage;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HousingListener implements MessageListener {

    final ErpNextConnectionFactory erpNextConnectionFactory;

    @Override
    public void onMessage(Message message) {
        try {
            BytesMessage raw = (BytesMessage) message;
            byte[] buf = new byte[(int) raw.getBodyLength()];
            raw.readBytes(buf);
            String payload = new String(buf);
            System.out.println("Recieved: " + payload);

            ObjectMapper mapper = new ObjectMapper();
            com.example.blsslab.model.dto.Message<HousingDTO> data = mapper.readValue(
                    payload, new TypeReference<com.example.blsslab.model.dto.Message<HousingDTO>>() {
                    });

            HousingDTO housing = (HousingDTO) data.getEntity();
            OperationType opType = data.getOpType();

            ErpNextConnection connection = erpNextConnectionFactory.getConnection();

            switch (opType) {
                case CREATE -> connection.createDocument(DocTypes.ITEM, housing.toDocType());
                case DELETE -> connection.deleteDocument(DocTypes.ITEM, housing.toDocType().getItem_code());

                default -> throw new UnsupportedOperationException();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
