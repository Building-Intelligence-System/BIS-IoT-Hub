package com.device.server.server;

import com.device.server.protocol.base.BaseProtocol;
import com.device.server.protocol.wialonIps.WialonIpsProtocol;
import com.device.server.server.base.Server;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public final class ServerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerService.class);

    private static final String PROTOCOL_POSTFIX = "Protocol";
    private static final Map<String, Class<?>> AVAILABLE_PROTOCOLS;

    static {
        AVAILABLE_PROTOCOLS = new HashMap<>();

        AVAILABLE_PROTOCOLS.put("adm", WialonIpsProtocol.class);
    }

    private final List<Server> servers = new ArrayList<>();

    @PostConstruct
    public void init() throws ReflectiveOperationException {
        for (final Map.Entry<String, Class<?>> entry : AVAILABLE_PROTOCOLS.entrySet()) {
            final Class<?> protocolClass = AVAILABLE_PROTOCOLS.get(entry.getKey());

            final BaseProtocol baseProtocol = (BaseProtocol) protocolClass.getDeclaredConstructor().newInstance();
            initProtocolServer(baseProtocol.getPort(), baseProtocol);
        }
    }

    public void startAll() throws Exception {
        for (final Server server : servers) {
            server.start();
        }
    }

    public void stopAll() throws Exception {
        for (final Server server : servers) {
            server.stop();
        }
    }

    private void initProtocolServer(final int port, final BaseProtocol protocol) {
        protocol.initServer(servers);

        LOGGER.info(
            MessageFormat.format("Run {0} server on {1} port.",
                protocol.getClass().getSimpleName().replace(PROTOCOL_POSTFIX, ""), String.valueOf(port)));
    }
}
