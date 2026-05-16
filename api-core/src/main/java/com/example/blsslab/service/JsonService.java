package com.example.blsslab.service;

import org.springframework.stereotype.Service;

import com.example.blsslab.model.dto.Message;
import com.example.blsslab.model.dto.OperationType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JsonService {

    final ObjectMapper mapper;

    public <T> String toJsonString(T entity, OperationType opType) throws JsonProcessingException {
        Message<T> msg = new Message<T>(entity, opType);
        return mapper.writeValueAsString(msg);
    }

}
