package com.device.server;

import com.device.server.protocol.wialonIps.WialonIpsProtocol;
import com.device.server.server.base.Server;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class WialonIpsProtocolFTest extends BaseProtocolFTest {
    private static boolean inited = false;
    private static int PORT;

    private static Server server;

    static final String AUTH_IMEI = "100000000000000";
    static byte[] VALID_AUTH_PACKET_VERSION_1_1 = "#L#100000000000000;123\r\n".getBytes(StandardCharsets.US_ASCII);
    static byte[] VALID_AUTH_RESPONSE = "#AL#1\r\n".getBytes(StandardCharsets.US_ASCII);

    static byte[] VALID_DATA_PACKET_VERSION_1_1 = "#D#051218;123045;5544.6025;N;03739.6834;E;50;10;100;33;5.0;250;10;14.77,0.02,3.6;ibutton;fuel:2:45.8\r\n".getBytes(StandardCharsets.US_ASCII);
    static byte[] VALID_DATA_RESPONSE = "#AD#1\r\n".getBytes(StandardCharsets.US_ASCII);


    @BeforeEach
    public void beforeAll() throws Exception {
        if (!inited) {
            PORT = 54321;

            final List<Server> servers = new ArrayList<>();
            final WialonIpsProtocol protocol = new WialonIpsProtocol();
            protocol.initServer(servers);
            server = servers.get(0);
            server.start();

            inited = true;
        }
    }

    @AfterClass
    public static void afterAll() throws Exception {
        server.stop();
    }

    @Test
    public void testAuthPacket() throws Exception {
        final Socket socket = new Socket("localhost", PORT);
        final InputStream in = socket.getInputStream();
        final OutputStream out = socket.getOutputStream();
        assertTrue(socket.isConnected());

        out.write(VALID_AUTH_PACKET_VERSION_1_1);
        out.flush();
        byte[] resp = new byte[VALID_AUTH_RESPONSE.length];
        assertEquals(resp.length, in.read(resp));
        assertArrayEquals(VALID_AUTH_RESPONSE, resp);

        out.write(VALID_DATA_PACKET_VERSION_1_1);
        out.flush();
        resp = new byte[VALID_DATA_RESPONSE.length];
        assertEquals(resp.length, in.read(resp));
        assertArrayEquals(VALID_DATA_RESPONSE, resp);

        out.close();
        in.close();
        socket.close();
        assertTrue(socket.isClosed());
    }
}
