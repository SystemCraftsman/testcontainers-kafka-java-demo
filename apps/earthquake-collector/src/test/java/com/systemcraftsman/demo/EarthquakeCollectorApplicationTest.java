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
    @Container
    public static KafkaContainer kafka =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));

    //TODO: Register Kafka properties for dynamic values such as Kafka bootstrap servers
    @DynamicPropertySource
    static void registerKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", () -> kafka.getBootstrapServers());
    }

    //TODO: Implement the testNotificationArrival test method
    @Test
    public void testNotificationArrival() {
        Faker faker = new Faker();

        LocationNotification locationNotification = new LocationNotification();
        locationNotification.setLongitude(faker.address().longitude());
        locationNotification.setLatitude(faker.address().latitude());

        producer.send(topic, locationNotification);

        Unreliables.retryUntilTrue(10, TimeUnit.SECONDS, () -> {
            String locationUrl = notificationConsumer.getLocationUrl();

            if (locationUrl == null)
                return false;

            assertNotNull(locationUrl);
            assertTrue(locationUrl.contains(locationNotification.getLatitude()));
            assertTrue(locationUrl.contains(locationNotification.getLongitude()));

            return true;
        });
    }

}


