package com.systemcraftsman.demo;

import com.github.javafaker.Faker;
import com.systemcraftsman.demo.model.LocationNotification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@DirtiesContext
@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class EarthquakeCollectorApplicationTests {

    @Value("${kafka.topic}")
    private String topic;

    @Container
    public static KafkaContainer kafka =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));

    @DynamicPropertySource
    static void registerKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", () -> kafka.getBootstrapServers());
    }

    @Autowired
    private NotificationConsumer notificationConsumer;

    @Autowired
    private KafkaTestProducer producer;

    @Test
    public void testNotificationArrival() throws Exception {
        Faker faker = new Faker();

        LocationNotification locationNotification = new LocationNotification();
        locationNotification.setLongitude(faker.address().longitude());
        locationNotification.setLatitude(faker.address().latitude());

        producer.send(topic, locationNotification);

        while(notificationConsumer.getLocationUrl() == null)
            Thread.sleep(1000);

        String locationUrl = notificationConsumer.getLocationUrl();

        assertNotNull(locationUrl);
        assertTrue(locationUrl.contains(locationNotification.getLatitude()));
        assertTrue(locationUrl.contains(locationNotification.getLongitude()));
    }

    @Test
    void contextLoads() {
    }

}


@Component
class KafkaTestProducer {
    @Autowired
    private KafkaTemplate<String, LocationNotification> kafkaTemplate;

    public void send(String topic, LocationNotification payload) {
        kafkaTemplate.send(topic, payload);
    }
}


