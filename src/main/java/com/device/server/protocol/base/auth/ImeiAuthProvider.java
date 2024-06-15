package com.device.server.protocol.base.auth;

import org.apache.commons.lang3.math.NumberUtils;
import com.device.server.Context;

public class ImeiAuthProvider extends AuthProvider {

    public ImeiAuthProvider(final String identifier) {
        super(identifier);
    }

    @Override
    protected String loadTrackerId() {
        if (!NumberUtils.isDigits(trackerIdentifier)) {
            throw new IllegalArgumentException("IMEI must contain digits only: " + trackerIdentifier);
        }
        return trackerIdentifier;
    }
}
