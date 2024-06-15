package com.device.server.protocol.wialonIps;

import com.device.server.protocol.base.BaseProtocol;
import com.device.server.server.TcpServer;

import java.util.List;

public class WialonIpsProtocol extends BaseProtocol {

    static final String NAME = "wialon-ips";

    public WialonIpsProtocol() {
        super(NAME, 54321);
    }

    @Override
    public void initServer(final List<com.device.server.server.base.Server> servers) {
        servers.add(
                new TcpServer(
                        "localhost",
                        this.port,
                        8,
                        16,
                        new WialonIpsInitializer(name, 60)));
    }
}
