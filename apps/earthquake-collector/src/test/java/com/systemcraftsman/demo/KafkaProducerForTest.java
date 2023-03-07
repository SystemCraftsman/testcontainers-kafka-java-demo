package com.systemcraftsman.demo;

import com.systemcraftsman.demo.model.LocationNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
class KafkaProducerForTest {
    @Autowired
    private KafkaTemplate<String, LocationNotification> kafkaTemplate;

    public void send(String topic, LocationNotification payload) {
        kafkaTemplate.send(topic, payload);
    }
}
