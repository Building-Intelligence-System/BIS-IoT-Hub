package com.device.server.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Tracker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String imei;

    public Tracker() {

    }

    public Tracker(final String imei) {
        this.imei = imei;
    }

    public Integer getId() {
        return id;
    }

    public String getImei() {
        return imei;
    }
}