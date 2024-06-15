package com.device.server.protocol.wialonIps;

import com.device.server.protocol.base.BasePacket;

public class WialonIpsPacket extends BasePacket {

    private final WialonIpsUtils.WialonIpsPacketType type;

    public WialonIpsPacket(final WialonIpsUtils.WialonIpsPacketType type) {
        this.type = type;
    }

    public WialonIpsUtils.WialonIpsPacketType getType() {
        return type;
    }

}
