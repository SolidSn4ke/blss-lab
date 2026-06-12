package com.example.blsslab.config;

import java.util.List;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.blsslab.model.dto.RequestStatus;
import com.example.blsslab.model.entity.ReceivedMessageEntity;
import com.example.blsslab.model.repo.ReceivedMessageRepository;
import com.example.blsslab.service.ErpNextConnectionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class HousingBPMNConfig {

    final ReceivedMessageRepository receivedMessageRepository;
    final ErpNextConnectionService connectionService;

    @ExternalTaskSubscription("sendToERPNext")
    @Bean
    ExternalTaskHandler processMessages() {
        return (externalTask, externalTaskService) -> {
            log.info("start send to erpnext extenal task");
            List<ReceivedMessageEntity> messages = receivedMessageRepository.findByStatus(RequestStatus.PENDING);

            if (messages.size() > 0) {
                connectionService.processMessages(messages);
            }

            externalTaskService.complete(externalTask);
            log.info("external task send to erpnext has been completed");
        };
    }
}
