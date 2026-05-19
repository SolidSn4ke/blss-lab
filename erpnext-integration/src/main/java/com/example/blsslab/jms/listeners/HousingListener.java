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
import jakarta.resource.ResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class HousingListener implements MessageListener {

    private final ErpNextConnectionFactory erpNextConnectionFactory;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onMessage(Message message) {
        ErpNextConnection connection = null;

        try {
            log.debug("Received JMS message: type={}", message.getClass().getSimpleName());

            BytesMessage raw = (BytesMessage) message;
            byte[] buf = new byte[(int) raw.getBodyLength()];
            raw.readBytes(buf);

            String payload = new String(buf);
            log.debug("Received payload: {}", payload);

            com.example.blsslab.model.dto.Message<HousingDTO> data = mapper.readValue(
                    payload,
                    new TypeReference<com.example.blsslab.model.dto.Message<HousingDTO>>() {
                    });

            HousingDTO housing = (HousingDTO) data.getEntity();
            OperationType opType = data.getOpType();

            log.info(
                    "Processing housing event: operation={}, housingId={}",
                    opType,
                    housing.getId());

            connection = (ErpNextConnection) erpNextConnectionFactory.getConnection();

            switch (opType) {
                case CREATE -> {
                    connection.createDocument(DocTypes.ITEM, housing.toDocType());
                    log.info(
                            "Housing synced to ERPNext successfully: operation=CREATE, housingId={}",
                            housing.getId());
                }
                case DELETE -> {
                    connection.deleteDocument(DocTypes.ITEM, housing.toDocType().getItem_code());
                    log.info(
                            "Housing removed from ERPNext successfully: operation=DELETE, housingId={}",
                            housing.getId());
                }
                default -> {
                    log.warn(
                            "Unsupported operation received: operation={}, housingId={}",
                            opType,
                            housing.getId());
                    throw new UnsupportedOperationException("Unsupported operation: " + opType);
                }
            }

        } catch (Exception e) {
            log.error("Failed to process housing JMS message", e);
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    log.debug("ERPNext connection closed");
                } catch (ResourceException e) {
                    log.error("Failed to close ERPNext connection", e);
                }
            }
        }
    }
}
