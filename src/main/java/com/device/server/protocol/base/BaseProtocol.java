package com.device.server.protocol.base;

import com.device.server.server.base.Server;

import java.util.List;

public abstract class BaseProtocol {

    protected final String name;
    protected final Integer port;

    public BaseProtocol(final String name, final Integer port) {
        this.name = name;
        this.port = port;
    }

    public Integer getPort() {
        return port;
    }

    public abstract void initServer(List<Server> servers);
}
