package com.example.blsslab.model.entity;

import com.example.blsslab.model.dto.RequestStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "received_messages")
@Getter
@Setter
public class ReceivedMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    @Enumerated(EnumType.STRING)
    RequestStatus status;

    @Column
    String message;
}
