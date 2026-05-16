package com.example.blsslab.model.postgres.entity;

import com.example.blsslab.model.dto.MessageStatus;
import com.example.blsslab.model.dto.OperationType;
import com.example.blsslab.model.dto.RabbitMQTopics;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "housing_outbox")
@Getter
@Setter
@NoArgsConstructor
public class HousingOutboxEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    MessageStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "op_type")
    OperationType operationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "topic_name")
    RabbitMQTopics topic;

    @Column(name = "housing_id")
    Long housingId;

    public HousingOutboxEntity(Long housingId, OperationType opType) {
        this.status = MessageStatus.PENDING;
        this.operationType = opType;
        this.housingId = housingId;
        this.topic = RabbitMQTopics.HOUSING_TOPIC;
    }
}
