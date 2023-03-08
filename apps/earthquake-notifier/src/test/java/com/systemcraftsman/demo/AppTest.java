package com.systemcraftsman.demo;

import com.systemcraftsman.demo.deserializer.NotificationDeserializer;
import com.systemcraftsman.demo.model.LocationNotification;
import com.systemcraftsman.demo.model.Notification;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.rnorth.ducttape.unreliables.Unreliables;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

//TODO: Annotate with @Testcontainers
@Testcontainers
public class AppTest {

    //TODO: Add the Kafka container instance
    @Container
    public static KafkaContainer kafka =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));

    //TODO: Implement the testNotificationSending test method
    @Test
    public void testNotificationSending() throws Exception {
        String topicName = "notifications-test";
        String bootstrapServers = kafka.getBootstrapServers();

        NotificationProducer producer = new NotificationProducer();
        producer.setBootstrapServers(bootstrapServers);
        producer.setTopicName(topicName);

        KafkaConsumer<String, Notification> consumer = new KafkaConsumer<>(ImmutableMap.of(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers, ConsumerConfig.GROUP_ID_CONFIG, "collector-test", ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"), new StringDeserializer(), new NotificationDeserializer());

        consumer.subscribe(Collections.singletonList(topicName));

        producer.produce();

        Unreliables.retryUntilTrue(10, TimeUnit.SECONDS, () -> {
            ConsumerRecords<String, Notification> records = consumer.poll(Duration.ofMillis(100));

            if (records.isEmpty()) {
                return false;
            }

            for (ConsumerRecord<String, Notification> record : records) {
                LocationNotification consumedLocationNotification = (LocationNotification) record.value();
                assertNotNull(consumedLocationNotification);
                assertNotNull(consumedLocationNotification.getLatitude());
                assertNotNull(consumedLocationNotification.getLongitude());
            }

            return true;
        });

        consumer.unsubscribe();

    }
}
