package com.systemcraftsman.demo.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.systemcraftsman.demo.model.LocationNotification;
import com.systemcraftsman.demo.model.Notification;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Map;

public class NotificationDeserializer implements Deserializer<Notification> {


    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Deserializer.super.configure(configs, isKey);
    }

    @Override
    public Notification deserialize(String topic, byte[] data) {
        ObjectMapper mapper = new ObjectMapper();
        LocationNotification locationNotification = null;
        try {
            locationNotification = mapper.readValue(data, LocationNotification.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return locationNotification;
    }

    @Override
    public Notification deserialize(String topic, Headers headers, byte[] data) {
        return Deserializer.super.deserialize(topic, headers, data);
    }

    @Override
    public void close() {
        Deserializer.super.close();
    }
}