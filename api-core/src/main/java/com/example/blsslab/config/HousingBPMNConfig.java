package com.example.blsslab.config;

import java.util.Map;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.blsslab.model.dto.AddressDTO;
import com.example.blsslab.model.dto.Country;
import com.example.blsslab.model.dto.HousingDTO;
import com.example.blsslab.model.dto.HousingType;
import com.example.blsslab.service.HousingService;
import com.example.blsslab.service.PublisherService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class HousingBPMNConfig {
    final HousingService housingService;

    final PublisherService publisherService;

    @ExternalTaskSubscription("addHousing")
    @Bean
    ExternalTaskHandler addHousing() {
        return (externalTask, externalTaskService) -> {
            log.info("start add housing external task service");

            Integer price = externalTask.getVariable("price");
            Double rating = externalTask.getVariable("rating");
            Integer numOfBeds = externalTask.getVariable("numOfBeds");
            HousingType housingType = HousingType.valueOf(externalTask.getVariable("housingType"));
            String street = externalTask.getVariable("street");
            Country country = Country.valueOf(externalTask.getVariable("country"));
            String initiator = externalTask.getVariable("initiator");

            log.info("got price={}, rating={}, numOfBeds={}, housingType={}, street={}, country={}", price, rating,
                    numOfBeds, housingType, street, country);

            AddressDTO address = new AddressDTO();
            address.setCountry(country);
            address.setStreet(street);

            HousingDTO housing = new HousingDTO();
            housing.setHousingType(housingType);
            housing.setPrice(price.longValue());
            housing.setNumOfBeds(numOfBeds);
            housing.setRating(rating);
            housing.setAddress(address);
            housing.setOwner(initiator);

            housing = housingService.addHousing(housing);

            externalTaskService.setVariables(externalTask, Map.ofEntries(Map.entry("housingId", housing.getId())));
            externalTaskService.setVariables(externalTask, Map.ofEntries(Map.entry("information", housing.toString())));
            externalTaskService.complete(externalTask);
        };
    }

    @ExternalTaskSubscription("moderateHousing")
    @Bean
    ExternalTaskHandler moderateHousing() {
        return (externalTask, externalTaskService) -> {
            log.info("start external task moderate housing");

            Long housingId = externalTask.getVariable("housingId");
            Boolean approved = Boolean.valueOf(externalTask.getVariable("approved"));

            externalTaskService.setVariables(externalTask, Map.ofEntries(Map.entry("isApproved", approved)));
            try {
                housingService.handleRequest(housingId, approved);
            } catch (Exception e) {
                log.error(e.getMessage());
                externalTaskService.setVariables(externalTask,
                        Map.ofEntries(Map.entry("isApproved", false), Map.entry("errorMsg", e.getMessage())));
            }
            externalTaskService.complete(externalTask);
        };
    }

    @ExternalTaskSubscription("publishMessages")
    @Bean
    ExternalTaskHandler publishMessages() {
        return (externalTask, externalTaskService) -> {
            publisherService.publish();
            externalTaskService.complete(externalTask);
        };
    }
}
