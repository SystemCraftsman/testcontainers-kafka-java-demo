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
public class AppTest {

    //TODO: Add the Kafka container instance

    //TODO: Implement the testNotificationSending test method
    @Test
    public void testNotificationSending() throws Exception {}
}
