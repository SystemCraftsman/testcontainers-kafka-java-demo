package com.systemcraftsman.demo.earthquakecollector.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;

public class LocationNotification implements Serializable {

    private String longitude;
    private String latitude;

    public Timestamp getTimestamp() {
        return Timestamp.from(Instant.now());
    }
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

}
