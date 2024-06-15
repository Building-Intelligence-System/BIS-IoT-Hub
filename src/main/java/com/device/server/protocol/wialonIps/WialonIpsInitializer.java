package com.device.server.protocol.wialonIps;

import com.device.server.protocol.base.BaseInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class WialonIpsInitializer extends BaseInitializer {

    WialonIpsInitializer(final String protocolName, final long idleTimeout) {
        super(protocolName, idleTimeout);
    }

    @Override
    protected void initChannel(final SocketChannel ch) {
        ch.pipeline()
            .addLast("idleTimeout", new IdleStateHandler(0, 0, idleTimeout, TimeUnit.SECONDS))
            .addLast("frameDecoder", new WialonIpsFrameDecoder())
            .addLast("packetEncoder", new WialonIpsPacketEncoder())
            .addLast("packetDecoder", new WialonIpsPacketDecoder())
            .addLast("handler", new WialonIpsHandler(protocolName));
    }
}
