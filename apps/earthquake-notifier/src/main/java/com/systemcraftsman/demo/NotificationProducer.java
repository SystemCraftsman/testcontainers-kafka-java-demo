package com.systemcraftsman.demo;

import com.github.javafaker.Faker;
import com.systemcraftsman.demo.model.LocationNotification;
import com.systemcraftsman.demo.serializer.NotificationSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class NotificationProducer {

    private static String BOOTSTRAP_SERVERS = "127.0.0.1:9092";

    public static void produce(){
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, NotificationSerializer.class.getName());

        Faker faker = new Faker();

        LocationNotification locationNotification = new LocationNotification();
        locationNotification.setLongitude(faker.address().longitude());
        locationNotification.setLatitude(faker.address().latitude());

        KafkaProducer<String, LocationNotification> producer = new KafkaProducer<>(properties);
        ProducerRecord<String, LocationNotification> producerRecord = new ProducerRecord<>("notifications", locationNotification);

        System.out.println(producerRecord.value());
        producer.send(producerRecord);

        producer.flush();
        producer.close();
    }
}
