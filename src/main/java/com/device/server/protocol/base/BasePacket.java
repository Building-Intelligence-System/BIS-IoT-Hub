package com.device.server.protocol.base;

import com.device.server.model.Telemetry;

import java.util.ArrayList;
import java.util.List;

public abstract class BasePacket {

    private String identifier;
    private boolean authorized;
    private final List<Telemetry> records = new ArrayList<>();

    public String getIdentifier() {
        return identifier;
    }
    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }

    public boolean isAuthorized() {
        return authorized;
    }
    public void setAuthorized(final boolean authorized) {
        this.authorized = authorized;
    }

    public List<Telemetry> getRecords() {
        return records;
    }

}
