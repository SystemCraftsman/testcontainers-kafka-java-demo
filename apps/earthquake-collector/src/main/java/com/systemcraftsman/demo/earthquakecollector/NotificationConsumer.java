package com.systemcraftsman.demo.earthquakecollector;

import com.systemcraftsman.demo.earthquakecollector.model.LocationNotification;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class NotificationConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationConsumer.class);

    @KafkaListener(topics = "${kafka.topic}")
    public void consume(ConsumerRecord<?, ?> consumerRecord) {
        LocationNotification notification = (LocationNotification) consumerRecord.value();
        LOGGER.info("https://www.google.com/maps/search/?api=1&query={},{}", notification.getLatitude(), notification.getLongitude());
    }
}
