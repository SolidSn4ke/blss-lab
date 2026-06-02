package com.example.blsslab.config;

import java.time.LocalDate;
import java.util.Map;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.blsslab.model.dto.BookingDTO;
import com.example.blsslab.service.BookingService;
import com.example.blsslab.service.HousingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BookingBPMNConfig {
    final BookingService bookingService;

    final HousingService housingService;

    @ExternalTaskSubscription("bookHousing")
    @Bean
    ExternalTaskHandler bookHousing() {
        return (externalTask, externalTaskService) -> {
            log.info("start book housing external task");

            Integer id = externalTask.getVariable("housingId");
            String checkIn = externalTask.getVariable("checkIn");
            String checkOut = externalTask.getVariable("checkOut");
            Integer adultsCount = externalTask.getVariable("adultsCount");
            Integer childCount = externalTask.getVariable("childCount");
            Integer infantsCount = externalTask.getVariable("infantsCount");
            Integer petCount = externalTask.getVariable("petCount");
            String initiator = externalTask.getVariable("initiator");

            log.info(
                    "got id={} checkIn={} checkOut={} adultsCount={} childCount={} infantsCount={} petCount={} initiator={}",
                    id,
                    checkIn, checkOut, adultsCount, childCount, infantsCount, petCount, initiator);

            BookingDTO bookingInfo = new BookingDTO();
            bookingInfo.setHousingId(id.longValue());
            bookingInfo.setCheckIn(LocalDate.parse(checkIn));
            bookingInfo.setCheckOut(LocalDate.parse(checkOut));
            bookingInfo.setAdultsCount(adultsCount);
            bookingInfo.setChildCount(childCount);
            bookingInfo.setInfantsCount(infantsCount);
            bookingInfo.setPetCount(petCount);
            bookingInfo.setGuest(initiator);

            boolean isValid;
            try {
                bookingInfo = bookingService.requireHousing(bookingInfo);
                isValid = true;
                externalTaskService.setVariables(externalTask,
                        Map.ofEntries(
                                Map.entry("assigneeHost",
                                        housingService.getHousingById(bookingInfo.getHousingId()).getOwner()),
                                Map.entry("bookingId", bookingInfo.getId())));
            } catch (Exception e) {
                log.error("Error while booking housing", e);
                externalTaskService.setVariables(externalTask,
                        Map.ofEntries(Map.entry("errorMsg", e.getMessage())));
                isValid = false;
            }

            externalTaskService.setVariables(externalTask, Map.ofEntries(Map.entry("isValid", isValid)));
            externalTaskService.complete(externalTask);
        };
    }

    @ExternalTaskSubscription("moderateBooking")
    @Bean
    ExternalTaskHandler moderateBooking() {
        return (externalTask, externalTaskService) -> {
            log.info("start moderate booking external task");

            Boolean approved = Boolean.valueOf(externalTask.getVariable("approved"));

            log.info("got approved={}", approved);

            try {
                bookingService.handleRequest(externalTask.getVariable("assigneeHost"),
                        externalTask.getVariable("bookingId"), approved);
                if (!approved) {
                    externalTaskService.setVariables(externalTask,
                            Map.ofEntries(Map.entry("errorMsg", "Booking request has been rejected by host")));
                }
            } catch (Exception e) {
                approved = false;
                log.error("Error while moderating booking", e);
                externalTaskService.setVariables(externalTask,
                        Map.ofEntries(Map.entry("errorMsg", e.getMessage())));
            }

            externalTaskService.setVariables(externalTask, Map.of("isApproved", approved));
            externalTaskService.complete(externalTask);
        };
    }
}
