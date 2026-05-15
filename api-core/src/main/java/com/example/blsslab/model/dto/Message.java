package com.example.blsslab.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Message<T> {
    T entity;
    OperationType opType;
}
