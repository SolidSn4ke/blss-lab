package com.example.blsslab.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.example.blsslab.model.dto.CamundaVariable;

import jakarta.annotation.PostConstruct;

@Service
public class CamundaConnectionService {
    @Value("${camunda.bpm.client.base-url}")
    String baseUrl;

    RestClient restClient;

    @PostConstruct
    void init() {
        restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public void correlateMessage(String messageName, String businessKey,
            Map<String, CamundaVariable<? extends Object>> vars) {
        Map<String, Object> body = Map.ofEntries(
                Map.entry("messageName", messageName),
                Map.entry("businessKey", businessKey),
                Map.entry("processVariables", vars));

        restClient.post().uri("/message").body(body).contentType(MediaType.APPLICATION_JSON).retrieve()
                .toBodilessEntity();
    }
}
