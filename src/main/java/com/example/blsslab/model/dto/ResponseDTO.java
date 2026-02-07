package com.example.blsslab.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class ResponseDTO<T> {
    T entity;

    String message;

    @NonNull Integer code;
}
