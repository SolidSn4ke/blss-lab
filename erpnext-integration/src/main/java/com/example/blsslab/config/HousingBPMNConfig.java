package com.example.blsslab.config;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.blsslab.jca.exception.ErpNextDuplicateOperationException;
import com.example.blsslab.model.dto.AddressDTO;
import com.example.blsslab.model.dto.Country;
import com.example.blsslab.model.dto.HousingDTO;
import com.example.blsslab.model.dto.HousingType;
import com.example.blsslab.model.dto.OperationType;
import com.example.blsslab.service.ErpNextConnectionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class HousingBPMNConfig {

    final ErpNextConnectionService connectionService;

    @ExternalTaskSubscription("sendToERPNext")
    @Bean
    ExternalTaskHandler processMessages() {
        return (externalTask, externalTaskService) -> {
            log.info("start send to erpnext extenal task");

            Long id = externalTask.getVariable("housingId");
            Integer price = externalTask.getVariable("price");
            Double rating = externalTask.getVariable("rating");
            Integer numOfBeds = externalTask.getVariable("numOfBeds");
            HousingType housingType = HousingType.valueOf(externalTask.getVariable("housingType"));
            String street = externalTask.getVariable("street");
            Country country = Country.valueOf(externalTask.getVariable("country"));
            String initiator = externalTask.getVariable("initiator");
            OperationType opType = OperationType.valueOf(externalTask.getVariable("opType"));

            AddressDTO address = new AddressDTO();
            address.setCountry(country);
            address.setStreet(street);

            HousingDTO housing = new HousingDTO();
            housing.setId(id);
            housing.setHousingType(housingType);
            housing.setPrice(price.longValue());
            housing.setNumOfBeds(numOfBeds);
            housing.setRating(rating);
            housing.setAddress(address);
            housing.setOwner(initiator);

            try {
                connectionService.sendToERPNext(housing, opType);
            } catch (ErpNextDuplicateOperationException e) {
                // ignore
            }

            externalTaskService.complete(externalTask);
            log.info("external task send to erpnext has been completed");
        };
    }
}
