package com.systemcraftsman.demo;

import com.github.javafaker.Faker;
import com.systemcraftsman.demo.model.LocationNotification;
import org.junit.jupiter.api.Test;
import org.rnorth.ducttape.unreliables.Unreliables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@DirtiesContext
@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class EarthquakeCollectorApplicationTest {

    @Value("${kafka.topic}")
    private String topic;

    @Autowired
    private NotificationConsumer notificationConsumer;

    @Autowired
    private KafkaProducerForTest producer;

    //TODO: Add the Kafka container instance

    //TODO: Register Kafka properties for dynamic values such as Kafka bootstrap servers
    @DynamicPropertySource
    static void registerKafkaProperties(DynamicPropertyRegistry registry) {}

    //TODO: Implement the testNotificationArrival test method
    @Test
    public void testNotificationArrival() {}

}


