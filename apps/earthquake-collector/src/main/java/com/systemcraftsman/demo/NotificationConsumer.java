package com.systemcraftsman.demo;

import com.systemcraftsman.demo.model.LocationNotification;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class NotificationConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationConsumer.class);

    private String locationUrl;

    @KafkaListener(topics = "${kafka.topic}")
    public void consume(ConsumerRecord<?, ?> consumerRecord) {
        LocationNotification notification = (LocationNotification) consumerRecord.value();
        locationUrl = String.format("https://www.google.com/maps/search/?api=1&query=%s,%s", notification.getLatitude(), notification.getLongitude());
        LOGGER.info(locationUrl);
    }

    public String getLocationUrl() {
        return locationUrl;
    }

}
