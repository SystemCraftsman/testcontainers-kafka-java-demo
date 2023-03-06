package com.systemcraftsman.demo.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;

public abstract class Notification implements Serializable {
    public Timestamp getTimestamp() {
        return Timestamp.from(Instant.now());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("timestamp", getTimestamp()).toString();
    }
}
