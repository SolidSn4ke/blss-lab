package com.example.blsslab.service;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.example.blsslab.config.MqttConfig.MqttGateway;
import com.example.blsslab.model.dto.HousingDTO;
import com.example.blsslab.model.dto.MessageStatus;
import com.example.blsslab.model.postgres.entity.HousingOutboxEntity;
import com.example.blsslab.model.postgres.repos.HousingOutboxRepository;
import com.example.blsslab.model.postgres.repos.HousingRepository;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PublisherService {
    final HousingOutboxRepository outboxRepository;

    final HousingRepository housingRepository;

    final MqttGateway gateway;

    final JsonService jsonService;

    @Scheduled(cron = "0 * * * * *")
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void publish() {
        List<HousingOutboxEntity> entities = outboxRepository.findAllByStatus(MessageStatus.PENDING);

        entities.stream().sorted((e1, e2) -> e1.getId().compareTo(e2.getId())).forEach(e -> {
            try {
                gateway.sendToMqtt(e.getTopic().topic,
                        jsonService.toJsonString(new HousingDTO(housingRepository.getReferenceById(e.getHousingId())),
                                e.getOperationType()));
            } catch (JsonProcessingException ex) {
                throw new RuntimeException("Failed to convert to json string");
            }

            e.setStatus(MessageStatus.SENT);

            outboxRepository.save(e);
        });
    }
}
