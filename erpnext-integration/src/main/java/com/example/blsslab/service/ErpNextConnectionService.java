package com.example.blsslab.service;

import java.util.List;

import org.glassfish.hk2.runlevel.RunLevelException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.example.blsslab.jca.ErpNextConnection;
import com.example.blsslab.jca.ErpNextConnectionFactory;
import com.example.blsslab.model.doctype.DocTypes;
import com.example.blsslab.model.dto.HousingDTO;
import com.example.blsslab.model.dto.OperationType;
import com.example.blsslab.model.entity.ReceivedMessageEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.resource.ResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ErpNextConnectionService {
    final ErpNextConnectionFactory connectionFactory;
    final ObjectMapper mapper = new ObjectMapper();
    ErpNextConnection connection = null;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void processMessages(List<ReceivedMessageEntity> messages) {
        messages.stream().sorted((e1, e2) -> e1.getId().compareTo(e2.getId())).forEach(m -> {
            com.example.blsslab.model.dto.Message<HousingDTO> data;
            try {
                data = mapper.readValue(
                        m.getMessage(),
                        new TypeReference<com.example.blsslab.model.dto.Message<HousingDTO>>() {
                        });
            } catch (JsonProcessingException e) {
                throw new RunLevelException(e.getMessage());
            }

            HousingDTO housing = (HousingDTO) data.getEntity();
            OperationType opType = data.getOpType();

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
        });
    }
}
