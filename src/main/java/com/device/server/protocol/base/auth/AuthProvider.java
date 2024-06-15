package com.device.server.protocol.base.auth;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public abstract class AuthProvider {

    protected final String trackerIdentifier;

    private Instant loadTrackerIdTime;

    public AuthProvider(final String trackerIdentifier) {
        this.trackerIdentifier = trackerIdentifier;
        reloadTrackerId();
    }

    public String getTrackerIdentifier() {
        return trackerIdentifier;
    }

    public String getTrackerId() {
        return trackerIdentifier;
    }

    private void reloadTrackerId() {
        loadTrackerIdTime = Instant.now();
    }

    protected abstract String loadTrackerId();
}
