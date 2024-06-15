package com.device.server.server.base;

public abstract class BaseServer implements Server {

    protected final String address;
    protected final int port;

    public BaseServer(final String address, final int port) {
        this.address = address;
        this.port = port;
    }
}
