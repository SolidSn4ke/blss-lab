package com.example.blsslab.service;

import org.springframework.stereotype.Service;
import com.example.blsslab.jca.ErpNextConnection;
import com.example.blsslab.jca.ErpNextConnectionFactory;
import com.example.blsslab.model.doctype.DocTypes;
import com.example.blsslab.model.dto.HousingDTO;
import com.example.blsslab.model.dto.OperationType;
import jakarta.resource.ResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ErpNextConnectionService {
    final ErpNextConnectionFactory connectionFactory;
    ErpNextConnection connection = null;

    public void sendToERPNext(HousingDTO housing, OperationType opType) {
        log.info(
                "Processing housing event: operation={}, housingId={}",
                opType,
                housing.getId());

        try {
            connection = (ErpNextConnection) connectionFactory.getConnection();
        } catch (ResourceException e) {
            throw new RuntimeException(e.getMessage());
        }

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
    }
}
