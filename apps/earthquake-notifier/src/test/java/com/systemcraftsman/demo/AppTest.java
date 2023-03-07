package com.systemcraftsman.demo;

import com.github.javafaker.Faker;
import com.systemcraftsman.demo.deserializer.NotificationDeserializer;
import com.systemcraftsman.demo.model.LocationNotification;
import com.systemcraftsman.demo.model.Notification;
import com.systemcraftsman.demo.serializer.NotificationSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.rnorth.ducttape.unreliables.Unreliables;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

        KafkaProducer<String, Notification> producer = new KafkaProducer<>(ImmutableMap.of(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers, ProducerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString()), new StringSerializer(), new NotificationSerializer());
        KafkaConsumer<String, Notification> consumer = new KafkaConsumer<>(ImmutableMap.of(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers, ConsumerConfig.GROUP_ID_CONFIG, "collector-test", ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"), new StringDeserializer(), new NotificationDeserializer());

        consumer.subscribe(Collections.singletonList(topicName));

        Faker faker = new Faker();

        LocationNotification locationNotification = new LocationNotification();
        locationNotification.setLongitude(faker.address().longitude());
        locationNotification.setLatitude(faker.address().latitude());

        producer.send(new ProducerRecord<>(topicName, locationNotification)).get();

        Unreliables.retryUntilTrue(10, TimeUnit.SECONDS, () -> {
            ConsumerRecords<String, Notification> records = consumer.poll(Duration.ofMillis(100));

            if (records.isEmpty()) {
                return false;
            }

            for (ConsumerRecord<String, Notification> record : records) {
                LocationNotification consumedLocationNotification = (LocationNotification) record.value();
                assertNotNull(consumedLocationNotification);
                assertEquals(locationNotification.getLatitude(), consumedLocationNotification.getLatitude());
                assertEquals(locationNotification.getLongitude(), consumedLocationNotification.getLongitude());
            }

            return true;
        });

        consumer.unsubscribe();

    }
}
