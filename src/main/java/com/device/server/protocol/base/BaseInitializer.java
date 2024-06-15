package com.device.server.protocol.base;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public abstract class BaseInitializer extends ChannelInitializer<SocketChannel> {

    protected final String protocolName;
    protected final long idleTimeout;

    public BaseInitializer(final String protocolName, final long idleTimeout) {
        this.protocolName = protocolName;
        this.idleTimeout = idleTimeout;
    }
}
