package com.example.blsslab.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CamundaVariable<T> {
    T value;
    String type;
}
