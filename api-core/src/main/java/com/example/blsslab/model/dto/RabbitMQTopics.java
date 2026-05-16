package com.example.blsslab.model.dto;

public enum RabbitMQTopics {
    HOUSING_TOPIC("housingTopic");

    public String topic;

    RabbitMQTopics(String topic) {
        this.topic = topic;
    }

}
