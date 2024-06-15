package com.device.server.protocol.wialonIps;

import com.device.server.Context;
import com.device.server.model.Tracker;
import com.device.server.protocol.base.BaseHandler;

public class WialonIpsHandler<T extends WialonIpsPacket> extends BaseHandler<T> {

    public WialonIpsHandler(final String protocolName) {
        super(protocolName);
    }

    @Override
    protected void handle(final T packet) throws Exception {
        switch (packet.getType()) {
            case L:
                if (authenticate(packet.getIdentifier())) {
                    packet.setAuthorized(true);
                }
                Context.getTrackerRepository().save(new Tracker(packet.getIdentifier()));
                break;

            case SD:
            case D:
            case B:
                checkAuthorized();
                writeTelemetries(packet.getRecords());
                break;

            case P:
            case M:
                break;

            default:
                throw new IllegalArgumentException();
        }
    }
}
