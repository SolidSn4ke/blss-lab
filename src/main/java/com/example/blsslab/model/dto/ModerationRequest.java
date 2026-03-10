package com.example.blsslab.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ModerationRequest {
    Boolean approved;
    UserDTO user;
}
